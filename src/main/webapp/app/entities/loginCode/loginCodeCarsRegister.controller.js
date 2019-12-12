(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeCarsRegisterController', LoginCodeCarsRegisterController);

    LoginCodeCarsRegisterController.$inject = ['Modal','WSVehicle', 'Vehicule', '$localStorage', '$scope', '$rootScope', '$state', 'Principal', '$timeout', 'Auth', 'MultiCompany', 'House', 'Company', 'Brand', 'CommonMethods', "PadronElectoral"];

    function LoginCodeCarsRegisterController(Modal,WSVehicle, Vehicule, $localStorage, $scope, $rootScope, $state, Principal, $timeout, Auth, MultiCompany, House, Company, Brand, CommonMethods, PadronElectoral) {
        angular.element(document).ready(function () {

            $("#loginCodeVehiculesPanel").fadeIn(1000);
        });
        var vm = this;
        vm.showKeys = function () {
            Modal.customDialog("<md-dialog>" +
                "<md-dialog-content class='md-dialog-content text-center'>" +
                "<h1 class='md-title'>Número de soporte </h1>" +
                "<div class='md-dialog-content-body'>" +
                "<p>En caso de necesitar ayuda o el sistema le presenta un problema, favor comunicarse al <b>8624-5504</b> o <b>6002-3372</b></p>" +

                "</div>" +
                "</md-dialog-content>" +
                "</md-dialog>")
        };
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicules = [];
        vm.required = 1;
        vm.required2 = 1;
        vm.countSaved = 0;
        vm.countVehicules = 0;
        vm.codeStatus = $localStorage.codeStatus;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        $("#carli").addClass("active");
        $("#profileli").removeClass("active");
        $("#residentli").removeClass("active");
        $("#homeli").removeClass("active");
        $("#donerli").removeClass("active");

        loadHouse();

        function loadHouse() {
            var id = CommonMethods.decryptIdUrl($state.params.loginCode);
            House.getByLoginCode({
                loginCode: id
            }).$promise.then(onSuccessHouse);

        }

        function onSuccessHouse(data) {
            vm.house = data;
            Vehicule.findVehiculesEnabledByHouseId({
                houseId: vm.house.id
            }).$promise.then(onSuccessVehicules, onError);

        }

        function onError(error) {
        }

        function onSuccessVehicules(data) {
            if (data.length > 0) {
                angular.forEach(data, function (vehicule, key) {
                    vm.vehicules.push(vehicule);
                });
            } else {
                vm.addVehiculeToList();
            }
        }

        Brand.query({}, onSuccessBrand);

        function onSuccessBrand(brands) {
            vm.brands = brands;


        }

        vm.addVehiculeToList = function () {
            var vehicule = {
                licenseplate: null,
                brand: null,
                color: "#ffff",
                enabled: 1,
                type: null,
                companyId: $localStorage.companyId,
                houseId: $localStorage.house.id,
                deleted: 0,
                house: vm.house
            };

            vm.vehicules.push(vehicule);
        };


        vm.saveInfo = function (vehicule) {
            vm.validate(vehicule);
        };
        vm.deleteVehiculeFromList = function (index) {
            Modal.confirmDialog("¿Desea eliminar este vehículo?", "", function () {
                vm.vehicules.splice(index, 1)

            });
        };
        vm.submitColor = function (vehicule, index) {
            setTimeout(function () {
                $scope.$apply(function () {
                    vehicule.color = $('#' + index).css('background-color');
                    $('#' + index + 'btn').css('background-color', vehicule.color);
                })
            }, 100)
        };


        vm.vehiculesInfoReady = function () {
            vm.countVehicules = 0;
            if (vm.vehicules.length == 0 || vm.vehicules.length == 1 && vm.vehicules[0].licenseplate == "" || vm.vehicules.length == 1 && vm.vehicules[0].licenseplate == undefined || vm.vehicules.length == 1 && vm.vehicules[0].licenseplate == null) {
                noVehiculesConfirmation()

            } else {

                if (vm.validArray() == true) {
                    vehiculesConfirmation();

                }
            }
        }

        function noVehiculesConfirmation() {

            Modal.confirmDialog("¿No se registró ningun vehículo autorizado, desea continuar de igual forma?", "", function () {

                if (vm.house.codeStatus == 2) {
                    vm.house.codeStatus = 3;
                    House.update(vm.house,function () {
                        $state.go('loginCodeResume');
                        $rootScope.hideLoadingBar();
                        $localStorage.vehicules = vm.vehicules;
                    });
                }else{
                    $state.go('loginCodeResume');
                    $rootScope.hideLoadingBar();
                    $localStorage.vehicules = vm.vehicules;
                }
            });


        }




        function vehiculesConfirmation() {

            Modal.confirmDialog("¿Desea confirmar el registro de esta información?", "", function () {
                $rootScope.showLoadingBar();
                angular.forEach(vm.vehicules, function (vehicule, i) {
                    vehicule.licenseplate = vehicule.licenseplate.toUpperCase();
                    if (vehicule.id !== null) {
                        vm.countVehicules++
                    } else {
                        validateIdNumber(resident)
                    }
                });
                setTimeout(function () {
                    if (vm.countVehicules == vm.vehicules.length) {
                        insertVehiculeCount();
                    }

                }, 1000);


            });


        }

        function insertVehiculeCount() {

            angular.forEach(vm.vehicules, function (vehicule, i) {
                vm.isSaving = true;
                if (vehicule.id == null) {
                    Vehicule.save(vehicule, onSaveSuccess, onSaveError);
                } else {
                    Vehicule.update(vehicule, onSaveSuccess, onSaveError);
                }


            });

        }
        function validateIdNumber(val) {
            Vehicule.getByCompanyAndPlate({
                companyId: vm.house.companyId,
                licensePlate: val.licenseplate
            }, alreadyExist, function () {
                vm.countVehicules++;
            });


        }

        function alreadyExist() {
            $rootScope.hideLoadingBar();
            Modal.toast("La placa ingresada ya existe.");
            vm.isSaving = false;
        }


        function onSaveSuccess(result) {
            WSVehicle.sendActivity(result);
            $scope.$emit('aditumApp:vehiculeUpdate', result);
            vm.countSaved++;
            if (vm.countSaved == vm.vehicules.length) {
                vm.isSaving = false;
                if (vm.house.codeStatus == 2) {
                    vm.house.codeStatus = 3;
                    House.update(vm.house,function () {
                        $localStorage.vehicules = vm.vehicules;
                        $state.go('loginCodeResume');
                        $rootScope.hideLoadingBar();
                    });
                }else{
                    $localStorage.vehicules = vm.vehicules;
                    $state.go('loginCodeResume');
                    $rootScope.hideLoadingBar();
                }

            }


        }



        function onSaveError() {
            vm.isSaving = false;
        }

        function haswhiteCedula(s) {
            return /\s/g.test(s);
        }


        vm.validate = function (car) {

            var invalidLic = false;
            if (hasCaracterEspecial(car.licenseplate || haswhiteCedula(car.licenseplate))) {
                car.validLicense = 0;
                invalidLic = true;
            } else {
                car.licenseplate = car.licenseplate.replace(/\s/g, '')
                car.validLicense = 1;
            }

            return {errorPlaca: invalidLic}
        }

        function hasCaracterEspecial(s) {
            var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!"]
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

        vm.validArray = function () {
            var errorPlaca = 0;

            angular.forEach(vm.vehicules, function (car, i) {
                var carValidation = vm.validate(car)
                if (carValidation.errorPlaca) {
                    errorPlaca++;
                }
            })


            if (errorPlaca > 0) {
                Modal.toast("No puede ingresar ningún caracter especial o espacio en blanco en la placa..");

            }


            if (errorPlaca == 0) {
                return true;
            } else {
                return false;
            }


        }

    }
})();
