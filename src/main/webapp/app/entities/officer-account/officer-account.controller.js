(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountController', OfficerAccountController);

    OfficerAccountController.$inject = ['$rootScope', '$state', 'CommonMethods', 'User', 'Modal', 'Company', 'OfficerAccount', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal'];

    function OfficerAccountController($rootScope, $state, CommonMethods, User, Modal, Company, OfficerAccount, ParseLinks, AlertService, paginationConstants, pagingParams, Principal) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        loadCondos();
        vm.isReady = false;

        $rootScope.active = "officerAccount";

        function loadCondos() {
            Company.query(onSuccessCompany);

            function onSuccessCompany(data) {
                vm.condos = data;
                vm.companySelected = data[0];
                loadAll();
            }
        }

        function loadAll() {
            OfficerAccount.getOfficerAccountsByCompanyId({
                companyId: vm.companySelected.id
            }, onSuccess, onError);

            function onSuccess(data) {
                vm.officerAccount = data;
                vm.isReady = true


                angular.forEach(vm.officerAccount, function (officer, key) {
                    User.getUserById({
                        id: officer.userId
                    }, function (user) {
                        officer.user = user;
                    });
                });

            }

            function onError(error) {
                AlertService.error(error.data.message);

            }

        }

        vm.editOfficerAccount = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('officerAccounts.edit', {
                id: encryptedId
            })
        }

        vm.deleteOfficerAccount = function (officerAcount) {

            Modal.confirmDialog("¿Está seguro que desea eliminar esta cuenta?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    vm.login = officerAcount.userLogin;
                    OfficerAccount.delete({
                        id: officerAcount.id
                    }, onSuccessDelete);
                });


        };

        function onSuccessDelete() {
            User.delete({login: vm.login},
                function () {
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha eliminado la cuenta correctamente.");
                    loadAll();
                });
        }


        vm.disableEnabledOfficer = function (officerAcount) {

            var correctMessage;

            if (officerAcount.enable == 1) {
                correctMessage = "¿Está seguro que desea deshabilitar esta cuenta?";


            } else {
                correctMessage = "¿Está seguro que desea habilitar esta cuenta?";

            }

            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                if (officerAcount.enable == 1) {
                    officerAcount.enable = 2;

                } else {
                    officerAcount.enable = 1;
                }
                console.log(officerAcount)
                OfficerAccount.update(officerAcount, function () {
                    if (officerAcount.user.activated == 1) {
                        officerAcount.user.activated = 0;
                    } else {
                        officerAcount.user.activated = 1;
                    }

                    User.update(officerAcount.user, onSuccessDisabledUser);
                });
            });

        }
        function onSuccessDisabledUser(data) {
            if (data.activated == 1) {
                Modal.toast("Se ha habilitado la cuenta correctamente.");
            } else {
                Modal.toast("Se ha deshabilitado la cuenta correctamente.");
            }

            Modal.hideLoadingBar();
            loadAll();
        }

        vm.changeCompany = function (company, i) {
            vm.isReady = false;
            vm.officerAccount = [];

            if (company !== undefined) {
                vm.selectedIndex = i;
                vm.companySelected = company;


            } else {

            }

            loadAll();

        };


    }
})();
