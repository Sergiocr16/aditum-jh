(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDialogController', OfficerDialogController);

    OfficerDialogController.$inject = ['$rootScope', '$state', 'Principal', '$timeout', 'CommonMethods', '$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company', 'SaveImageCloudinary', 'PadronElectoral', 'globalCompany', 'Modal'];

    function OfficerDialogController($rootScope, $state, Principal, $timeout, CommonMethods, $scope, $stateParams, $q, DataUtils, entity, Officer, User, Company, SaveImageCloudinary, PadronElectoral, globalCompany, Modal) {
        var vm = this;
        vm.isReady = false;
        var fileImage = null;
        vm.isAuthenticated = Principal.isAuthenticated;
        Principal.identity().then(function (account) {
            if (account.authorities[0] === "ROL_RH" || account.authorities[0] === "ROL_ADMIN") {
                vm.required = 1;
            }
            vm.isReady = true;
        })

        vm.officer = entity;
        vm.officer.type = "9";
        if (vm.officer.image_url == undefined) {
            vm.officer.image_url = null;
        }
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.user = entity;
        moment.locale('es');

        var indentification = vm.officer.identificationnumber;
        CommonMethods.validateLetters();
        vm.loginStringCount = 0;
        CommonMethods.validateNumbers();
        vm.birthdate = new Date().setYear(new Date().getYear() - 20);
        $rootScope.active = "officers";
        if (vm.officer.id !== null) {
            vm.title = "Editar oficial";
            vm.button = "Editar";
            vm.birthdate = new Date(vm.officer.fechanacimiento);
            vm.birthdateToShow = moment(vm.birthdate).format("D MMM YYYY");
        } else {
            vm.title = "Registrar oficial";
            vm.button = "Registrar";
        }
        $rootScope.mainTitle = vm.title;


        function save() {
            vm.isSaving = true;
            CommonMethods.waitingMessage();
            if (vm.officer.id !== null) {
                if (indentification !== vm.officer.identificationnumber) {
                    Officer.getByCompanyAndIdentification({
                        companyId: globalCompany.getId(),
                        identificationID: vm.officer.identificationnumber
                    }, alreadyExist, allClear)

                    function alreadyExist(data) {
                        toastr["error"]("La cédula ingresada ya existe.");
                        bootbox.hideAll();
                    }

                    function allClear(data) {
                        updateOfficer();
                    }
                } else {
                    updateOfficer();
                }
            } else {
                Officer.getByCompanyAndIdentification({
                    companyId: globalCompany.getId(),
                    identificationID: vm.officer.identificationnumber
                }, alreadyExist, allClear)

                function alreadyExist(data) {
                    toastr["error"]("La cédula ingresada ya existe.");
                    bootbox.hideAll();
                }

                function allClear(data) {
                    vm.officer.name = CommonMethods.capitalizeFirstLetter(vm.officer.name);
                    vm.officer.lastname = CommonMethods.capitalizeFirstLetter(vm.officer.lastname);
                    vm.officer.secondlastname = CommonMethods.capitalizeFirstLetter(vm.officer.secondlastname);
                    Principal.identity().then(function (account) {
                        if (account.authorities[0] != "ROLE_RH") {
                            vm.officer.companyId = globalCompany.getId();
                        }
                        insertOfficer();

                    })

                }
            }
        }

        function haswhiteCedula(s) {
            return /\s/g.test(s);
        }

        vm.findInPadron = function (officer) {

            if (officer.identificationnumber != undefined || officer.identificationnumber != "") {
                if (hasCaracterEspecial(officer.identificationnumber) || haswhiteCedula(officer.identificationnumber) || officer.type == "9" && hasLetter(officer.identificationnumber)) {
                    officer.validIdentification = 0;
                } else {
                    officer.validIdentification = 1;
                }

                if (officer.type == "9" && officer.identificationnumber != undefined) {
                    if (officer.identificationnumber.trim().length == 9) {
                        PadronElectoral.find(officer.identificationnumber, function (person) {
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    var nombre = person.nombre.split(",");
                                    officer.name = nombre[0];
                                    officer.lastname = nombre[1];
                                    officer.secondlastname = nombre[2];
                                    officer.found = 1;
                                })
                            }, 100)
                        }, function () {

                        })


                    } else {
                        setTimeout(function () {
                            $scope.$apply(function () {
                                officer.found = 0;
                            })
                        }, 100)
                    }
                } else {
                    officer.found = 0;
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

        function onSaveError() {
            vm.isSaving = false;
        }

        function insertOfficer() {
            vm.officer.userId = 1;
            vm.officer.inservice = 0;
            vm.officer.enable = true;
            vm.officer.fechanacimiento = new Date(vm.officer.fechanacimiento);
            vm.imageUser = {user: "a"};
            if (fileImage !== null) {
                SaveImageCloudinary
                    .save(fileImage, vm.imageUser)
                    .then(onSaveImageSuccess, onSaveError, onNotify);

                function onNotify(info) {
                    vm.progress = Math.round((info.loaded / info.total) * 100);
                }

                function onSaveImageSuccess(data) {
                    vm.officer.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                    Officer.save(vm.officer, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        vm.isSaving = false;
                        Principal.identity().then(function (account) {
                            if (account.authorities[0] == "ROLE_RH") {
                                $state.go('officer-rh');
                            } else {
                                $state.go('officer');
                            }
                            Modal.hideLoadingBar();
                            Modal.toast("Se ha registrado el oficial correctamente.");
                        })
                    }
                }
            } else {
                Officer.save(vm.officer, onSaveSuccess, onSaveError);

                function onSaveSuccess(result) {
                    vm.isSaving = false;
                    Principal.identity().then(function (account) {
                        if (account.authorities[0] == "ROLE_RH") {
                            $state.go('officer-rh');
                        } else {
                            $state.go('officer');
                        }
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha registrado el oficial correctamente.");
                    })

                }
            }

        }

        function updateOfficer() {
            vm.officer.fechanacimiento = new Date(vm.officer.fechanacimiento);
            vm.imageUser = {user: vm.officer.identificationnumber};
            if (fileImage !== null) {
                SaveImageCloudinary
                    .save(fileImage, vm.imageUser)
                    .then(onSaveImageSuccess, onSaveError, onNotify);

                function onNotify(info) {
                    vm.progress = Math.round((info.loaded / info.total) * 100);
                }

                function onSaveImageSuccess(data) {
                    vm.officer.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";

                    Officer.update(vm.officer, onUpdateSuccess, onSaveError);
                }
            } else {
                Officer.update(vm.officer, onUpdateSuccess, onSaveError);
            }

        }

        function onUpdateSuccess(result) {
            vm.isSaving = false;
            Principal.identity().then(function (account) {
                if (account.authorities[0] == "ROLE_RH") {
                    $state.go('officer-rh');
                } else {
                    $state.go('officer');
                }
                Modal.hideLoadingBar();
                Modal.toast("Se ha editado el oficial correctamente.");
            })
        }

        function onSaveSuccess(result) {
            Principal.identity().then(function (account) {
                if (account.authorities[0] == "ROLE_RH") {
                    $state.go('officer-rh');
                } else {
                    $state.go('officer');
                }

            })
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
})();
