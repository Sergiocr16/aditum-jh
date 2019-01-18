(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchDetailController', WatchDetailController);

    WatchDetailController.$inject = ['$rootScope', 'entity', 'CommonMethods', '$state', 'Modal','$scope'];

    function WatchDetailController($rootScope, entity, CommonMethods, $state, Modal,$scope) {
        var vm = this;
        vm.isReady = true;
        vm.watch = formatWatch(entity);
        $rootScope.mainTitle = "Detalle de turno";
        moment.locale("es");
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });


        vm.detailOfficer = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('officer.details', {
                id: encryptedId
            })
        }

        function formatWatch(watch) {
            moment.locale("es");

            watch.initialtime = moment(watch.initialtime).format('dddd D [de] MMMM [del] YYYY, h:mm a');
            if (watch.finaltime === null) {
                watch.finaltime = 'Aún en progreso'
            } else {
                watch.finaltime = moment(watch.finaltime).format('dddd D [de] MMMM [del] YYYY, h:mm a');
            }
            watch.totalOfficers = totalOfficer(watch);
            return watch;
        }

        function totalOfficer(watch) {
            var ids = watch.responsableofficer.split("||");
            return ids.length + 1;
        }
    }
})();


