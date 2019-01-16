(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDialogController', ResidentDialogController);

    ResidentDialogController.$inject = ['$state', '$timeout', '$scope', '$rootScope', '$stateParams', 'CommonMethods', 'previousState', 'DataUtils', '$q', 'entity', 'Resident', 'User', 'Company', 'House', 'Principal', 'companyUser', 'WSResident', 'SaveImageCloudinary', 'PadronElectoral', 'Modal', 'globalCompany'];

    function ResidentDialogController($state, $timeout, $scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q, entity, Resident, User, Company, House, Principal, companyUser, WSResident, SaveImageCloudinary, PadronElectoral, Modal, globalCompany) {
        $rootScope.active = "residents";
        var vm = this;
        vm.isReady = false;
        var fileImage = null;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        vm.resident.principalContact = vm.resident.principalContact + "";
        if (entity.image_url == undefined) {
            entity.image_url = null;
        }
        vm.resident.nationality = "9";
        vm.required = 1;
        vm.validEmail = function (email) {
            var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(String(email).toLowerCase());
        }
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        var indentification = vm.resident.identificationnumber;
        vm.user = entity;
        vm.success = null;
        vm.loginStringCount = 0;
        vm.SaveUserError = false;

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
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        if (vm.resident.id !== null) {
            vm.title = "Editar usuario";
            vm.button = "Editar";
            vm.resident.type = vm.resident.type + "";
            var autorizadorStatus = vm.resident.isOwner;
            if (vm.resident.isOwner == 1) {
                vm.resident.isOwner = true;
            }
        } else {
            vm.title = "Registrar usuario";
            vm.button = "Registrar";
        }
        $rootScope.mainTitle = vm.title;


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

                    vm.resident.name = vm.resident.name.toUpperCase();
                    vm.resident.lastname = vm.resident.lastname.toUpperCase();
                    vm.resident.secondlastname = vm.resident.secondlastname.toUpperCase();
                    vm.isSaving = true;
                    if (vm.resident.id !== null) {
                        if (indentification !== vm.resident.identificationnumber) {
                            Resident.getByCompanyAndIdentification({
                                companyId: globalCompany.getId(),
                                identificationID: vm.resident.identificationnumber
                            }, alreadyExist, allClearUpdate)

                            function alreadyExist(data) {
                                Modal.toast("La cédula ingresada ya existe.");
                            }

                        } else {
                            updateResident();
                        }


                    } else {
                        Resident.getByCompanyAndIdentification({
                            companyId: globalCompany.getId(),
                            identificationID: vm.resident.identificationnumber
                        }, alreadyExist, allClearInsert)

                        function alreadyExist(data) {
                            Modal.toast("La cédula ingresada ya existe.");
                        }
                    }
                })

            }

            function allClearInsert(data) {
                if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "") {
                    Modal.toast("Debe ingresar un correo para asignar el usuario como crear una cuenta.");
                } else if (vm.resident.isOwner == 1) {
                    Modal.showLoadingBar();
                    createAccount(1);
                } else {
                    Modal.showLoadingBar();
                    insertResident(null);
                }
            }

            function allClearUpdate(data) {
                updateResident();
            }

            function updateResident() {
                if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "") {
                    Modal.toast("Debe ingresar un correo para asignar el usuario como crear una cuenta.");
                } else if (autorizadorStatus == 1 && vm.resident.isOwner == false) {
                    Modal.showLoadingBar();
                    updateAccount(0);
                } else if (autorizadorStatus == 0 && vm.resident.isOwner == true) {
                    if (vm.resident.userId !== null) {
                        Modal.showLoadingBar();
                        updateAccount(1);
                    } else {
                        Modal.showLoadingBar();
                        createAccount(2);
                    }
                } else if (vm.resident.isOwner == false) {
                    changeStatusIsOwner();
                    Modal.showLoadingBar();
                    vm.imageUser = {user: vm.resident.id};
                    if (fileImage !== null) {
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

                            Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                        }
                    } else {

                        if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                        }
                        Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                    }
                } else {
                    Modal.showLoadingBar();
                    updateAccount(vm.resident.enabled);
                }
            }

            function changeStatusIsOwner() {
                if (vm.resident.isOwner == false) {
                    vm.resident.isOwner = 0
                } else {
                    vm.resident.isOwner = 1
                }
            }

            function createAccount(opcion) {
                vm.opcion = opcion;
                var authorities = ["ROLE_USER"];
                vm.user.firstName = vm.resident.name;
                vm.user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
                vm.user.email = vm.resident.email;
                vm.user.activated = true;
                vm.user.authorities = authorities;
                vm.user.login = generateLogin(0);
                User.save(vm.user, onSaveUser, onSaveLoginError);


            }

            function onSaveUser(result) {
                if (vm.opcion == 1) {
                    insertResident(result.id)
                }
                else if (vm.opcion == 2) {
                    vm.resident.userId = result.id;
                    vm.resident.isOwner = 1;
                    vm.imageUser = {user: vm.resident.id};
                    if (fileImage !== null) {
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
                            Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                        }
                    } else {
                        if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                        }
                        Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                    }
                }
                vm.isSaving = false;
            }

            function updateAccount(status) {
                User.getUserById({id: vm.resident.userId}, onSuccess);

                function onSuccess(user, headers) {
                    user.id = vm.resident.userId;
                    user.activated = status;
                    user.firstName = vm.resident.name;
                    user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
                    user.email = vm.resident.email;
                    User.update(user, onSuccessUser);

                    function onSuccessUser(data, headers) {
                        changeStatusIsOwner();
                        vm.imageUser = {user: vm.resident.id};
                        if (fileImage !== null) {
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
                                Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                            }
                        } else {
                            if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                                vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                            }
                            Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                        }

                    }
                }

            }

            function onUpdateSuccess(result) {
                WSResident.sendActivity(result);
                vm.isSaving = false;
                $state.go('resident');
                Modal.hideLoadingBar();
                Modal.toast("Se ha editado el usuario correctamente.");
            }

            function insertResident(id) {
                vm.resident.enabled = 1;
                vm.resident.companyId = globalCompany.getId();
                vm.resident.userId = id;
                if (vm.resident.isOwner) {
                    vm.resident.isOwner = 1;
                } else {
                    vm.resident.isOwner = 0;
                }
                vm.imageUser = {user: id};
                if (fileImage !== null) {
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

                        Resident.save(vm.resident, onSaveSuccess, onSaveError);

                        function onSaveSuccess(result) {
                            WSResident.sendActivity(result);
                            vm.isSaving = false;
                            $state.go('resident');
                            Modal.hideLoadingBar();
                            Modal.toast("Se ha registrado el usuario correctamente.");
                        }
                    }
                } else {

                    if (vm.resident.identificationnumber != undefined || vm.resident.identificationnumber != null) {
                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                    }

                    Resident.save(vm.resident, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        WSResident.sendActivity(result);
                        vm.isSaving = false;
                        $state.go('resident');
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha registrado el usuario correctamente.");
                    }
                }

            }

            function generateLogin(config) {
                function getCleanedString(cadena) {
                    // Definimos los caracteres que queremos eliminar
                    var specialChars = "!@#$^&%*()+=-[]\/{}|:<>?,.";

                    // Los eliminamos todos
                    for (var i = 0; i < specialChars.length; i++) {
                        cadena = cadena.replace(new RegExp("\\" + specialChars[i], 'gi'), '');
                    }

                    // Lo queremos devolver limpio en minusculas
                    cadena = cadena.toLowerCase();

                    // Quitamos espacios y los sustituimos por _ porque nos gusta mas asi
                    cadena = cadena.replace(/ /g, "_");

                    // Quitamos acentos y "ñ". Fijate en que va sin comillas el primer parametro
                    cadena = cadena.replace(/á/gi, "a");
                    cadena = cadena.replace(/é/gi, "e");
                    cadena = cadena.replace(/í/gi, "i");
                    cadena = cadena.replace(/ó/gi, "o");
                    cadena = cadena.replace(/ú/gi, "u");
                    cadena = cadena.replace(/ñ/gi, "n");
                    return cadena;
                }

                var firstletterFirstName = vm.resident.name.charAt(0);
                var firstletterSecondName = vm.resident.secondlastname.charAt(0);
                if (config == 1) {
                    vm.loginStringCount = vm.loginStringCount + 1;
                    return getCleanedString(firstletterFirstName + vm.resident.lastname + firstletterSecondName + vm.loginStringCount);
                }
                return getCleanedString(firstletterFirstName + vm.resident.lastname + firstletterSecondName);
            }

            function onSaveError() {
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
                        vm.user.login = generateLogin(1);

                        User.save(vm.user, onSaveUser, onSaveLoginError);

                        break;
                }

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
