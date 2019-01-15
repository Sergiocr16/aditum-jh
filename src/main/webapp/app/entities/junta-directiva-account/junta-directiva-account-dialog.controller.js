(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JuntaDirectivaAccountDialogController', JuntaDirectivaAccountDialogController);

    JuntaDirectivaAccountDialogController.$inject = ['$rootScope', '$timeout', '$scope', '$stateParams', '$q', 'entity', 'JuntaDirectivaAccount', 'CommonMethods', 'Company', 'User', 'globalCompany', 'Modal'];

    function JuntaDirectivaAccountDialogController($rootScope, $timeout, $scope, $stateParams, $q, entity, JuntaDirectivaAccount, CommonMethods, Company, User, globalCompany, Modal) {
        var vm = this;
        vm.user = entity;
        $rootScope.active = 'jdAccount';
        vm.juntaDirectivaAccount = entity;
        vm.save = save;
        vm.companies = Company.query();
        vm.users = User.query();
        vm.isReady = false;
        vm.class = "small-caption";
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        Company.get({
            id: globalCompany.getId()
        }, companySuccess, onSaveError)

        function companySuccess(result) {

            vm.company = result;
            JuntaDirectivaAccount.getByCompanyId({
                companyId: globalCompany.getId()
            }, successJuntaDirectiva, onSaveErrorJuntaDirectiva)

        }

        function save() {
            vm.isSaving = true;
            if (vm.juntaDirectivaAccount.id !== null) {
                vm.user.login = vm.juntaDirectivaAccount.login;
                vm.user.lastName = CommonMethods.encryptIdUrl(vm.juntaDirectivaAccount.password);
                vm.user.contrasenna = vm.juntaDirectivaAccount.password;
                User.updateWithPassword(vm.user, function () {
                    JuntaDirectivaAccount.update(vm.juntaDirectivaAccount, onUpdateUserSuccess, onSaveError);
                }, onSaveUserError);

            } else {
                var authorities = ["ROLE_JD"];
                vm.user.firstName = "Junta directiva " + vm.company.name;
                vm.user.lastName = CommonMethods.encryptIdUrl(vm.juntaDirectivaAccount.password);
                vm.user.contrasenna = vm.juntaDirectivaAccount.password;
                generateRandomEmail();
                vm.user.activated = true;
                vm.user.authorities = authorities;
                vm.user.login = vm.juntaDirectivaAccount.login;
                User.createUserWithoutSendEmailWithPassword(vm.user, onSaveUserSuccess, onSaveUserError);

            }
        }

        vm.showPassword = function () {
            if (vm.class === "small-caption") {
                vm.class = "";
            } else {
                vm.class = "small-caption";
            }

        };

        function onSaveErrorJuntaDirectiva(error) {
            vm.isReady = true;
            vm.isSaving = true;
        }

        function onUpdateUserSuccess(result) {
            Modal.toast("Los datos de la cuenta se han actualizado correctamente.");
        }

        function successJuntaDirectiva(result) {
            console.log(result.userId)
            User.getUserById({
                id: result.userId
            }, function (user) {
                vm.isReady = true;
                vm.user = user;
                vm.juntaDirectivaAccount = result;
                vm.juntaDirectivaAccount.password = CommonMethods.decryptIdUrl(user.lastName);
                vm.juntaDirectivaAccount.login = user.login;

            }, onSaveError)

        }

        function generateRandomEmail() {
            var number = Math.floor(Math.random() * 9000000000) + 1000000000;
            vm.juntaDirectivaAccount.email = number + "@aditumcr.com";
            vm.user.email = vm.juntaDirectivaAccount.email;
        }

        function onSaveUserSuccess(result) {
            vm.juntaDirectivaAccount.userId = result.id;
            vm.juntaDirectivaAccount.companyId = globalCompany.getId();
            JuntaDirectivaAccount.save(vm.juntaDirectivaAccount, onSaveSuccess, onSaveError);
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

        function onSaveSuccess(result) {
            Modal.toast("La cuenta de junta directiva se ha creado correctamente.");
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
