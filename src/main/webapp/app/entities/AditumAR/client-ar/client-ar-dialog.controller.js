(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('ClientARDialogController', ClientARDialogController);

        ClientARDialogController.$inject = ['$localStorage', '$state', '$timeout', '$scope', '$rootScope', '$stateParams', 'CommonMethods', 'previousState', 'DataUtils', '$q', 'entity', 'Resident', 'User', 'Company', 'House', 'Principal', 'WSResident', 'SaveImageCloudinary', 'PadronElectoral', 'Modal', 'globalCompany'];

        function ClientARDialogController($localStorage, $state, $timeout, $scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q, entity, Resident, User, Company, House, Principal, WSResident, SaveImageCloudinary, PadronElectoral, Modal, globalCompany) {
            $rootScope.active = "owner";
            var vm = this;
            vm.isReady = false;
            var fileImage = null;
            vm.isAuthenticated = Principal.isAuthenticated;
            vm.resident = entity;
            vm.resident.isOwner = true;
            vm.resident.type = 1;
            var housesResident = vm.resident.houses;
            vm.resident.principalContact = vm.resident.principalContact + "";
            if (entity.image_url == undefined) {
                entity.image_url = null;
            }
            vm.clearSearchTerm = function () {
                vm.searchTerm = '';
            };
            vm.searchTerm;
            vm.typingSearchTerm = function (ev) {
                ev.stopPropagation();
            }
            var autorizadorStatus = vm.resident.isOwner;
            vm.resident.nationality = "9";
            vm.required = 1;
            vm.previousState = previousState.name;
            vm.byteSize = DataUtils.byteSize;
            vm.openFile = DataUtils.openFile;
            vm.titleHouse = "";
            vm.save = save;
            vm.hasCaracterEspecial = hasCaracterEspecial;
            Modal.enteringForm(save);
            $scope.$on("$destroy", function () {
                Modal.leavingForm();
            });
            var indentification = vm.resident.identificationnumber;
            vm.user = entity;
            vm.success = null;
            vm.loginStringCount = 0;
            vm.SaveUserError = false;
            vm.resident.isCompany = vm.resident.isCompany ? 1 : 0;
            var wordOnModal;
            if (vm.resident.id !== null) {
                vm.title = "Editar usuario";
                vm.button = "Editar";
                wordOnModal = "modificar";
                vm.resident.type = vm.resident.type + "";
                if (vm.resident.isOwner == 1) {
                    vm.resident.isOwner = true;
                }
            } else {
                if ($localStorage.infoHouseNumber !== undefined) {
                    vm.resident.houseId = $localStorage.infoHouseNumber.id;
                    vm.titleHouse = " filial " + $localStorage.infoHouseNumber.housenumber;
                }
                wordOnModal = "registrar";
                vm.title = "Registrar usuario ";
                vm.button = "Registrar";
            }

            $rootScope.mainTitle = vm.title;

            House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
                $rootScope.mainTitle = vm.title;
                vm.isReady = true;
            }


            function save() {
                if (vm.resident.isCompany == 1) {
                    saving()
                } else {
                    if (vm.validate()) {
                        saving()
                    }
                }


            }

            function haswhiteCedula(s) {
                return /\s/g.test(s);
            }


            function saving() {

                Modal.confirmDialog("¿Está seguro que desea " + wordOnModal + " el propietario?", "", function () {
                    vm.resident.name = vm.resident.name ? vm.resident.name.toUpperCase() : vm.resident.name;
                    vm.resident.lastname = vm.resident.lastname ? vm.resident.lastname.toUpperCase() : vm.resident.lastname;
                    vm.resident.secondlastname = vm.resident.secondlastname ? vm.resident.secondlastname.toUpperCase() : vm.resident.secondlastname;
                    vm.isSaving = true;
                    vm.resident.isCompany = vm.resident.isCompany == 1 ? true : false;

                    if (vm.resident.id == null) {

                        Resident.getByCompanyAndIdentification({
                            companyId: globalCompany.getId(),
                            identificationID: vm.resident.identificationnumber
                        }, alreadyExist, allClearInsert)

                    } else {

                        if (indentification !== vm.resident.identificationnumber) {

                            Resident.getByCompanyAndIdentification({
                                companyId: globalCompany.getId(),
                                identificationID: vm.resident.identificationnumber
                            }, alreadyExist, updateResident)

                        } else {
                            updateResident();
                        }

                    }
                })
            }

            function allClearInsert() {
                Modal.showLoadingBar();

                changeStatusIsOwner();

                if (vm.resident.isOwner == 1) {

                    createAccount(1);

                } else {

                    insertResident(null);

                }

            }


            function insertResident(id) {

                vm.resident.enabled = 1;
                vm.resident.companyId = globalCompany.getId();
                vm.resident.userId = id;
                saveImageInsert(id);

            }

            function saveImageInsert(id) {

                vm.imageUser = {user: id};
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccessInsert, onSaveError, onNotify);
                } else {
                    if (vm.resident.identificationnumber !== undefined || vm.resident.identificationnumber != null) {
                        vm.resident.identificationnumber = vm.resident.identificationnumber ? vm.resident.identificationnumber.toUpperCase() : vm.resident.identificationnumber;
                    }
                    Resident.save(vm.resident, onSaveSuccess, onSaveError);
                }

            }


            function saveImageUpdate() {
                changeStatusIsOwner();
                vm.imageUser = {user: vm.resident.id};
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccessUpdate, onSaveError, onNotify);
                } else {
                    if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                    }
                    if (vm.resident.type == 2) {
                        vm.resident.houseId = null;
                    }
                    Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                }

            }

            function createAccount(opcion) {
                var authorities;

                if (vm.resident.type == 1) {
                    authorities = [ "ROLE_USER_AR"];
                }
                vm.opcion = opcion;
                vm.user.firstName = vm.resident.name;
                vm.user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
                vm.user.email = vm.resident.email;
                vm.user.activated = true;
                vm.user.authorities = authorities;
                vm.user.login = vm.user.email;

                console.log(vm.user)
                User.save(vm.user, onSaveUser, onSaveLoginError);
            }

            function onSaveUser(result) {
                vm.isSaving = false;
                if (vm.opcion === 1) {
                    insertResident(result.id)
                } else if (vm.opcion === 2) {
                    vm.resident.userId = result.id;
                    vm.resident.isOwner = 1;
                    saveImageUpdate();
                }

            }

            function updateResident() {
                changeStatusIsOwner();
                Modal.showLoadingBar();
                if (autorizadorStatus === 1 && vm.resident.isOwner === 0) {
                    updateAccount(0);
                } else if (autorizadorStatus === 0 && vm.resident.isOwner === 1) {
                    if (vm.resident.userId !== null) {
                        updateAccount(1);
                    } else {
                        createAccount(2);
                    }
                } else if (autorizadorStatus === 0 && vm.resident.isOwner === 0) {

                    updateAccount(vm.resident.enabled);

                } else if (autorizadorStatus === 1 && vm.resident.isOwner === 1) {
                    updateAccount(vm.resident.enabled);
                }
            }

            function onSaveImageSuccessUpdate(data) {
                vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                if (vm.resident.identificationnumber !== undefined || vm.resident.identificationnumber != null) {
                    vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                }
                if (vm.resident.type == 2) {
                    vm.resident.houseId = null;
                }
                Resident.update(vm.resident, onUpdateSuccess, onSaveError);
            }

            function onNotify(info) {
                vm.progress = Math.round((info.loaded / info.total) * 100);
            }


            function changeStatusIsOwner() {
                if (vm.resident.isOwner) {
                    vm.resident.isOwner = 1
                } else if (vm.resident.isOwner === false || vm.resident.isOwner == null) {
                    vm.resident.isOwner = 0
                }
            }

            function updateAccount(status) {

                User.getUserById({id: vm.resident.userId}, onSuccess);

                function onSuccess(user) {

                    user.id = vm.resident.userId;
                    user.activated = status;
                    user.firstName = vm.resident.name;
                    user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
                    user.email = vm.resident.email;
                    user.login = vm.resident.email;
                    // if (vm.resident.type == 1) {
                    //     user.authorities = ["ROLE_OWNER", "ROLE_USER"];
                    // } else {
                    //     user.authorities = ["ROLE_USER"];
                    // }
                    User.update(user, saveImageUpdate);

                }

            }

            function onUpdateSuccess(result) {
                WSResident.sendActivity(result);
                vm.isSaving = false;
                $state.go('owner');
                Modal.hideLoadingBar();
                Modal.toast("Se ha editado el propietario correctamente.");
            }


            function onSaveImageSuccessInsert(data) {
                vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                if (vm.resident.identificationnumber !== undefined || vm.resident.identificationnumber != null) {
                    vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                }
                Resident.save(vm.resident, onSaveSuccess, onSaveError);


            }

            function onSaveSuccess(result) {
                WSResident.sendActivity(result);
                vm.isSaving = false;
                $state.go('owner');
                Modal.hideLoadingBar();
                Modal.toast("Se ha registrado el propietario correctamente.");
            }


            function onSaveError() {
                Modal.toast("Ocurrió un error insperado.");
                Modal.hideLoadingBar();
                vm.isSaving = false;
            }

            function onSaveLoginError(error) {
                vm.isSaving = false;
                switch (error.data.login) {
                    case "emailexist":
                        Modal.toast("El correo electrónico ingresado ya existe.");
                        Modal.hideLoadingBar();
                        break;
                    case "userexist":
                        Modal.toast("El correo electrónico ingresado ya existe.");
                        Modal.hideLoadingBar();
                        break;
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

            // function generateLogin(config) {
            //     function getCleanedString(cadena) {
            //         // Definimos los caracteres que queremos eliminar
            //         var specialChars = "!@#$^&%*()+=-[]\/{}|:<>?,.";
            //
            //         // Los eliminamos todos
            //         for (var i = 0; i < specialChars.length; i++) {
            //             cadena = cadena.replace(new RegExp("\\" + specialChars[i], 'gi'), '');
            //         }
            //
            //         // Lo queremos devolver limpio en minusculas
            //         cadena = cadena.toLowerCase();
            //
            //         // Quitamos espacios y los sustituimos por _ porque nos gusta mas asi
            //         cadena = cadena.replace(/ /g, "_");
            //
            //         // Quitamos acentos y "ñ". Fijate en que va sin comillas el primer parametro
            //         cadena = cadena.replace(/á/gi, "a");
            //         cadena = cadena.replace(/é/gi, "e");
            //         cadena = cadena.replace(/í/gi, "i");
            //         cadena = cadena.replace(/ó/gi, "o");
            //         cadena = cadena.replace(/ú/gi, "u");
            //         cadena = cadena.replace(/ñ/gi, "n");
            //         return cadena;
            //     }
            //
            //     var firstletterFirstName = vm.resident.name.charAt(0);
            //     var firstletterSecondName = vm.resident.secondlastname.charAt(0);
            //     if (config == 1) {
            //         vm.loginStringCount = vm.loginStringCount + 1;
            //         return getCleanedString(firstletterFirstName + vm.resident.lastname + firstletterSecondName + vm.loginStringCount);
            //     }
            //     return getCleanedString(firstletterFirstName + vm.resident.lastname + firstletterSecondName);
            // }
            //


            vm.findInPadron = function (resident) {

                if (resident.identificationnumber !== undefined || resident.identificationnumber !== "") {
                    if (hasCaracterEspecial(resident.identificationnumber) || haswhiteCedula(resident.identificationnumber) || resident.nationality === "9" && hasLetter(resident.identificationnumber)) {
                        resident.validIdentification = 0;
                    } else {
                        resident.validIdentification = 1;
                    }

                    if (resident.nationality === "9" && resident.identificationnumber !== undefined) {
                        if (resident.identificationnumber.trim().length === 9) {
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


            function hasCaracterEspecial(s) {
                var caracteres = [, ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^", "!"]
                var invalido = 0;
                angular.forEach(caracteres, function (val, index) {
                    if (s != undefined) {
                        for (var i = 0; i < s.length; i++) {
                            if (s.charAt(i) === val) {
                                invalido++;
                            }
                        }
                    }
                })
                if (invalido === 0) {
                    return false;
                } else {
                    return true;
                }
            }

            function hasLetter(s) {
                var caracteres = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]
                var invalido = 0;
                angular.forEach(caracteres, function (val, index) {
                    if (s !== undefined) {
                        for (var i = 0; i < s.length; i++) {
                            if (s.charAt(i).toUpperCase() === val.toUpperCase()) {

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


            function hasWhiteSpace(s) {
                function tiene(s) {
                    return /\s/g.test(s);
                }

                if (tiene(s) || s == undefined) {
                    return true
                }
                return false;
            }


            function alreadyExist() {
                Modal.toast("La cédula ingresada ya existe.");
                vm.isSaving = false;
            }


            vm.validate = function () {
                var invalido = 0;
                if (vm.resident.name === null || vm.resident.lastname === null || vm.resident.identificationnumber != null && hasWhiteSpace(vm.resident.identificationnumber)) {
                    Modal.toast("No puede ingresar espacios en blanco.");
                    invalido++;
                } else if (hasCaracterEspecial(vm.resident.name) || hasCaracterEspecial(vm.resident.lastname) || hasCaracterEspecial(vm.resident.secondlastname) || hasCaracterEspecial(vm.resident.identificationnumber)) {
                    invalido++;
                    Modal.toast("No puede ingresar ningún caracter especial.");
                }
                if (invalido === 0) {
                    return true;
                } else {
                    return false;
                }
            };


        }
    }
)();
