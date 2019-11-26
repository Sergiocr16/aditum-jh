(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ContractController', ContractController);

    ContractController.$inject = ['Contract', 'ParseLinks', 'AlertService', 'paginationConstants', 'CommonMethods', '$state'];

    function ContractController(Contract, ParseLinks, AlertService, paginationConstants, CommonMethods, $state) {

        var vm = this;

        vm.contracts = [];
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
            Contract.query({
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
                    vm.contracts.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.editContract = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('contract.edit', {
                id: encryptedId
            })
        };

        function reset() {
            vm.page = 0;
            vm.contracts = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
