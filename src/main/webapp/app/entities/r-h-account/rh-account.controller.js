(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RHAccountController', RHAccountController);

    RHAccountController.$inject = ['RHAccount', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','$rootScope','User','CommonMethods'];

    function RHAccountController(RHAccount, ParseLinks, AlertService, paginationConstants, pagingParams, Principal,$rootScope, User,CommonMethods) {
        $rootScope.active = "recursosHumanos";
        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();
        vm.disableEnabledAdmin= function(rhAccount) {
            var correctMessage;
            if (rhAccount.enable==1) {
                correctMessage = "¿Está seguro que desea deshabilitar al RH " + rhAccount.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al RH " + rhAccount.name + "?";
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
                        RHAccount.get({id: rhAccount.id}).$promise.then(onSuccessGetAdmin);
                    }
                }
            });

           function onSuccessGetAdmin (result) {
                     enabledDisabledAdmin(result);
                 }

                 function enabledDisabledAdmin(adminInfo){
                     if(adminInfo.enable==1){
                         adminInfo.enable = 0;
                     } else {
                         adminInfo.enable = 1;
                     }
                     RHAccount.update(adminInfo, onSuccessDisabledAdmin);
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
                         toastr["success"]("Se ha modificado el estado del RH correctamente.");
                         bootbox.hideAll();
                         loadAll();
                     }
                 }
                 }
        function loadAll () {
            RHAccount.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.rHAccounts = data;
                vm.page = pagingParams.page;
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
    }
})();
