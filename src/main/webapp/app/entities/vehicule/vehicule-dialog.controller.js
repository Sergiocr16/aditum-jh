(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDialogController', VehiculeDialogController);

    VehiculeDialogController.$inject = ['$state', 'CommonMethods', '$rootScope', 'Principal', '$timeout', '$scope', '$stateParams', 'entity', 'Vehicule', 'House', 'Company', 'WSVehicle', 'Brand', 'globalCompany', 'Modal'];

    function VehiculeDialogController($state, CommonMethods, $rootScope, Principal, $timeout, $scope, $stateParams, entity, Vehicule, House, Company, WSVehicle, Brand, globalCompany, Modal) {
        $rootScope.active = "vehicules";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;

        if (vm.vehicule.id == undefined) {
            vm.button = "Registrar";
            vm.title = "Registrar vehículo";

        } else {
            vm.button = "Editar";
            vm.title = "Editar vehículo";
        }
        vm.options = {
            label: "",
            icon: "",
            default: "#ffffff",
            openOnInput: true
        };
        vm.isReady = false;
        $rootScope.mainTitle = vm.title;
        vm.myPlate = vm.vehicule.licenseplate;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.required = 1;
        vm.required2 = 1;
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();

        angular.element(document).ready(function () {
            ColorPicker.init();
        });
        Brand.query({}, onSuccessBrand);

        function onSuccessBrand(brands) {
            vm.brands = brands;


        }

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            angular.forEach(data, function (value, key) {
                value.housenumber = parseInt(value.housenumber);
                if (value.housenumber == 9999) {
                    value.housenumber = "Oficina"
                }
            })
            vm.houses = data;
            vm.isReady = true;

        }


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
                var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "{", "}"]
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
                Modal.toast("No puede ingresar la placa con espacios en blanco.");

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

        function save() {
            var wordOnModal = vm.vehicule.id == undefined ? "registrar" : "modificar"
            if (vm.validate()) {
                Modal.confirmDialog("¿Está seguro que desea " + wordOnModal + " el vehículo?", "", function () {
                    vm.vehicule.enabled = 1;
                    vm.vehicule.companyId = globalCompany.getId();
                    vm.vehicule.licenseplate = vm.vehicule.licenseplate.toUpperCase();
                    if (vm.vehicule.color == null) {
                        vm.vehicule.color = "#ffffff"
                    }
                    if (vm.vehicule.id !== null) {
                        if (vm.myPlate !== vm.vehicule.licenseplate) {
                            Vehicule.getByCompanyAndPlate({
                                companyId: globalCompany.getId(),
                                licensePlate: vm.vehicule.licenseplate
                            }, alreadyExist, allClearUpdate)

                            function alreadyExist(data) {
                                Modal.toast("La placa ingresada ya existe.");
                            }
                        } else {
                            Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
                        }
                    } else {
                        Vehicule.getByCompanyAndPlate({
                            companyId: globalCompany.getId(),
                            licensePlate: vm.vehicule.licenseplate
                        }, alreadyExist, allClearInsert)

                        function alreadyExist(data) {
                            Modal.toast("La placa ingresada ya existe.");
                        }

                        function allClearInsert() {
                            Modal.showLoadingBar();
                            insertVehicule();
                        }

                        function insertVehicule() {
                            console.log(vm.vehicule.type);
                            Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
                        }
                    }
                })
            }

            function allClearUpdate(data) {
                Modal.showLoadingBar();
                updateVehicule();

            }

            function updateVehicule() {
                Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
            }

            function onSaveSuccess(result) {
                WSVehicle.sendActivity(result);
                $state.go('vehicule');
                Modal.hideLoadingBar();
                Modal.toast("Se ha registrado el vehículo correctamente.");

                vm.isSaving = false;
            }

            function onEditSuccess(result) {
                WSVehicle.sendActivity(result);
                $state.go('vehicule');
                Modal.hideLoadingBar();
                Modal.toast("Se ha editado el vehículo correctamente.");

                vm.isSaving = false;
            }

            function onSaveError() {
                vm.isSaving = false;
            }

        }
    }
})();
