(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseDialogController', ResidentByHouseDialogController);

    ResidentByHouseDialogController.$inject = ['$state', '$timeout', '$scope', '$rootScope', '$stateParams', 'CommonMethods', 'previousState', 'DataUtils', '$q', 'entity', 'Resident', 'User', 'Company', 'House', 'Principal', 'WSResident', 'SaveImageCloudinary', 'PadronElectoral', 'globalCompany', 'Modal'];

    function ResidentByHouseDialogController($state, $timeout, $scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q, entity, Resident, User, Company, House, Principal, WSResident, SaveImageCloudinary, PadronElectoral, globalCompany, Modal) {
        $rootScope.active = "residentsHouses";
        var vm = this;
        var fileImage = null;
        $rootScope.mainTitle = vm.title;
        vm.isAuthenticated = Principal.isAuthenticated;
        if (entity.image_url == undefined) {
            entity.image_url = null;
        }
        vm.resident = entity;
        vm.resident.houseId = globalCompany.getHouseId();
        vm.resident.nationality = "9";
        vm.resident.principalContact = 0;
        vm.residentType = globalCompany.getUser().type;
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
        Principal.identity().then(function (account) {
            vm.account = account;
        });

        if (vm.resident.id !== null) {
            vm.title = "Editar usuario";
            vm.button = "Editar";
            if (vm.resident.isOwner == 1) {
                vm.resident.isOwner = true;
            }
            vm.resident.type = vm.resident.type + "";
        } else {

            vm.title = "Registrar usuario";
            vm.button = "Registrar";
        }
        var wordOnModal = vm.resident.id == undefined ? "registrar" : "modificar"
        $rootScope.mainTitle = vm.title;
        House.query({
            companyId: globalCompany.getId()
        }).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            vm.isReady = true;
        }


        function save() {


            if (vm.validate()) {
                saving()
            }


        }


        function saving() {

            if (vm.validate()) {

                Modal.confirmDialog("¿Está seguro que desea " + wordOnModal + " el propietario?", "", function () {
                    vm.resident.name = vm.resident.name ? vm.resident.name.toUpperCase() : vm.resident.name;
                    vm.resident.lastname = vm.resident.lastname ? vm.resident.lastname.toUpperCase() : vm.resident.lastname;
                    vm.resident.secondlastname = vm.resident.secondlastname ? vm.resident.secondlastname.toUpperCase() : vm.resident.secondlastname;
                    vm.isSaving = true;
                    vm.resident.isCompany = vm.resident.isCompany == 1 ? true : false;

                    if (vm.resident.id == null) {
                        console.log(globalCompany.getUser().type)


                        switch (globalCompany.getUser().type) {
                            case "2":
                                vm.resident.type = 4;
                                break;
                            case "3":
                                vm.resident.type = 3;
                                break;
                            case "4":
                                vm.resident.type = 4;
                                break;
                        }
                        console.log(vm.resident.type)
                        Resident.getByCompanyAndIdentification({
                            companyId: globalCompany.getId(),
                            identificationID: vm.resident.identificationnumber
                        }, alreadyExist, insertResident)

                    } else {

                        if (vm.temporalIndentification !== vm.resident.identificationnumber) {

                            Resident.getByCompanyAndIdentification({
                                companyId: globalCompany.getId(),
                                identificationID: vm.resident.identificationnumber
                            }, alreadyExist, saveImageUpdate)

                        } else {
                            saveImageUpdate();
                        }

                    }
                });
            }

            function insertResident() {
                vm.resident.enabled = 1;
                vm.resident.companyId = globalCompany.getId();
                if (vm.residentType == 1 && vm.resident.type == 1) {
                    House.get({id: globalCompany.getHouseId()}, function (house) {
                        vm.resident.houses = [];
                        vm.resident.houses.push(house);
                        console.log("ffff");
                        console.log(vm.resident);
                        saveImageInsert();
                    });
                }else{

                    saveImageInsert();
                }



            }

            function saveImageInsert() {
                changeStatusIsOwner()
                vm.imageUser = {user: null};
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccessInsert, onSaveError, onNotify);
                } else {
                    if (vm.resident.identificationnumber !== undefined || vm.resident.identificationnumber != null) {
                        vm.resident.identificationnumber = vm.resident.identificationnumber ? vm.resident.identificationnumber.toUpperCase() : vm.resident.identificationnumber;
                    }

                    Resident.save(vm.resident, onSuccess, onSaveError);
                }

            }

            function onSaveImageSuccessInsert(data) {
                vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                if (vm.resident.identificationnumber !== undefined || vm.resident.identificationnumber != null) {
                    vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                }
                Resident.save(vm.resident, onSuccess, onSaveError);

            }

            function alreadyExist() {
                vm.isSaving = false;
                Modal.toast("La cédula ingresada ya existe.");
            }

            function saveImageUpdate() {
                changeStatusIsOwner()
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
                    Resident.update(vm.resident, onSuccess, onSaveError);
                }

            }


            function onNotify(info) {
                vm.progress = Math.round((info.loaded / info.total) * 100);
            }

            function onSaveImageSuccessUpdate(data) {
                vm.resident.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                if (vm.resident.identificationnumber !== undefined || vm.resident.identificationnumber != null) {
                    vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                }
                if (vm.resident.type == 2) {
                    vm.resident.houseId = null;
                }
                Resident.update(vm.resident, onSuccess, onSaveError);
            }

            function onSuccess(result) {
                WSResident.sendActivity(result);
                if (globalCompany.getUser().id === result.id) {
                    $rootScope.companyUser = result;
                }
                vm.isSaving = false;
                $state.go('residentByHouse');

                if (vm.resident.id !== null) {
                    Modal.toast("Se ha editado el usuario correctamente.");
                } else {
                    Modal.toast("Se ha registrado el usuario correctamentecorrectamente.");
                }
                Modal.hideLoadingBar();
            }

            function onSaveError() {
                Modal.toast("Ocurrió un error insperado.");
                Modal.hideLoadingBar();
                vm.isSaving = false;
            }

        }


        function changeStatusIsOwner() {
            if (vm.resident.isOwner) {
                vm.resident.isOwner = 1
            } else if (vm.resident.isOwner === false || vm.resident.isOwner == null) {
                vm.resident.isOwner = 0
            }
        }

        vm.validate = function () {
            var invalido = 0;

            function hasWhiteSpace(s) {
                function tiene(s) {
                    return /\s/g.test(s);
                }

                if (tiene(s) || s === undefined) {
                    return true
                }
                return false;
            }

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

        function haswhiteCedula(s) {
            return /\s/g.test(s);
        }


        vm.findInPadron = function (resident) {

            if (resident.identificationnumber !== undefined || resident.identificationnumber !== "") {
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
