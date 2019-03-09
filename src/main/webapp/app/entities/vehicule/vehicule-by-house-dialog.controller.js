(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeByHouseDialogController', VehiculeByHouseDialogController);

    VehiculeByHouseDialogController.$inject = ['companyUser','$state', 'CommonMethods', '$rootScope', 'Principal', '$timeout', '$scope', '$stateParams', 'entity', 'Vehicule', 'House', 'Company', 'WSVehicle', 'Brand', 'globalCompany', 'Modal'];

    function VehiculeByHouseDialogController(companyUser,$state, CommonMethods, $rootScope, Principal, $timeout, $scope, $stateParams, entity, Vehicule, House, Company, WSVehicle, Brand, globalCompany, Modal) {
        $rootScope.active = "vehiculesHouses";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;
        $rootScope.mainTitle = vm.title;

        vm.required = 1;
        vm.isReady = false;
        Brand.query({}, onSuccessBrand);
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.vehicule.houseId = companyUser.houseId;
        vm.myPlate = vm.vehicule.licenseplate;
        vm.houses = House.query();
        angular.element(document).ready(function () {
            ColorPicker.init();
        });

        if (vm.vehicule.id !== null) {
            vm.title = "Editar vehículo";
            vm.button = "Editar";

        } else {
            vm.title = "Registrar vehículo";
            vm.button = "Registrar";
        }
        $rootScope.mainTitle = vm.title;

        vm.validate = function () {
            var invalido = 0;

            function hasWhiteSpace(s) {
                function tiene(s) {
                    return /\s/g.test(s);
                }

                if (tiene(s) || s == undefined) {
                    return true
                }
                return false;
            }

            function hasCaracterEspecial(s) {
                var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "{", "}", "[", "]", "''"];
                var invalido = 0;
                angular.forEach(caracteres, function (val, index) {
                    if (s != undefined) {
                        for (var i = 0; i < s.length; i++) {
                            if (s.charAt(i) == val) {
                                invalido++;
                            }
                        }
                    }
                })
                if (invalido == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            if (vm.vehicule.licenseplate == undefined || hasWhiteSpace(vm.vehicule.licenseplate)) {
                Modal.toast("No puede ingresar la placa con espacios en blanco");
                invalido++;
            } else if (hasCaracterEspecial(vm.vehicule.licenseplate)) {
                invalido++;
                Modal.toast("No puede ingresar la placa con guiones o cualquier otro carácter especial");
            }
            if (invalido == 0) {
                return true;
            } else {
                return false;
            }
        }

        function haswhiteLicensePlate(s) {
            return /\s/g.test(s);
        }


        function hasCaracterEspecial(s) {
            var caracteres = [, ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^", "!"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i) == val) {
                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                return false;
            } else {
                return true;
            }
        }

        vm.validateLicenseplate = function (vehicule) {
            if (hasCaracterEspecial(vehicule.licenseplate) || haswhiteLicensePlate(vehicule.licenseplate)) {
                vehicule.validLicenseplate = 0;
            } else {
                vehicule.validLicenseplate = 1;
            }
        };
        function onSuccessBrand(brands) {
            vm.brands = brands;

        }

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            vm.isReady = true;

        }

        vm.submitColor = function () {
            vm.vehicule.color = $('#color').css('background-color');
        }


        function save() {
            if (vm.validate()) {
                if (vm.vehicule.color == undefined) {
                    vm.vehicule.color = "rgb(255, 255, 255)";
                }
                vm.vehicule.enabled = 1;
                vm.vehicule.companyId = globalCompany.getId();
                vm.vehicule.houseId = $rootScope.companyUser.houseId;
                vm.vehicule.licenseplate = vm.vehicule.licenseplate.toUpperCase();
                vm.isSaving = true;
                if (vm.vehicule.id !== null) {
                    if (vm.myPlate !== vm.vehicule.licenseplate) {
                        Vehicule.getByCompanyAndPlate({
                            companyId: globalCompany.getId(),
                            licensePlate: vm.vehicule.licenseplate
                        }, alreadyExist, allClearUpdate)


                    } else {
                        Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
                    }

                } else {
                    Vehicule.getByCompanyAndPlate({
                        companyId: globalCompany.getId(),
                        licensePlate: vm.vehicule.licenseplate
                    }, alreadyExist, allClearInsert)


                }
            }

            function allClearInsert() {
                Modal.showLoadingBar();
                insertVehicule();
            }

            function insertVehicule() {
                console.log(vm.vehicule)
                Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
            }

            function alreadyExist(data) {
                Modal.toast("La placa ingresada ya existe");
            }

            function allClearUpdate() {
                Modal.showLoadingBar();
                updateVehicule();
            }

            function updateVehicule() {
                Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
            }

            function onSaveSuccess(result) {
                WSVehicle.sendActivity(result);
                $state.go('vehiculeByHouse');
                Modal.hideLoadingBar();
                Modal.toast("Se ha registrado el vehículo correctamente");
                vm.isSaving = false;
            }

            function onEditSuccess(result) {
                WSVehicle.sendActivity(result);
                $state.go('vehiculeByHouse');
                Modal.hideLoadingBar();
                Modal.toast("Se ha editado el vehículo correctamente.");
                vm.isSaving = false;
            }

            function onSaveError() {
                Modal.toast("Ocurrió un error insperado.");
                Modal.hideLoadingBar();
                vm.isSaving = false;
            }

        }
    }
})();
