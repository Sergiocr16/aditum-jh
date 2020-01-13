(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionController', RevisionController);

    RevisionController.$inject = ['globalCompany','Revision', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function RevisionController(globalCompany,Revision, ParseLinks, AlertService, paginationConstants) {

        var vm = this;
        vm.revisions = [];
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
            Revision.query({
                page: vm.page,
                size: vm.itemsPerPage,
                companyId: globalCompany.getId(),
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
                    vm.revisions.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.revisions = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
