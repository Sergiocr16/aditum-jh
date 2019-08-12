(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('AdminInfoDialogController', AdminInfoDialogController);

        AdminInfoDialogController.$inject = ['globalCompany', 'MacroCondominium', 'PadronElectoral', 'Modal', '$rootScope', '$state', 'CommonMethods', '$timeout', '$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'AdminInfo', 'User', 'Company', 'Principal', 'SaveImageCloudinary'];

        function AdminInfoDialogController(globalCompany, MacroCondominium, PadronElectoral, Modal, $rootScope, $state, CommonMethods, $timeout, $scope, $stateParams, $q, DataUtils, entity, AdminInfo, User, Company, Principal, SaveImageCloudinary) {

            var vm = this;
            var fileImage = null;
            vm.loginStringCount = 0;
            if (entity.image_url == undefined) {
                entity.image_url = null;
            }

            vm.adminInfo = entity;
            vm.adminInfo.nationality = "9";
            vm.byteSize = DataUtils.byteSize;
            vm.openFile = DataUtils.openFile;
            vm.save = save;
            Modal.enteringForm(save);
            $scope.$on("$destroy", function () {
                Modal.leavingForm();
            });
            vm.user = entity;
            vm.isReady = false;
            vm.required = 1;
            vm.users = User.query();
            vm.company = Company.query();
            vm.companies = Company.query();
            vm.isAuthenticated = Principal.isAuthenticated;
            var indentification = vm.adminInfo.identificationnumber;
            $timeout(function () {
                angular.element('.form-group:eq(1)>input').focus();
            });

            vm.isAuthenticated = Principal.isAuthenticated;
            loadCondos();

            function loadCondos() {
                MacroCondominium.getCondos({id: globalCompany.getMacroId()}, onSuccessHouses);

                function onSuccessHouses(data) {
                    vm.condos = data.companies;
                    vm.isReady = true;


                    if (vm.adminInfo.id !== null) {
                        User.getUserById({id: vm.adminInfo.userId}, onSuccess);
                        vm.title = "Editar administrador";
                        vm.button = "Editar";
                        angular.forEach(data.companies, function (company, key) {
                            angular.forEach(vm.adminInfo.companies, function (userCompany, key) {
                                if(company.id ==userCompany.id){
                                    company.selected = true;
                                }
                            });
                        });

                    } else {
                        angular.forEach(data.companies, function (value, key) {
                            data.selected = false;
                        });
                        vm.title = "Registrar administrador ";
                        vm.button = "Registrar";

                    }
                }
            }

            vm.selectCompany = function (condo) {
                condo.selected = !condo.selected;

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

                if (vm.adminInfo.name === undefined || vm.adminInfo.lastname === undefined || vm.adminInfo.secondlastname === undefined || hasWhiteSpace(vm.adminInfo.identificationnumber) || hasWhiteSpace(vm.adminInfo.phonenumber) && vm.adminInfo.phonenumber != null && vm.adminInfo.phonenumber !== "") {
                    Modal.toast("No puede ingresar espacios en blanco.");
                    invalido++;
                } else if (hasCaracterEspecial(vm.adminInfo.name) || hasCaracterEspecial(vm.adminInfo.lastname) || hasCaracterEspecial(vm.adminInfo.secondlastname) || hasCaracterEspecial(vm.adminInfo.identificationnumber) || hasCaracterEspecial(vm.adminInfo.phonenumber)) {
                    invalido++;
                    Modal.toast("No puede ingresar ningún caracter especial.");
                }
                if (invalido === 0) {
                    return true;
                } else {
                    return false;
                }
            };

            function haswhiteCedula(s) {
                return /\s/g.test(s);
            }

            vm.validatePhoneNumber = function (adminInfo) {
                if (hasCaracterEspecial(adminInfo.phonenumber) || haswhiteCedula(adminInfo.phonenumber) || adminInfo.nationality == "9" && hasLetter(adminInfo.phonenumber)) {
                    adminInfo.validPhonenumber = 0;
                } else {
                    adminInfo.validPhonenumber = 1;
                }
            };

            vm.findInPadron = function (adminInfo) {

                if (adminInfo.identificationnumber !== undefined || adminInfo.identificationnumber !== "") {
                    if (hasCaracterEspecial(adminInfo.identificationnumber) || haswhiteCedula(adminInfo.identificationnumber) || adminInfo.nationality === "9" && hasLetter(adminInfo.identificationnumber)) {
                        adminInfo.validIdentification = 0;
                    } else {
                        adminInfo.validIdentification = 1;
                    }

                    if (adminInfo.nationality === "9" && adminInfo.identificationnumber !== undefined) {
                        if (adminInfo.identificationnumber.trim().length === 9) {
                            PadronElectoral.find(adminInfo.identificationnumber, function (person) {
                                setTimeout(function () {
                                    $scope.$apply(function () {
                                        var nombre = person.nombre.split(",");
                                        adminInfo.name = nombre[0];
                                        adminInfo.lastname = nombre[1];
                                        adminInfo.secondlastname = nombre[2];
                                        adminInfo.found = 1;
                                    })
                                }, 100)
                            }, function () {

                            })


                        } else {
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    adminInfo.found = 0;
                                })
                            }, 100)
                        }
                    } else {
                        adminInfo.found = 0;
                    }
                }
            };

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


            function onSuccess(user, headers) {
                vm.user = user;
                vm.adminInfo.email = vm.user.email;
            }


            function save() {

                vm.adminInfo.companies = [];
                angular.forEach(vm.condos, function (value, key) {
                    if (value.selected) {
                        vm.adminInfo.companies.push(value)
                    }

                });

                console.log(  vm.adminInfo.companies)
                if (vm.adminInfo.id === undefined || vm.adminInfo.id === null) {
                    var wordOnModal = "registrar";
                } else {
                    var wordOnModal = "modificar";

                }
                if (vm.validate()) {
                    if (vm.adminInfo.companies.length > 0) {


                        Modal.confirmDialog("¿Está seguro que desea " + wordOnModal + " el usuario?", "", function () {

                            vm.adminInfo.name = vm.adminInfo.name.toUpperCase();
                            vm.adminInfo.lastname = vm.adminInfo.lastname.toUpperCase();
                            vm.adminInfo.secondlastname = vm.adminInfo.secondlastname.toUpperCase();
                            vm.isSaving = true;

                            if (vm.adminInfo.id !== null) {
                                if (indentification !== vm.adminInfo.identificationnumber) {
                                    AdminInfo.getByCompanyAndIdentification({
                                        companyId: globalCompany.getId(),
                                        identificationID: vm.adminInfo.identificationnumber
                                    }, alreadyExist, allClearUpdate)


                                } else {
                                    updateAdmin();
                                }

                            } else {
                                // Resident.getByCompanyAndIdentification({
                                //     companyId: globalCompany.getId(),
                                //     identificationID: vm.adminInfo.identificationnumber
                                // }, alreadyExist, allClearInsert)
                                allClearInsert();
                            }
                        })
                    } else {
                        Modal.toast("Debe seleccionar al menos un condominio");
                    }

                }

                function allClearInsert(data) {
                    Modal.showLoadingBar();
                    createAccount(1);
                }

                function onSaveUser(result) {
                    if (vm.opcion === 1) {
                        insertAdmin(result.id)
                    }
                    else if (vm.opcion === 2) {
                        vm.adminInfo.userId = result.id;
                        vm.imageUser = {user: vm.adminInfo.id};
                        if (fileImage !== null) {
                            SaveImageCloudinary
                                .save(fileImage, vm.imageUser)
                                .then(onSaveImageSuccess, onSaveError, onNotify);

                        } else {
                            if (vm.adminInfo.identificationnumber != undefined || vm.adminInfo.identificationnumber != null) {
                                vm.adminInfo.identificationnumber = vm.adminInfo.identificationnumber.toUpperCase()
                            }
                            AdminInfo.update(vm.adminInfo, onUpdateSuccess, onSaveError);
                        }
                    }
                    vm.isSaving = false;
                }

                function updateAdmin() {

                    updateAccount(0);

                }

                function createAccount(opcion) {
                    vm.opcion = opcion;
                    var authorities = ["ROLE_MANAGER"];
                    vm.user.firstName = vm.adminInfo.name;
                    vm.user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
                    vm.user.email = vm.adminInfo.email;
                    vm.user.activated = true;
                    vm.user.authorities = authorities;
                    vm.user.login = generateLogin(0);
                    User.save(vm.user, onSaveUser, onSaveLoginError);

                }


                function onSaveUser(result) {
                    insertAdmin(result.id)
                    vm.isSaving = false;
                }

                function onSaveLoginError(error) {
                    vm.isSaving = false;
                    switch (error.data.login) {
                        case "emailexist":
                            Modal.toast("El correo electrónico ingresado ya existe.");
                            bootbox.hideAll();
                            break;
                        case "userexist":
                            vm.user.login = generateLogin(1);

                            User.save(vm.user, onSaveUser, onSaveLoginError);

                            break;
                    }
                }

                vm.validEmail = function (email) {
                    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                    return re.test(String(email).toLowerCase());
                }

                function insertAdmin(id) {
                    vm.adminInfo.enabled = 1;
                    vm.adminInfo.userId = id;
                    vm.imageUser = {user: id};
                    if (fileImage !== null) {
                        SaveImageCloudinary
                            .save(fileImage, vm.imageUser)
                            .then(onSaveImageSuccessSave, onSaveError, onNotify);


                    } else {

                        if (vm.adminInfo.identificationnumber !== undefined || vm.adminInfo.identificationnumber != null) {
                            vm.adminInfo.identificationnumber = vm.adminInfo.identificationnumber.toUpperCase()
                        }

                        AdminInfo.save(vm.adminInfo, onSaveSuccess, onSaveError);


                    }

                    function onSaveSuccess(result) {
                        vm.isSaving = false;
                        Modal.hideLoadingBar();
                        $state.go('admin-info-by-company');
                        Modal.toast("Se ha registrado el usuario correctamente.");
                    }
                }

                function updateAccount(status) {
                    User.getUserById({id: vm.adminInfo.userId}, onSuccess);

                    function onSuccess(user) {
                        user.id = vm.adminInfo.userId;
                        user.activated = status;
                        user.firstName = vm.adminInfo.name;
                        user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
                        user.email = vm.adminInfo.email;
                        User.update(user, onSuccessUser);

                        function onSuccessUser() {
                            vm.imageUser = {user: vm.adminInfo.id};
                            if (fileImage !== null) {
                                SaveImageCloudinary
                                    .save(fileImage, vm.imageUser)
                                    .then(onSaveImageSuccess, onSaveError, onNotify);

                            } else {
                                if (vm.adminInfo.identificationnumber !== undefined || vm.adminInfo.identificationnumber != null) {
                                    vm.adminInfo.identificationnumber = vm.adminInfo.identificationnumber.toUpperCase()
                                }

                                AdminInfo.update(vm.adminInfo, onUpdateSuccess, onSaveError);
                            }

                        }
                    }

                }

                function onUpdateSuccess(result) {
                    vm.isSaving = false;
                    $state.go('admin-info-by-company');
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha editado el usuario correctamente.");
                }


                function onSaveImageSuccess(data) {
                    vm.adminInfo.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                    if (vm.adminInfo.identificationnumber !== undefined || vm.adminInfo.identificationnumber != null) {
                        vm.adminInfo.identificationnumber = vm.adminInfo.identificationnumber.toUpperCase()
                    }

                    AdminInfo.update(vm.adminInfo, onUpdateSuccess, onSaveError);

                }

                function onNotify(info) {
                    vm.progress = Math.round((info.loaded / info.total) * 100);
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

                    var firstletterFirstName = vm.adminInfo.name.charAt(0);
                    var firstletterSecondName = vm.adminInfo.secondlastname.charAt(0);
                    if (config == 1) {
                        vm.loginStringCount = vm.loginStringCount + 1;
                        return getCleanedString(firstletterFirstName + vm.adminInfo.lastname + firstletterSecondName + vm.loginStringCount);
                    }
                    return getCleanedString(firstletterFirstName + vm.adminInfo.lastname + firstletterSecondName);
                }


                function onSaveError() {
                    Modal.toast("Ocurrió un error insperado.");
                    Modal.hideLoadingBar();
                    vm.isSaving = false;
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
        }
    }

)();
