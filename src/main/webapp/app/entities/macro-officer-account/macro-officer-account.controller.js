(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroOfficerAccountController', MacroOfficerAccountController);

    MacroOfficerAccountController.$inject = ['MacroOfficerAccount', 'ParseLinks', 'AlertService', 'paginationConstants','$stateParams','MacroCondominium','$rootScope'];

    function MacroOfficerAccountController(MacroOfficerAccount, ParseLinks, AlertService, paginationConstants,$stateParams,MacroCondominium,$rootScope) {

        var vm = this;
        $rootScope.active = "macro-condominium";
        vm.macroCondoId = $stateParams.id;
        vm.macroOfficerAccounts = [];
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

        function loadAll () {
            MacroCondominium.get({id : vm.macroCondoId },function(result){
                vm.macroCondo = result;
                MacroOfficerAccount.queryByMacro({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    macroId: vm.macroCondoId
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
                    vm.macroOfficerAccounts.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.macroOfficerAccounts = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
