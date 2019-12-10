(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeResidentsController', LoginCodeResidentsController);

    LoginCodeResidentsController.$inject = ['Modal', 'Resident', 'WSResident', '$localStorage', '$scope', '$rootScope', '$state', 'Principal', '$timeout', 'Auth', 'MultiCompany', 'House', 'Company', 'Brand', 'CommonMethods', "PadronElectoral"];

    function LoginCodeResidentsController(Modal, Resident, WSResident, $localStorage, $scope, $rootScope, $state, Principal, $timeout, Auth, MultiCompany, House, Company, Brand, CommonMethods, PadronElectoral) {
        var vm = this;
        angular.element(document).ready(function () {

            $("#loginCodeResidentsPanel").fadeIn(1000);
        });
        vm.residents = [];
        vm.required = 1;
        vm.required2 = 1;
        vm.propietarioResidente = "Propietario residente";
        vm.countSaved = 0;
        vm.ownerGotten = false;
        vm.residentsEmpty = false;
        vm.codeStatus = $localStorage.codeStatus;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        $("#residentli").addClass("active");
        $("#profileli").removeClass("active");
        $("#homeli").removeClass("active");
        $("#carli").removeClass("active");
        $("#donerli").removeClass("active");

        loadHouse();
        vm.addResidentToList = function () {
            vm.arrayIsEmpty = false;
            var resident = {
                id: null,
                name: null,
                lastname: null,
                secondlastame: null,
                phonenumber: null,
                identificationnumber: null,
                email: null,
                isOwner: 0,
                enabled: 1,
                nacionality: "9",
                found: 0,
                companyId: $localStorage.companyId,
                houseId: $localStorage.house.id,
                validIdentification: 1,
                validPlateNumber: 1,
                lockNames: true,
                principalContact: false,
                deleted: 0
            };
            if (vm.house.houseForRent) {
                resident.type = 4;
            } else {
                resident.type = 3;
            }
            vm.residents.push(resident);

        };

        function loadHouse() {
            var id = CommonMethods.decryptIdUrl($state.params.loginCode)
            House.getByLoginCode({
                loginCode: id
            }).$promise.then(onSuccessHouse);

        }

        function onSuccessHouse(data) {
            vm.house = data;
            console.log("fadfad")
            console.log(vm.house)
            if (!vm.house.houseForRent) {
                Resident.getOwners({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    companyId: vm.house.companyId,
                    name: " ",
                    houseId: vm.house.id
                }, onSuccessOwners, onError);
            }else{
                getResidents();
            }
        }


        function onSuccessOwners(data, headers) {
            if (data.length > 0) {
                vm.noPeople = false;
                angular.forEach(data, function (owner, key) {
                    if (owner.type == 1) {
                        vm.ownerGotten = true;
                        vm.blockContactPrincipal = true;
                        vm.residents.push(owner)
                    }

                });

            } else {
                vm.noPeople = true;

            }

            getResidents();
        }

        function getResidents() {

            Resident.getResidents({
                page: vm.page,
                size: vm.itemsPerPage,
                companyId: vm.house.companyId,
                name: " ",
                houseId: vm.house.id,
                owner: "empty",
                enabled: 1
            }, onSuccessResidents, onError);

        }

        function onError(error) {
            Modal.toast("Un error inesperado sucedió.");
        }

        function onSuccessResidents(data, headers) {

            if (data.length > 0) {
                vm.noPeople = false;
                angular.forEach(data, function (resident, key) {
                    if (resident.houseId == vm.house.id && resident.type !== 1) {
                        if (vm.ownerGotten) {
                            vm.blockContactPrincipal = true;
                        } else {
                            vm.blockContactPrincipal = false;
                        }

                        if (resident.principalContact == 1) {
                            resident.principalContact = true;
                        } else if (resident.principalContact == 0) {
                            resident.principalContact = false;
                        }
                        vm.residents.push(resident)
                    }


                });
            } else {
                vm.noPeople = true;

            }

            if (vm.noPeople) {
                vm.addResidentToList();

            }


        }

        vm.deleteResidentFromList = function (index) {
            Modal.confirmDialog("¿Desea eliminar esta persona autorizada?", "", function () {
                if (vm.residents[index].id !== null) {
                    vm.residents[index].deleted = 1;
                    vm.residents[index].principalContact = false;

                } else {
                    vm.residents.splice(index, 1)
                }

            });

        };
        vm.residentsInfoReady = function () {
            vm.countResidents = 0;
            if (vm.validArray() == true) {
                residentsConfirmation()
            }

        };
        vm.checkPrincipalContact = function (index) {
            angular.forEach(vm.residents, function (resident, key) {
                resident.principalContact = false;
            });
            vm.residents[index].principalContact = true;
        };


        function residentsConfirmation() {
            Modal.confirmDialog("¿Desea confirmar el registro de esta información?", "", function () {
                $rootScope.showLoadingBar();
                vm.principalContactDetected = 0;
                angular.forEach(vm.residents, function (resident, i) {

                    if (resident.principalContact || resident.principalContact == 1) {
                        vm.principalContactDetected++;
                    }
                    if (resident.id !== null) {
                        vm.countResidents++
                    } else {
                        validateIdNumber(resident)

                    }


                });
                setTimeout(function () {

                    if (vm.countResidents == vm.residents.length) {
                        if (vm.principalContactDetected > 0) {
                            insertResident();
                        } else {
                            $rootScope.hideLoadingBar();
                            Modal.toast("Debe seleccionar un contacto principal");
                            vm.isSaving = false;
                        }
                    }

                }, 1000);


            });

        }


        function validateIdNumber(val) {
            Resident.getByCompanyAndIdentification({
                companyId: vm.house.companyId,
                identificationID: val.identificationnumber
            }, alreadyExist, function () {
                vm.countResidents++
            });


        }

        function alreadyExist() {
            $rootScope.hideLoadingBar();
            Modal.toast("La cédula ingresada ya existe.");
            vm.isSaving = false;
        }


        function insertResident() {

            angular.forEach(vm.residents, function (resident, i) {

                vm.isSaving = true;

                if (resident.principalContact == true) {
                    resident.principalContact = 1;
                    $localStorage.residentPrincipal = resident;
                } else if (resident.principalContact == false) {
                    resident.principalContact = 0;
                }

                if (resident.id == null) {

                    Resident.save(resident, onSaveSuccessInsertUpdate, onSaveError);
                } else {
                    if (resident.type == 1) {
                        $localStorage.residentPrincipal = resident;
                        resident.principalContact = 1;
                    }
                    if (resident.deleted == 1) {
                        Resident.delete(resident, onSaveSuccessInsertUpdate, onSaveError);
                    } else {
                        Resident.update(resident, onSaveSuccessInsertUpdate, onSaveError);
                    }
                }


            })


        }

        function onSaveSuccessInsertUpdate(result) {

            WSResident.sendActivity(result);
            $scope.$emit('aditumApp:residentUpdate', result);
            vm.countSaved++;
            if (vm.countSaved === vm.residents.length) {
                vm.isSaving = false;
                if($localStorage.residentPrincipal.principalContact==1){
                    $localStorage.residentPrincipal.id = result.id;
                }
                if (vm.house.codeStatus == 1) {
                    vm.house.codeStatus = 2;
                    House.update(vm.house, function () {
                        $rootScope.hideLoadingBar();
                        $state.go('loginCodeCars');
                        $localStorage.residents = vm.residents;
                    });
                } else {
                    $rootScope.hideLoadingBar();
                    $localStorage.residents = vm.residents;
                    $state.go('loginCodeCars');
                }
            }
        }


        function onSaveError() {
            vm.isSaving = false;
        }

        vm.unlockPersonNames = function (person) {
            if (person.nacionality == "15") {
                person.lockNames = false;
            } else {
                person.lockNames = true;
            }

        }

        vm.findInPadron = function (person) {
            $localStorage.residentsLoginCode = vm.residents;
            if (person == undefined && person.nacionality == "9") {

                $scope.$apply(function () {
                    person.lockNames = true;
                    person.name = "";
                    person.lastname = "";
                    person.secondlastname = "";
                })
            } else {

                if (hasCaracterEspecial(person.identificationnumber) || haswhiteCedula(person.identificationnumber)) {
                    person.validIdentification = 0;
                } else {
                    person.validIdentification = 1;
                }
                if (person.nacionality == "9" && person.identificationnumber != undefined) {
                    if (person.identificationnumber.trim().length == 9) {
                        PadronElectoral.find(person.identificationnumber, function (info) {
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    var nombre = info.nombre.split(",");
                                    person.name = nombre[0];
                                    person.lastname = nombre[1];
                                    person.secondlastname = nombre[2];
                                    person.found = 1;
                                })
                            }, 100)
                        }, function () {
                            $scope.$apply(function () {
                                person.lockNames = false;

                            });
                            Modal.toast("No se han encontrado datos en el padrón electoral, por favor ingresarlos manualmente.");
                        })


                    } else {
                        setTimeout(function () {
                            $scope.$apply(function () {
                                person.lockNames = true;
                                person.name = "";
                                person.lastname = "";
                                person.secondlastname = "";
                            })
                        }, 100)
                    }
                } else {
                    person.found = 0;
                }
            }
        }

        function haswhiteCedula(s) {
            return /\s/g.test(s);
        }

        vm.hasNumbersOrSpecial = function (s) {
            var caracteres = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase()) {
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
        vm.hasLettersOrSpecial = function (s) {
            var caracteres = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "´ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase()) {
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

        function hasCaracterEspecial(s) {
            var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#"]
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

        vm.validate = function (person) {
            var invalido = 0;
            var invalidCed = false;
            var invalidCedLength = false;
            if (hasCaracterEspecial(person.identificationnumber || haswhiteCedula(person.identificationnumber))) {
                person.validIdentification = 0;
                invalidCed = true;
            } else if (person.nacionality == "9" && person.identificationnumber.length < 9) {

                person.validIdentification = 0;
                invalidCedLength = true;
            } else {
                person.identificationnumber = person.identificationnumber.replace(/\s/g, '')
                person.validIdentification = 1;
            }

            if (person.name == undefined || person.lastname == undefined || person.secondlastname == undefined || person.name == "" || person.lastname == "" || person.secondlastname == "") {
                invalido++;
            } else if (hasCaracterEspecial(person.name) || hasCaracterEspecial(person.lastname) || hasCaracterEspecial(person.secondlastname)) {
                invalido++;
            } else if (person.identificationnumber != undefined) {

            }
            return {errorNombreInvalido: invalido, errorCedula: invalidCed, errorCedulaCorta: invalidCedLength}
        }

        vm.validArray = function () {
            var nombreError = 0;
            var errorCedula = 0;
            var errorCedLenght = 0;

            angular.forEach(vm.residents, function (resident, i) {
                resident.name = resident.name.toUpperCase()
                resident.lastname = resident.lastname.toUpperCase()
                resident.secondlastname = resident.secondlastname.toUpperCase()

                var residentValidation = vm.validate(resident)
                if (residentValidation.errorCedula) {
                    errorCedula++;
                }
                if (residentValidation.errorNombreInvalido > 0) {
                    nombreError++;
                }
                if (residentValidation.errorCedulaCorta) {
                    errorCedLenght++;
                }
            })


            if (errorCedula > 0) {
                $rootScope.hideLoadingBar();
                Modal.toast("No puede ingresar ningún caracter especial o espacio en blanco en la cédula.");

            }
            if (nombreError > 0) {
                $rootScope.hideLoadingBar();
                Modal.toast("No puede ingresar ningún caracter especial en el nombre.");

            }
            if (errorCedLenght > 0) {
                $rootScope.hideLoadingBar();
                Modal.toast("Si la nacionalidad es costarricense, debe ingresar el número de cédula igual que aparece en la cédula de identidad para obtener la información del padrón electoral de Costa Rica. Ejemplo: 10110111.");
            }


            if (errorCedula == 0 && nombreError == 0 && errorCedLenght == 0) {
                return true;
            } else {
                return false;
            }


        }


    }
})();
