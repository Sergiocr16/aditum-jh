(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroCondominiumController', MacroCondominiumController);

    MacroCondominiumController.$inject = ['MacroCondominium', 'ParseLinks', 'AlertService', 'paginationConstants','$rootScope'];

    function MacroCondominiumController(MacroCondominium, ParseLinks, AlertService, paginationConstants,$rootScope) {

        var vm = this;
        $rootScope.active = "macro-condominium";

        vm.macroCondominiums = [];
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
            MacroCondominium.query({
                page: vm.page,
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
                for (var i = 0; i < data.length; i++) {
                    vm.macroCondominiums.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.macroCondominiums = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
