(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDialogController', VehiculeDialogController);

    VehiculeDialogController.$inject = ['$localStorage','$state', 'CommonMethods', '$rootScope', 'Principal', '$timeout', '$scope', '$stateParams', 'entity', 'Vehicule', 'House', 'Company', 'WSVehicle', 'Brand', 'globalCompany', 'Modal'];

    function VehiculeDialogController($localStorage,$state, CommonMethods, $rootScope, Principal, $timeout, $scope, $stateParams, entity, Vehicule, House, Company, WSVehicle, Brand, globalCompany, Modal) {
        $rootScope.active = "vehicules";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;
        vm.titleHouse = "";

        vm.options = {
            label: "",
            icon: "",
            default: "#ffffff",
            openOnInput: true
        };


        vm.isReady = false;
        vm.myPlate = vm.vehicule.licenseplate;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.required = 1;
        vm.required2 = 1;

        angular.element(document).ready(function () {
            ColorPicker.init();

        });
        Brand.query({}, onSuccessBrand);

        function onSuccessBrand(brands) {
            vm.brands = brands;


        }

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        vm.changeHouse = function(houseId){
            angular.forEach(vm.houses, function (value, key) {
                if(value.id == houseId){
                    vm.titleHouse = " filial " + value.housenumber;
                    $localStorage.infoHouseNumber = value;
                    console.log(vm.title)
                    console.log(vm.titleHouse)
                    $rootScope.mainTitle = vm.title + vm.titleHouse;
                }
            });

        };
        function onSuccessHouses(data, headers) {

            vm.houses = data;
            if (vm.vehicule.id !== null) {
                angular.forEach(vm.houses, function (value, key) {
                    if(value.id == vm.vehicule.houseId){
                        vm.titleHouse = " filial " + value.housenumber;

                    }
                });
                vm.title = "Editar vehículo";
                vm.button = "Editar";
            }else{
                if($localStorage.infoHouseNumber!==undefined){
                    vm.vehicule.houseId = $localStorage.infoHouseNumber.id;
                    vm.titleHouse = " filial " + $localStorage.infoHouseNumber.housenumber;
                }
                vm.title = "Registrar vehículo";
                vm.button = "Registrar";


            }
            $rootScope.mainTitle = vm.title + vm.titleHouse;
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
                var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "{", "}", "[", "]", "''"];
                var invalido = 0;
                angular.forEach(caracteres, function (val, index) {
                    if (s !== undefined) {
                        for (var i = 0; i < s.length; i++) {
                            if (s.charAt(i) === val) {
                                invalido++;
                            }
                        }
                    }
                });
                if (invalido === 0) {
                    return false;
                } else {
                    return true;
                }
            }

            if (vm.vehicule.licenseplate === undefined || hasWhiteSpace(vm.vehicule.licenseplate)) {
                Modal.toast("No puede ingresar la placa con espacios en blanco.");

                invalido++;
            } else if (hasCaracterEspecial(vm.vehicule.licenseplate)) {
                invalido++;
                Modal.toast("No puede ingresar la placa con guiones o cualquier otro carácter especial");
            }
            if (invalido === 0) {
                return true;
            } else {
                return false;
            }
        };

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

        function save() {
            console.log(vm.vehicule.id)
            var wordOnModal = vm.vehicule.id === null ? "registrar" : "modificar"
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


                        } else {
                            Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
                        }
                    } else {
                        Vehicule.getByCompanyAndPlate({
                            companyId: globalCompany.getId(),
                            licensePlate: vm.vehicule.licenseplate
                        }, alreadyExist, allClearInsert)


                    }
                })
            }

            function allClearInsert() {
                Modal.showLoadingBar();
                insertVehicule();
            }

            function insertVehicule() {
                console.log(vm.vehicule);
                Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
            }

            function alreadyExist(data) {
                Modal.toast("La placa ingresada ya existe.");
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
                Modal.toast("Ocurrió un error insperado.");
                Modal.hideLoadingBar();
                vm.isSaving = false;
            }

        }
    }
})();
