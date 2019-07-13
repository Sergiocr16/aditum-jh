(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroOfficerAccountDialogController', MacroOfficerAccountDialogController);

    MacroOfficerAccountDialogController.$inject = ['Modal','CommonMethods','$rootScope','$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MacroOfficerAccount', 'User', 'MacroCondominium'];

    function MacroOfficerAccountDialogController (Modal,CommonMethods, $rootScope,$timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MacroOfficerAccount, User, MacroCondominium) {
        var vm = this;
        vm.user = {
            email: null,
            password: null,
            id: null
        };
        $rootScope.active = "macro-condominium";
        vm.class = "small-caption";
        vm.macroCondoId = $stateParams.id;
        vm.macroOfficerAccount = entity;
        vm.clear = clear;
        vm.save = save;
        // vm.users = User.query();
        // vm.macrocondominiums = MacroCondominium.query();
        MacroCondominium.get({id : vm.macroCondoId },function(result){
            vm.macroCondo = result;
            if(vm.macroOfficerAccount.id==null) {
                vm.macroOfficerAccount.name = vm.macroCondo.name;
            }
        });
        if(vm.macroOfficerAccount.id!=null) {
            User.getUserById({
                id: vm.macroOfficerAccount.userId
            }, function (user) {
                vm.user = user;
                vm.macroOfficerAccount.password = CommonMethods.decryptIdUrl(user.lastName);
                vm.macroOfficerAccount.login = user.login;
            }, onSaveError)
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        vm.showPassword = function () {
            if (vm.class === "small-caption") {
                vm.class = "";
            } else {
                vm.class = "small-caption";
            }
        };
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function generateRandomEmail() {
            var number = Math.floor(Math.random() * 9000000000) + 1000000000;
            vm.macroOfficerAccount.email = number + "@aditumcr.com";
            vm.user.email = vm.macroOfficerAccount.email;
        }
        function save () {
            vm.isSaving = true;
            if (vm.macroOfficerAccount.id !== null) {
                vm.user.login = vm.macroOfficerAccount.login;
                vm.user.firstName = vm.macroOfficerAccount.name;
                vm.user.lastName = CommonMethods.encryptIdUrl(vm.macroOfficerAccount.password);
                vm.user.contrasenna = vm.macroOfficerAccount.password;
                vm.user.activated = vm.macroOfficerAccount.enabled;
                User.updateWithPassword(vm.user, function () {
                    MacroOfficerAccount.update(vm.macroOfficerAccount, onUpdateUserSuccess, onSaveError);
                }, onSaveUserError);
            } else {
                var authorities = ["ROLE_OFFICER_MACRO"];
                vm.user.firstName = vm.macroOfficerAccount.name;
                vm.user.lastName = CommonMethods.encryptIdUrl(vm.macroOfficerAccount.password);
                vm.user.contrasenna = vm.macroOfficerAccount.password;
                generateRandomEmail();
                vm.user.activated = vm.macroOfficerAccount.enabled;
                vm.user.authorities = authorities;
                vm.user.login = vm.macroOfficerAccount.login;
                User.createUserWithoutSendEmailWithPassword(vm.user, onSaveUserSuccess, onSaveUserError);
            }


        }
        function onSaveUserSuccess(result) {
            vm.macroOfficerAccount.userId = result.id;
            vm.macroOfficerAccount.macroCondominiumId = vm.macroCondoId;
            MacroOfficerAccount.save(vm.macroOfficerAccount, onSaveSuccess, onSaveError);
        }
        function onUpdateUserSuccess(result) {
            Modal.toast("Los datos de la cuenta se han actualizado correctamente.");
            vm.clear();
        }
        function onSaveSuccess(result) {
            Modal.toast("La cuenta  se ha creado correctamente.");
            vm.clear();
        }
        function onSaveError() {
            vm.isSaving = false;
        }
        function onSaveUserError(error) {
            console.log(error.data.login)
            if (error.data.login === "userexist") {
                Modal.toast("El nombre de usuario ya existe.");
            } else if (error.data.login === "emailexist") {
                Modal.toast("El correo electrónico ya existe.");
            }
            vm.isSaving = false;
        }
        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
