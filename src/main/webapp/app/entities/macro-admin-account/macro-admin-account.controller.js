(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroAdminAccountController', MacroAdminAccountController);

    MacroAdminAccountController.$inject = ['MacroAdminAccount', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope','$stateParams','MacroCondominium'];

    function MacroAdminAccountController(MacroAdminAccount, ParseLinks, AlertService, paginationConstants, $rootScope,$stateParams, MacroCondominium) {

        var vm = this;
        $rootScope.active = "macro-condominium";
        vm.macroCondoId = $stateParams.id;
        vm.isReady = false;
        vm.macroAdminAccounts = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll() {
            MacroCondominium.get({id: vm.macroCondoId}, function (result) {
                vm.macroCondo = result;
                MacroAdminAccount.queryByMacro({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
                    , macroId: vm.macroCondoId
                }, onSuccess, onError);
            });

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
                for (var i = 0; i < data.length; i++) {
                    vm.macroAdminAccounts.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.macroAdminAccounts = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
