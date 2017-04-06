(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['Auth','$rootScope','CommonMethods','AccessDoor', 'Resident' ,'House','Vehicule','Visitant','AlertService', 'Principal'];

    function AccessDoorController(Auth,$rootScope,CommonMethods, AccessDoor, Resident, House,Vehicule,Visitant,AlertService,Principal) {
        var residentsList, vehiculesList, housesList, emergencyList, visitorsList;
        var securityKey, emergencyKey, housenumber;
        var vm = this;
          vm.logout = logout;
              vm.show = 4;
        vm.isAuthenticated = Principal.isAuthenticated;
        function logout() {
            Auth.logout();
            $rootScope.companyUser = undefined;

        }
        loadResidents();

        function loadResidents() {
            Resident.query({companyId: $rootScope.companyId}, onSuccessResident, onError);
            function onSuccessResident(residents, headers) {
               residentsList = residents;
               loadHouses();
            }
        }

        function loadHouses() {
           House.query({companyId: $rootScope.companyId}, onSuccessHouse, onError);
           function onSuccessHouse(houses, headers) {
              housesList = houses;
              loadVehicules()
           }
        }

        function loadVehicules() {
           Vehicule.query({companyId: $rootScope.companyId}, onSuccessVehicule, onError);
           function onSuccessVehicule(vehicules, headers) {
              vehiculesList = vehicules;
             loadVisitors()
           }
        }

    function loadVisitors() {
           Visitant.query({companyId: $rootScope.companyId}, onSuccessVisitor, onError);
           function onSuccessVisitor(visitors, headers) {
              visitorsList = visitors;
              showLists()
           }
        }
        function showLists(){
            console.log(residentsList);
               console.log(housesList);
                  console.log(vehiculesList);
                      console.log(visitorsList);
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.getVehicule = function(){
            vm.id_number = "";
            if (vm.id_vehicule == "") {
                 $("#vehicule_license_plate").css("text-transform", "none");
                 $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                  vm.show = 4;
            } else {
                 $("#vehicule_license_plate").css("text-transform", "uppercase");

                        vm.show = 3;
                        angular.forEach(vehiculesList, function(item, index) {
                            if (item.licenseplate.toUpperCase() == vm.id_vehicule.toUpperCase()) {
                                vm.vehiculeRegisteredTitle = "Vehículo registrado";
                                vm.colorVehiculeRegistered = "green-font";
                                vm.imageVehiculeState = "success-car-image";
                                $("#vehiculeAccess").fadeIn(100);
                                vm.show = 2;
                                vm.vehicule = item;


                                angular.forEach(housesList, function(itemHouse, index) {
                                    if (itemHouse.id == item.houseId) {
                                        vm.SelectedHouse = itemHouse.housenumber;
                                        housenumber = itemHouse.housenumber;
                                        securityKey = itemHouse.securityKey;
                                        emergencyKey = itemHouse.emergencyKey;
                                    }
                                });
                                if (item.enabled == 0) {
                                    vm.vehiculeRegisteredTitle = "Vehículo no habilitado para ingresar";
                                    vm.colorVehiculeRegistered = "red-font";
                                    vm.imageVehiculeState = "disabled-car-image";
                                }
                            }
                        });

                    }

        }

        vm.getResident = function() {
                vm.id_vehicule = "";
                $("#vehicule_license_plate").css("text-transform", "none");
                $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                if (vm.id_number == "") {
                    vm.show = 4;
                } else {
                    vm.show = 3;
                    angular.forEach(residentsList, function(item, index) {
                        if (item.identificationnumber == vm.id_number) {
                            vm.residentRegisteredTitle = "Residente registrado"
                            vm.colorResidentRegistered = "green-font"
                            console.log(item.enabled)
                            $("#residentAccess").fadeIn(100);
                            vm.show = 1;
                            vm.name = item.name + " " + item.lastname + " " + item.secondlastname;
                            vm.indentification = item.identificationnumber;
                            vm.resident = item;
                            angular.forEach(housesList, function(itemHouse, index) {
                                if (itemHouse.id == item.houseId) {
                                    housenumber = itemHouse.housenumber;
                                    vm.housenumber = itemHouse.housenumber;
                                    securityKey = itemHouse.securityKey;
                                    emergencyKey = itemHouse.emergencyKey;
                                }
                            });
                            if (item.enabled == 0) {
                                vm.residentRegisteredTitle = "Residente no habilitado para ingresar";
                                vm.colorResidentRegistered = "red-font";
                            }
                        }
                    });

//                    if ($scope.id_number.length > 6) {
//
//                        accessFunctions.findRegisteredVisitant($scope.id_number).success(function(data) {
//
//                            if (data == 0) {
//
//                            } else {
//                                $("#loadingIconnn").fadeOut(0);
//                                $("#visitantInvitedtAccess").fadeIn(0);
//                                $scope.show = 10;
//                                $scope.invited_visitant_name = data.name
//                                $scope.invited_visitant_last_name = data.last_name;
//                                $scope.invited_visitant_second_last_name = data.second_last_name;
//                                angular.forEach(housesPrueba, function(itemHouse, index) {
//                                        if (itemHouse.id == data.id_house) {
//                                            $scope.invited_visitant_house_number = itemHouse.house_number;
//                                        }
//                                    })
//                                    // $scope.invited_visitant_house_number = data.id_house;
//                                $scope.invited_visitant_indentification = data.identification_number;
//                                if (data.license_plate == null) {
//                                    $scope.invited_visitant_license_plate = "Ninguna";
//
//                                } else {
//                                    $scope.invited_visitant_license_plate = data.license_plate;
//                                }
//
//
//                            }
//                        });
//                    }
                }
            };

            vm.getKeys = function() {
                if (securityKey == null || emergencyKey == null) {
                    toastr["error"]("Esta casa aún no tiene claves de seguridad asignadas");
                } else {
                    bootbox.dialog({
                        message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div>\
                        <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de seguridad: <span class="font-20 bold" id="security_key">1134314</span></h1></div>\
                          <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de emergencia: <span class="font-20 bold" id="emergency_key">1134314</span></h1></div>',
                        closeButton: false,
                        buttons: {
                            confirm: {
                                label: 'Ocultar',
                                className: 'btn-success'
                            }
                        },
                    })
                    document.getElementById("key_id_house").innerHTML = "" + housenumber;
                    document.getElementById("security_key").innerHTML = "" + securityKey;
                    document.getElementById("emergency_key").innerHTML = "" + emergencyKey;
                }
            }
            function clearInputs() {
                vm.visitor_id_number = ""
                vm.visitor_name = ""
                vm.visitor_last_name = "";
                vm.visitor_second_last_name = "";
                vm.visitor_license_plate = "";
                vm.house = "";
            }

            vm.searchVisitor = function() {
                vm.show = 5;
                $("#license_plate").css("text-transform", "none");
                $("#license_plate").attr("placeholder", "Número placa (sin guiones)");
//
                clearInputs();
//
//                if ($scope.id_number == undefined || $scope.id_number == "") {
//
//                } else {
//
//                    $scope.visitor_id_number = $scope.id_number;
//                    angular.forEach(visitorsList, function(itemVisitor, index) {
//                        if (itemVisitor.identification_number == $scope.visitor_id_number && itemVisitor.is_invited == 0) {
//
//                            $scope.visitor_name = itemVisitor.name;
//                            $scope.visitor_last_name = itemVisitor.last_name;
//                            $scope.visitor_second_last_name = itemVisitor.second_last_name;
//                            $scope.visitor_license_plate = itemVisitor.license_plate;
//
                            vm.houses = housesList;
//                            var house = $scope.houses.filter(function(el) {
//                                return el.id == itemVisitor.id_house;
//                            });
//                            $scope.house = house[0];
//
//                        }
//                    });
//                }
//
//                if ($scope.id_vehicule == undefined || $scope.id_vehicule == "") {} else {
//
//                    $scope.visitor_license_plate = $scope.id_vehicule;
//                    angular.forEach(visitorsList, function(itemVisitor, index) {
//                        if (itemVisitor.license_plate == $scope.visitor_license_plate && itemVisitor.is_invited == 0) {
//                            $scope.visitor_name = itemVisitor.name;
//                            $scope.visitor_last_name = itemVisitor.last_name;
//                            $scope.visitor_second_last_name = itemVisitor.second_last_name;
//                            $scope.visitor_license_plate = itemVisitor.license_plate;
//                            $scope.visitor_id_number = itemVisitor.identification_number;
//                            $scope.houses = housesPrueba;
//                            var house = $scope.houses.filter(function(el) {
//                                return el.id == itemVisitor.id_house;
//                            });
//                            $scope.house = house[0];
//
//                        }
//                    });
//                }
            }
            vm.deleteResidentVehiculeSpots = function() {
                $("#vehicule_license_plate").css("text-transform", "none");
                $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                vm.id_number = "";
                vm.id_vehicule = "";
                vm.show = 4;

            }
    }
})();
