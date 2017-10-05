(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountsByCompanyController', OfficerAccountsByCompanyController);

    OfficerAccountsByCompanyController.$inject = ['CommonMethods','User','$state','$stateParams','Company','DataUtils', 'OfficerAccount', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','$rootScope'];

    function OfficerAccountsByCompanyController(CommonMethods,User,$state,$stateParams,Company,DataUtils, OfficerAccount, ParseLinks, AlertService, paginationConstants,Principal,$rootScope) {
        $rootScope.active = "condons";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        getCondominio();
        function getCondominio(){
         Company.get({
                    id: $stateParams.companyId
                }, onSuccess, onError);
                function onSuccess(data) {
                    vm.company = data;

                }
                function onError(error) {
                    AlertService.error(error.data.message);

                }
        }

        loadAll ();
        function loadAll () {
            vm.companyId  = $stateParams.companyId;
            OfficerAccount.getOfficerAccountsByCompanyId({
                companyId: $stateParams.companyId
            }, onSuccess, onError);
            function onSuccess(data) {
                vm.officerAccount = data;

            }
            function onError(error) {
                AlertService.error(error.data.message);

            }

        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

                  vm.deleteOfficerAccount = function(officerAcount) {
                            bootbox.confirm({
                                message: "¿Está seguro que desea eliminar la cuenta de oficial?",
                                buttons: {
                                    confirm: {
                                        label: 'Aceptar',
                                        className: 'btn-success'
                                    },
                                    cancel: {
                                        label: 'Cancelar',
                                        className: 'btn-danger'
                                    }
                                },
                                callback: function(result) {
                                    if (result) {
                                        vm.login = officerAcount.userLogin;
                                        OfficerAccount.delete({
                                            id: officerAcount.id
                                        }, onSuccessDelete);


                                    }
                                }
                            });

                            function onSuccessDelete () {
                                    User.delete({login: vm.login},
                                        function () {
                                            toastr["success"]("Se ha eliminado la cuenta de oficial correctamente.");
                                            loadAll();
                                        });

                            }
                    }

    vm.disableEnabledAdmin= function(officerAccount) {

            var correctMessage;
            if (officerAccount.enable==1) {
                correctMessage = "¿Está seguro que desea deshabilitar la cuenta " + officerAccount.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar la cuenta " + officerAccount.name + "?";
            }
            bootbox.confirm({

                message: correctMessage,

                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        CommonMethods.waitingMessage();
                        OfficerAccount.get({id: officerAccount.id}).$promise.then(onSuccessGetAccount);
                    }
                }
            });
        };

        function onSuccessGetAccount (result) {
            enabledDisabledAccount(result);
        }

        function enabledDisabledAccount(officerAccount){
            if(officerAccount.enable==1){
                officerAccount.enable = 0;
            } else {
                officerAccount.enable = 1;
            }
            OfficerAccount.update(officerAccount, onSuccessDisabledAdmin);
        }

        function onSuccessDisabledAdmin(data, headers) {

            User.getUserById({
                id: data.userId
            }, onSuccessGetDisabledUser);

        }
        function onSuccessGetDisabledUser(data, headers) {
            if(data.activated==1){
                data.activated = 0;
            } else {
                data.activated = 1;
            }

            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {
                toastr["success"]("Se ha modificado el estado de la cuenta correctamente.");
                bootbox.hideAll();
                loadAll();
            }
        }
    }
})();
