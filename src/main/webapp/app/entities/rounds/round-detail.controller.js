(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RoundDetailController', RoundDetailController);

    RoundDetailController.$inject = ['Modal', '$rootScope', 'entity', 'Rounds', 'CommonMethods', '$stateParams', '$scope'];

    function RoundDetailController(Modal, $rootScope, entity, Rounds, CommonMethods, $stateParams, $scope) {

        var vm = this;
        vm.isReady = false;
        $rootScope.active = "round-detail";
        $rootScope.mainTitle = 'Detalle de ronda';
        vm.round = entity;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        vm.lateTime = calculateDifBetweenHours(vm.round.startingTime, vm.round.executionDate);
        if (vm.round.finished) {
            vm.durationTime = calculateDifBetweenHours(vm.round.finishingTime, vm.round.startingTime);
        }

        function calculateDifBetweenHours(startingTime, finishTime) {
            var ms = moment(startingTime).diff(moment(finishTime));
            var d = moment.duration(ms);
            var s = Math.floor(d.asHours()) + moment.utc(ms).format(":mm:ss");
            return s;
        }

        vm.getColorDone = function (round) {
            return round.done ? "#44b6ae" : "rgb(154, 0, 7)";
        }
        paintMap();
        vm.reload = function () {
            $rootScope.isShowingLoadingBar = true;
            var id = CommonMethods.decryptIdUrl($stateParams.id)
            return Rounds.getOne({uid: id}, function (data) {
                vm.round = data;
                $rootScope.isShowingLoadingBar = false;
                paintMap();
            });
        }

        function paintMap() {
            var mapOptions = {
                zoom: vm.round.mapZoom,
                center: new google.maps.LatLng(vm.round.latitudeCenter, vm.round.longitudeCenter),
                mapTypeId: google.maps.MapTypeId.SATELLITE,
                disableDefaultUI: true
            }

            vm.map = new google.maps.Map(document.getElementById('map'), mapOptions);
            createMarkers();
            createPath(vm.round.checkpoints);
        }

        function createMarkers() {
            vm.markers = [];

            var infoWindow = new google.maps.InfoWindow();

            var createMarker = function (checkpoint) {
                var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachf';
                var marker = new google.maps.Marker({
                    map: vm.map,
                    position: new google.maps.LatLng(checkpoint.latitude, checkpoint.longitude),
                    title: "Punto " + checkpoint.order,
                    label: {text: checkpoint.order + "", color: "white"},
                    icon: {
                        path: google.maps.SymbolPath.CIRCLE,
                        scale: 11,
                        fillColor: checkpoint.done ? "#44b6ae" : "rgb(154, 0, 7)",
                        fillOpacity: 1.0,
                        strokeWeight: 0.4
                    },
                });
                if (checkpoint.done) {
                    var arrivalTime = "Completado a las " + moment(checkpoint.arrivalTime).format('hh:mm a');
                } else {
                    arrivalTime = "No completado"
                }
                marker.content = '<div class="infoWindowContent">' + arrivalTime + '</div>';
                google.maps.event.addListener(marker, 'click', function () {
                    infoWindow.setContent('<h5>' + marker.title + '</h5>' + marker.content);
                    infoWindow.open(vm.map, marker);
                });
                vm.markers.push(marker);
            }

            for (var i = 0; i < vm.round.checkpoints.length; i++) {
                createMarker(vm.round.checkpoints[i]);
            }
        }


        vm.openInfoWindow = function (e, selectedMarker) {
            e.preventDefault();
            google.maps.event.trigger(selectedMarker, 'click');
        }

        function createPoint(lat, long) {
            return new google.maps.LatLng(lat, long);
        }

        function createPath(checkpoints) {
            var officersPlanCompletedCoordinates = [];
            var officersPlanIncompletedCoordinates = [];
            var lastDone;
            for (var i = 0; i < checkpoints.length; i++) {
                var point = createPoint(checkpoints[i].latitude, checkpoints[i].longitude);
                if (checkpoints[i].done) {
                    officersPlanCompletedCoordinates.push(point);
                    lastDone = point;
                } else {
                    officersPlanIncompletedCoordinates.push(point);
                }
            }
            if (lastDone) {
                officersPlanIncompletedCoordinates.unshift(lastDone);
            }
            var incompletedPath = new google.maps.Polyline({
                path: officersPlanIncompletedCoordinates,
                geodesic: true,
                strokeColor: 'rgb(154, 0, 7)',
                strokeOpacity: 1.0,
                strokeWeight: 3
            });
            var completedPath = new google.maps.Polyline({
                path: officersPlanCompletedCoordinates,
                geodesic: true,
                strokeColor: '#44b6ae',
                strokeOpacity: 1.0,
                strokeWeight: 3
            });
            incompletedPath.setMap(vm.map);
            completedPath.setMap(vm.map);
        }

    }
})();
