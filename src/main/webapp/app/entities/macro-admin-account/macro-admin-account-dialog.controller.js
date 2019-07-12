(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroAdminAccountDialogController', MacroAdminAccountDialogController);

    MacroAdminAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MacroAdminAccount', 'MacroCondominium', 'User', '$rootScope', 'DataUtils', 'Principal', 'CommonMethods', 'SaveImageCloudinary'];

    function MacroAdminAccountDialogController($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MacroAdminAccount, MacroCondominium, User, $rootScope, DataUtils, Principal, CommonMethods, SaveImageCloudinary) {
        var vm = this;
        $rootScope.active = "macro-condominium";
        vm.class = "small-caption";
        vm.macroCondoId = $stateParams.id;
        vm.macroAdminAccount = entity;
        vm.clear = clear;
        vm.save = save;
        // vm.macrocondominiums = MacroCondominium.query();
        // vm.users = User.query();
        MacroCondominium.get({id: vm.macroCondoId}, function (result) {
            vm.macroCondo = result;
        });

        var vm = this;
        var fileImage = null;
        vm.loginStringCount = 0;
        if (entity.imageUrl == undefined) {
            entity.imageUrl = null;
        }
        vm.macroAdminAccount = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;

        vm.required = 1;
        vm.isAuthenticated = Principal.isAuthenticated;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });


        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.isAuthenticated = Principal.isAuthenticated;

        if (vm.macroAdminAccount.id !== null) {
            User.getUserById({id: vm.macroAdminAccount.userId}, onSuccess);
        }

        function onSuccess(user, headers) {
            vm.user = user;
            vm.macroAdminAccount.email = vm.user.email;
        }


        function save() {
            vm.isSaving = true;
            if (vm.macroAdminAccount.id !== null) {
                vm.imageUser = {user: vm.macroAdminAccount.id};
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify);

                    function onNotify(info) {
                        vm.progress = Math.round((info.loaded / info.total) * 100);
                    }

                    function onSaveImageSuccess(data) {
                        vm.macroAdminAccount.imageUrl = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                        MacroAdminAccount.update(vm.macroAdminAccount, onSaveSuccess, onSaveError);
                        updateAccount();
                    }
                } else {
                    MacroAdminAccount.update(vm.macroAdminAccount, onSaveSuccess, onSaveError);
                    updateAccount();
                }
            } else {
                vm.macroAdminAccount.name = CommonMethods.capitalizeFirstLetter(vm.macroAdminAccount.name);
                vm.macroAdminAccount.lastname = CommonMethods.capitalizeFirstLetter(vm.macroAdminAccount.lastname);
                vm.macroAdminAccount.secondlastname = CommonMethods.capitalizeFirstLetter(vm.macroAdminAccount.secondlastname);
                createAccount();
            }
        }


        function createAccount() {
            var authorities = ["ROLE_MANAGER_MACRO"];
            vm.user.firstName = vm.macroAdminAccount.name;
            vm.user.lastName = vm.macroAdminAccount.lastname + ' ' + vm.macroAdminAccount.secondlastname;
            vm.user.email = vm.macroAdminAccount.email;
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
                    toastr["error"]("El correo electrónico ingresado ya existe.");
                    bootbox.hideAll();
                    break;
                case "userexist":
                    vm.user.login = generateLogin(1);

                    User.save(vm.user, onSaveUser, onSaveLoginError);

                    break;
            }
        }

        function insertAdmin(id) {
            vm.macroAdminAccount.enabled = 1;
            vm.macroAdminAccount.macroCondominiumId = vm.macroCondo.id;
            vm.macroAdminAccount.userId = id;
            vm.imageUser = {user: id};
            if (fileImage !== null) {
                SaveImageCloudinary
                    .save(fileImage, vm.imageUser)
                    .then(onSaveImageSuccess, onSaveError, onNotify);

                function onNotify(info) {
                    vm.progress = Math.round((info.loaded / info.total) * 100);
                }

                function onSaveImageSuccess(data) {

                    vm.macroAdminAccount.imageUrl = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                    MacroAdminAccount.save(vm.macroAdminAccount, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        vm.isSaving = false;
                        $uibModalInstance.close(result);
                        toastr["success"]("Se ha registrado el administrador correctamente.");
                    }
                }
            } else {
                MacroAdminAccount.save(vm.macroAdminAccount, onSaveSuccess, onSaveError);

                function onSaveSuccess(result) {
                    vm.isSaving = false;
                    $uibModalInstance.close(result);
                    toastr["success"]("Se ha registrado el administrador correctamente.");
                }
            }

        }

        function updateAccount() {
            User.getUserById({id: vm.macroAdminAccount.userId}, onSuccess);

            function onSuccess(user, headers) {
                user.id = vm.macroAdminAccount.userId;
                user.activated = vm.macroAdminAccount.enabled;
                user.firstName = vm.macroAdminAccount.name;
                user.lastName = vm.macroAdminAccount.lastname + ' ' + vm.macroAdminAccount.secondlastname;
                user.email = vm.macroAdminAccount.email;

                User.update(user, onSuccessUser);

                function onSuccessUser(data, headers) {
                    macroAdminAccount.update(vm.macroAdminAccount, onUpdateSuccess, onSaveError);

                }
            }

        }

        function onUpdateSuccess(result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
            toastr["success"]("Se ha editado el administrador correctamente.");
        }

        function onSaveError() {
            vm.isSaving = false;
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

            var firstletterFirstName = vm.macroAdminAccount.name.charAt(0);
            var firstletterSecondName = vm.macroAdminAccount.secondlastname.charAt(0);
            if (config == 1) {
                vm.loginStringCount = vm.loginStringCount + 1;
                return getCleanedString(firstletterFirstName + vm.macroAdminAccount.lastname + firstletterSecondName + vm.loginStringCount);
            }
            return getCleanedString(firstletterFirstName + vm.macroAdminAccount.lastname + firstletterSecondName);
        }

        function onSaveSuccess(result) {
            toastr["success"]("Se ha editado tu información correctamente.");
        }

        function onSaveError() {
            vm.isSaving = false;

        }

        function updateAccount() {
            vm.user.id = vm.macroAdminAccount.userId;
            vm.user.activated = 1;
            vm.user.firstName = vm.macroAdminAccount.name;
            vm.user.lastName = vm.macroAdminAccount.lastname + ' ' + vm.macroAdminAccount.secondlastname;
            vm.user.email = vm.macroAdminAccount.email;
            User.update(vm.user, onSuccessUser);

            function onSuccessUser(result) {
                $uibModalInstance.close(result);
                toastr["success"]("Se ha editado el administrador correctamente.");
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
