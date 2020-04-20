(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseARDialogController', HouseARDialogController);

    HouseARDialogController.$inject = ['globalCompany', '$scope', 'Modal', 'entity', 'HouseSecurityDirection', 'House', 'CompanyConfiguration', '$rootScope', '$state'];

    function HouseARDialogController(globalCompany, $scope, Modal, entity, HouseSecurityDirection, House, CompanyConfiguration, $rootScope, $state) {
        var vm = this;
        $rootScope.active = "viviendas";
        vm.houseAR = entity;
        vm.markers = [];
        vm.options = {
            toolbar: [
                // [groupName, [list of button]]
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['font', ['strikethrough']],
                // ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                // ['height', ['height']]
            ]
        }
        if (vm.houseAR.id == undefined) {
            vm.button = "Registrar"
            vm.title = "Registrar vivienda"
        } else {
            vm.button = "Editar"
            vm.title = "Editar vivienda"

        }
        $rootScope.mainTitle = vm.title;
        vm.isReady = false;


        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        getConfiguration();

        function getConfiguration() {
            CompanyConfiguration.getByCompanyId({companyId: globalCompany.getId()}).$promise.then(onSuccessCompany, onError);
        }

        function onSuccessCompany(data) {
            angular.forEach(data, function (configuration, key) {
                vm.companyConfiguration = configuration;
            });
            loadQuantities();
        }

        function onError() {
        }

        function geocodePosition(pos) {
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode
            ({
                    latLng: pos
                },
                function (results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        $("#pac-input").val(results[0].formatted_address);
                    } else {
                        console.log('Cannot determine address at this location.' + status);
                    }
                }
            );
        }

        function paintMap(position) {
            var currentPosition = position.coords;
            var mapOptions = {
                zoom: 15,
                center: new google.maps.LatLng(currentPosition.latitude, currentPosition.longitude),
                mapTypeId: 'roadmap'
            }
            vm.map = new google.maps.Map(document.getElementById('map'), mapOptions);
            // Create the search box and link it to the UI element.
            var pacinput = "<input id='pac-input' ng-model='vm.houseAR.ubication.smallDirection' class='controls' type='text' placeholder='Buscar ubicación'><br /><br />";
            $('#map').append(pacinput);
            var input = document.getElementById('pac-input');
            var searchBox = new google.maps.places.SearchBox(input);
            vm.map.controls[google.maps.ControlPosition.TOP_CENTER].push(input);

            // Bias the SearchBox results towards current map's viewport.
            vm.map.addListener('bounds_changed', function () {
                searchBox.setBounds(vm.map.getBounds());
            });

            // Listen for the event fired when the user selects a prediction and retrieve
            // more details for that place.
            searchBox.addListener('places_changed', function () {
                var places = searchBox.getPlaces();
                if (places.length == 0) {
                    return;
                }

                // Clear out the old markers.
                vm.markers.forEach(function (marker) {
                    marker.setMap(null);
                });

                // For each place, get the icon, name and location.
                var bounds = new google.maps.LatLngBounds();
                places.forEach(function (place) {
                    if (!place.geometry) {
                        console.log("Returned place contains no geometry");
                        return;
                    }

                    // Create a marker for each place.
                    var marker = new google.maps.Marker({
                        map: vm.map,
                        position: place.geometry.location,
                        title: "Descripción física",
                        label: {text: "Arrastrar hasta ubicación de vivienda", color: "black"},
                        draggable: true,
                        animation: google.maps.Animation.DROP,
                        icon: {
                            url: '../../content/images/house-ar-icon.png',
                            labelOrigin: {x: 12, y: -10}
                        },
                    });
                    vm.houseAR.ubication.latitude = place.geometry.location.lat();
                    vm.houseAR.ubication.longitude = place.geometry.location.lng();
                    google.maps.event.addListener(marker, 'dragend', function () {
                        var lat = marker.getPosition().lat();
                        var lng = marker.getPosition().lng();
                        vm.houseAR.ubication.latitude = lat;
                        vm.houseAR.ubication.longitude = lng;
                        geocodePosition(marker.getPosition());
                    });

                    var infoWindow = new google.maps.InfoWindow();
                    marker.content = '<div class="infoWindowContent">Esta es la ubicación exacta de la vivienda.</div>';
                    google.maps.event.addListener(marker, 'click', function () {
                        infoWindow.setContent('<h5>' + marker.title + '</h5>' + marker.content);
                        infoWindow.open(vm.map, marker);
                    });
                    vm.markers.push(marker);


                    if (place.geometry.viewport) {
                        // Only geocodes have viewport.
                        bounds.union(place.geometry.viewport);
                    } else {
                        bounds.extend(place.geometry.location);
                    }
                });
                vm.map.fitBounds(bounds);
            });
            if (vm.houseAR.id == null) {
                createMarkerPosition(position.coords)
            }

        }

        getConfiguration();

        function getConfiguration() {
            CompanyConfiguration.getByCompanyId({companyId: globalCompany.getId()}).$promise.then(onSuccessCompany, onError);
        }

        function onSuccessCompany(data) {
            angular.forEach(data, function (configuration, key) {
                vm.companyConfiguration = configuration;
            });
            loadQuantities();
        }

        function createMarkerPosition(coords) {
            var marker = new google.maps.Marker({
                map: vm.map,
                position: new google.maps.LatLng(coords.latitude, coords.longitude),
                title: "Ubicación de la vivienda",
                label: {text: "Arrastrar hasta ubicación de vivienda", color: "black"},
                draggable: true,
                animation: google.maps.Animation.DROP,
                icon: {
                    url: '../../content/images/house-ar-icon.png',
                    labelOrigin: {x: 12, y: -10}
                },
            });
            vm.houseAR.ubication.latitude = coords.latitude;
            vm.houseAR.ubication.longitude = coords.longitude;
            var infoWindow = new google.maps.InfoWindow();
            marker.content = '<div class="infoWindowContent">Esta es la ubicación exacta de la vivienda.</div>';
            geocodePosition(marker.getPosition());
            google.maps.event.addListener(marker, 'dragend', function () {
                var lat = marker.getPosition().lat();
                var lng = marker.getPosition().lng();
                vm.houseAR.ubication.latitude = lat;
                vm.houseAR.ubication.longitude = lng;
                geocodePosition(marker.getPosition());
            });
            google.maps.event.addListener(marker, 'click', function () {
                infoWindow.setContent('<h5>' + marker.title + '</h5>' + marker.content);
                infoWindow.open(vm.map, marker);

            });
            vm.markers.push(marker);
        }


        function getLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(paintMap);
            } else {
                x.innerHTML = "Geolocation is not supported by this browser.";
            }
        }


        function generateLoginCode() {
            var text = "";
            var letters = "";
            var numbers = "";
            var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

            for (var i = 0; i < 4; i++)
                letters += possible.charAt(Math.floor(Math.random() * possible.length));

            numbers = Math.floor((Math.random() * 899) + 100);
            text = numbers + "" + letters;
            return text.toUpperCase();
        }

        loadQuantities();

        function loadQuantities() {
            House.query({companyId: globalCompany.getId()}, onSuccess, onError);
        }

        function onSuccess(data) {
            vm.houseARQuantity = data.length;
            vm.isReady = true;
            getLocation();
        }

        if (vm.houseAR.id !== null) {
            vm.title = "Editar vivienda";
            vm.button = "Editar";

        } else {
            vm.title = "Registrar vivienda";
            vm.button = "Registrar";
        }


        function save() {
            if (vm.houseAR.extension == undefined) {
                vm.extension = 'noTengoExtensionCODE';
            } else {
                vm.extension = vm.houseAR.extension;
            }
            if (vm.houseAR.due == null || vm.houseAR.due == undefined) {
                vm.houseAR.due = 0;
            }
            if (vm.houseAR.squareMeters == null || vm.houseAR.squareMeters == undefined) {
                vm.houseAR.squareMeters = 0;
            }
            vm.isSaving = true;
            var wordOnModal = vm.houseAR.id == undefined ? "registrar" : "modificar"
            vm.houseAR.ubication.smallDirection = $("#pac-input").val();
            Modal.confirmDialog("¿Está seguro que desea " + wordOnModal + " la vivienda?", "", function () {
                if (vm.houseAR.id !== null) {
                    Modal.showLoadingBar();
                    House.validateUpdate({
                        houseId: vm.houseAR.id,
                        houseNumber: vm.houseAR.housenumber.toUpperCase(),
                        extension: vm.extension,
                        companyId: globalCompany.getId()
                    }, onSuccessUp, onErrorUp)
                } else {
                    if (vm.companyConfiguration.quantityhouses <= vm.houseARQuantity) {
                        Modal.toast("Ha excedido la cantidad de vivienda permitidas para registrar, contacte el encargado de soporte.");
                        Modal.hideLoadingBar();
                    } else {
                        Modal.showLoadingBar();
                        vm.houseAR.companyId = globalCompany.getId();
                        vm.houseAR.desocupationinitialtime = new Date();
                        vm.houseAR.desocupationfinaltime = new Date();
                        vm.houseAR.loginCode = generateLoginCode();
                        vm.houseAR.codeStatus = 0;
                        House.validate({
                            houseNumber: vm.houseAR.housenumber,
                            extension: vm.extension,
                            companyId: globalCompany.getId()
                        }, onSuccess, onError)
                    }
                }
            })


            function onSuccessUp(data) {
                Modal.hideLoadingBar();
                if (vm.houseAR.id !== data.id) {
                    Modal.toast("El número de vivienda o de extensión ingresado ya existe.");
                } else {
                    HouseSecurityDirection.update(vm.houseAR, onSaveSuccess, onSaveError);
                }
            }

            function onErrorUp() {
                HouseSecurityDirection.update(vm.houseAR, onSaveSuccess, onSaveError);
            }

            function onSuccess(data) {
                Modal.hideLoadingBar();
                Modal.toast("El número de vivienda o de extensión ingresado ya existe.");
            }

            function onError() {
                HouseSecurityDirection.save(vm.houseAR, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            if (entity.id == null) {
                // var balance = {houseId: parseInt(result.id), extraordinary: 0, commonAreas: 0, maintenance: 0};
                // Balance.save(balance);
            }
            $state.go('houses-tabs.house');
            Modal.hideLoadingBar();
            if (vm.houseAR.id !== null) {
                Modal.toast("Se editó la vivienda correctamente");
            } else {
                Modal.toast("Se registró la vivienda correctamente");
            }
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
