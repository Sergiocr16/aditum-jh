(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseDialogController', ResidentByHouseDialogController);

    ResidentByHouseDialogController.$inject = ['$state', '$timeout', '$scope', '$rootScope', '$stateParams', 'CommonMethods', 'previousState', 'DataUtils', '$q', 'entity', 'Resident', 'User', 'Company', 'House', 'Principal', 'companyUser', 'WSResident', 'SaveImageCloudinary', 'PadronElectoral', 'globalCompany', 'Modal'];

    function ResidentByHouseDialogController($state, $timeout, $scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q, entity, Resident, User, Company, House, Principal, companyUser, WSResident, SaveImageCloudinary, PadronElectoral, globalCompany, Modal) {
        $rootScope.active = "residentsHouses";
        var vm = this;
        var fileImage = null;
        $rootScope.mainTitle = vm.title;
        vm.isAuthenticated = Principal.isAuthenticated;
        if (entity.image_url == undefined) {
            entity.image_url = null;
        }
        vm.resident = entity;
        vm.resident.houseId = 0;
        vm.resident.nationality = "9";
        vm.resident.principalContact = vm.resident.principalContact + "";

        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.user = entity;
        vm.temporalIndentification = vm.resident.identificationnumber;
        vm.success = null;
        vm.loginStringCount = 0;
        vm.SaveUserError = false;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        Principal.identity().then(function (account) {
            vm.account = account;
        });

        if (vm.resident.id !== null) {
            vm.title = "Editar usuario";
            vm.button = "Editar";
            var autorizadorStatus = vm.resident.isOwner;
            if (vm.resident.isOwner == 1) {
                vm.resident.isOwner = true;
            }
            vm.resident.type = vm.resident.type + "";
        } else {

            vm.title = "Registrar usuario";
            vm.button = "Registrar";
        }
        $rootScope.mainTitle = vm.title;
        House.query({
            companyId: globalCompany.getId()
        }).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
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
                var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿"]
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

            if (vm.resident.name == undefined || vm.resident.lastname == undefined || vm.resident.secondlastname == undefined || hasWhiteSpace(vm.resident.identificationnumber)) {
                Modal.toast("No puede ingresar espacios en blanco.");
                invalido++;
            } else if (hasCaracterEspecial(vm.resident.name) || hasCaracterEspecial(vm.resident.lastname) || hasCaracterEspecial(vm.resident.secondlastname) || hasCaracterEspecial(vm.resident.identificationnumber)) {
                invalido++;
                Modal.toast("No puede ingresar ningún caracter especial.");
            }
            if (invalido == 0) {
                return true;
            } else {
                return false;
            }
        }

        function haswhiteCedula(s) {
            return /\s/g.test(s);
        }

        vm.findInPadron = function (resident) {

            if (resident.identificationnumber != undefined || resident.identificationnumber != "") {
                if (hasCaracterEspecial(resident.identificationnumber) || haswhiteCedula(resident.identificationnumber) || resident.nationality == "9" && hasLetter(resident.identificationnumber)) {
                    resident.validIdentification = 0;
                } else {
                    resident.validIdentification = 1;
                }

                if (resident.nationality == "9" && resident.identificationnumber != undefined) {
                    if (resident.identificationnumber.trim().length == 9) {
                        PadronElectoral.find(resident.identificationnumber, function (person) {
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    var nombre = person.nombre.split(",");
                                    resident.name = nombre[0];
                                    resident.lastname = nombre[1];
                                    resident.secondlastname = nombre[2];
                                    resident.found = 1;
                                })
                            }, 100)
                        }, function () {

                        })


                    } else {
                        setTimeout(function () {
                            $scope.$apply(function () {
                                resident.found = 0;
                            })
                        }, 100)
                    }
                } else {
                    resident.found = 0;
                }
            }
        };

        function hasLetter(s) {
            var caracteres = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]
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


        function save() {
            var wordOnModal = vm.resident.id == undefined ? "registrar" : "modificar"
            if (vm.validate()) {
                Modal.confirmDialog("¿Está seguro que desea " + wordOnModal + " el usuario?", "", function () {
                    vm.isSaving = true;
                    vm.resident.name = CommonMethods.capitalizeFirstLetter(vm.resident.name);
                    vm.resident.lastname = CommonMethods.capitalizeFirstLetter(vm.resident.lastname);
                    vm.resident.secondlastname = CommonMethods.capitalizeFirstLetter(vm.resident.secondlastname);
                    if (vm.resident.id !== null) {
                        if (vm.temporalIndentification !== vm.resident.identificationnumber) {
                            Resident.getByCompanyAndIdentification({
                                companyId: globalCompany.getId(),
                                identificationID: vm.resident.identificationnumber
                            }, alreadyExist, allClearUpdate)

                            function alreadyExist(data) {
                                bootbox.hideAll();
                                Modal.toast("La cédula ingresada ya existe.");
                            }

                            function allClear(data) {
                                CommonMethods.waitingMessage();
                                console.log(vm.resident.isOwner)
                                if (vm.resident.isOwner == true) {
                                    vm.resident.isOwner = 1;
                                } else {
                                    vm.resident.isOwner = 0;
                                }

                                if (fileImage !== null) {

                                    vm.imageUser = {
                                        user: vm.resident.id
                                    };
                                    SaveImageCloudinary
                                        .save(fileImage, vm.imageUser)
                                        .then(onSaveImageSuccess, onSaveError, onNotify);

                                    function onNotify(info) {
                                        vm.progress = Math.round((info.loaded / info.total) * 100);
                                    }

                                    function onSaveImageSuccess(data) {
                                        vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                                        if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                        }
                                        Resident.update(vm.resident, onSuccess, onSaveError);
                                    }
                                } else {
                                    if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                    }
                                    Resident.update(vm.resident, onSuccess, onSaveError);
                                }
                            }
                        } else {
                            console.log()
                            if (vm.resident.isOwner == true) {
                                vm.resident.isOwner = 1;
                            } else {
                                vm.resident.isOwner = 0;
                            }

                            if (fileImage !== null) {
                                vm.imageUser = {
                                    user: vm.resident.id
                                };
                                SaveImageCloudinary
                                    .save(fileImage, vm.imageUser)
                                    .then(onSaveImageSuccess, onSaveError, onNotify);

                                function onNotify(info) {
                                    vm.progress = Math.round((info.loaded / info.total) * 100);
                                }

                                function onSaveImageSuccess(data) {
                                    vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                                    if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                    }
                                    Resident.update(vm.resident, onSuccess, onSaveError);
                                }
                            } else {
                                if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                                    vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                }
                                Resident.update(vm.resident, onSuccess, onSaveError);
                            }
                        }

                    } else {
                        Resident.getByCompanyAndIdentification({
                            companyId: globalCompany.getId(),
                            identificationID: vm.resident.identificationnumber
                        }, alreadyExist, allClearInsert)

                        function alreadyExist(data) {
                            bootbox.hideAll();
                            Modal.toast("La cédula ingresada ya existe.");
                        }


                    }
                })
            }

            function allClearInsert() {
                CommonMethods.waitingMessage();
                vm.resident.enabled = 1;
                vm.resident.isOwner = 0;
                vm.resident.companyId = globalCompany.getId();
                vm.resident.houseId = $rootScope.companyUser.houseId
                if (fileImage !== null) {
                    vm.imageUser = {
                        user: vm.resident.id
                    };
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify);

                    function onNotify(info) {
                        vm.progress = Math.round((info.loaded / info.total) * 100);
                    }

                    function onSaveImageSuccess(data) {
                        vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                        if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                        }
                        Resident.save(vm.resident, onSuccess, onSaveError);
                    }
                } else {
                    if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                    }
                    Resident.save(vm.resident, onSuccess, onSaveError);
                }
            }

            function allClearUpdate() {
                modificar();
            }

            function modificar() {
                CommonMethods.waitingMessage();

                if (vm.resident.isOwner == true) {
                    vm.resident.isOwner = 1;
                } else {
                    vm.resident.isOwner = 0;
                }
                if (fileImage !== null) {

                    vm.imageUser = {
                        user: vm.resident.id
                    };
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify);

                    function onNotify(info) {
                        vm.progress = Math.round((info.loaded / info.total) * 100);
                    }

                    function onSaveImageSuccess(data) {
                        vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                        if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                        }
                        Resident.update(vm.resident, onSuccess, onSaveError);
                    }
                } else {
                    if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                    }
                    Resident.update(vm.resident, onSuccess, onSaveError);
                }

            }

            function onSuccess(result) {
                WSResident.sendActivity(result);
                if ($rootScope.companyUser.id === result.id) {
                    $rootScope.companyUser = result;

                }
                vm.isSaving = false;
                $state.go('residentByHouse', null, {
                    reload: true
                });
                bootbox.hideAll();
                if (vm.resident.id !== null) {
                    Modal.toast("Se ha editado el usuario correctamente.");
                } else {
                    Modal.toast("Se ha registrado el usuario correctamente.");
                }

            }

            function onSaveError() {
                vm.isSaving = false;
            }
        }

        vm.setImage = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        vm.displayImage = base64Data;
                        vm.displayImageType = $file.type;
                    });
                });
                fileImage = $file;
            }
        };
    }
})();
