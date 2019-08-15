(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountDialogController', OfficerAccountDialogController);

    OfficerAccountDialogController.$inject = ['Modal', 'CommonMethods', '$state', '$rootScope', '$timeout', '$scope', '$stateParams', '$q', 'entity', 'OfficerAccount', 'Company', 'User', 'Principal', 'MultiCompany'];

    function OfficerAccountDialogController(Modal, CommonMethods, $state, $rootScope, $timeout, $scope, $stateParams, $q, entity, OfficerAccount, Company, User, Principal, MultiCompany) {
        var vm = this;
        vm.user = entity;

        $rootScope.active = "officerAccount";
        vm.class = "";
        vm.officerAccount = entity;
        vm.save = save;
        vm.companies = Company.query();
        vm.users = User.query();
        vm.isReady = false;
        vm.class = "small-caption";
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        loadCondos();

        function loadCondos() {
            Company.query(onSuccessCompany);

            function onSuccessCompany(data) {
                vm.condos = data;
                vm.isReady = true;

                if (vm.officerAccount.id !== null) {
                    User.getUserById({id: vm.officerAccount.userId}, function (user) {
                        vm.officerAccount.login = user.login;
                        vm.user = user;
                        vm.officerAccount.password = CommonMethods.decryptIdUrl(user.lastName);

                        vm.button = "Editar";
                        angular.forEach(vm.condos, function (company, key) {
                            if(company.id == vm.officerAccount.companyId){
                                company.selected = true;
                                vm.title = "Editar cuenta de oficial de seguridad / Condominio " + company.name;
                            }else{
                                company.selected = false;
                            }
                        });
                    });


                } else {
                    vm.title = "Crear cuenta de oficial de seguridad";
                    vm.button = "Registrar";

                }
            }
        }

        function save() {
            Modal.showLoadingBar();
            vm.isSaving = true;
            if (vm.officerAccount.id !== null) {

                vm.user.login = vm.officerAccount.login;
                vm.user.lastName = CommonMethods.encryptIdUrl(vm.officerAccount.password);
                vm.user.contrasenna = vm.officerAccount.password;
                User.updateWithPassword(vm.user, function () {
                    OfficerAccount.update(vm.officerAccount, onUpdateUserSuccess, onSaveError);
                }, onSaveUserError);

            } else {
                var authorities = ["ROLE_OFFICER"];

                angular.forEach(vm.condos, function (company, key) {
                    if (company.id == vm.companyId) {
                        vm.condoSelected = company;
                    }
                });

                vm.user.firstName = "Cuenta oficial " + vm.condoSelected.name;
                vm.user.lastName = CommonMethods.encryptIdUrl(vm.officerAccount.password);
                vm.user.contrasenna = vm.officerAccount.password;
                generateRandomEmail();
                vm.user.activated = true;
                vm.user.authorities = authorities;
                vm.user.login = vm.officerAccount.login;

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

        function onUpdateUserSuccess(result) {
            Modal.hideLoadingBar();
            $state.go('officerAccounts');
            Modal.toast("Los datos de la cuenta se han actualizado correctamente.");
        }

        function generateRandomEmail() {
            var number = Math.floor(Math.random() * 9000000000) + 1000000000;
            vm.officerAccount.email = number + "@aditumcr.com";
            vm.user.email = vm.officerAccount.email;
        }

        function onSaveUserSuccess(result) {
            vm.officerAccount.userId = result.id;
            vm.officerAccount.enable = 1;
            vm.officerAccount.companyId = vm.condoSelected.id;
            OfficerAccount.save(vm.officerAccount, onSaveSuccess, onSaveError);
        }

        function onSaveUserError(error) {

            if (error.data.login === "userexist") {
                Modal.toast("El nombre de usuario ya existe.");
            } else if (error.data.login === "emailexist") {
                Modal.toast("El correo electrónico ya existe.");
            }
            vm.isSaving = false;
        }

        function onSaveSuccess(result) {
            Modal.hideLoadingBar();
            $state.go('officerAccounts');
            Modal.toast("La cuenta de oficial se ha creado correctamente.");
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
