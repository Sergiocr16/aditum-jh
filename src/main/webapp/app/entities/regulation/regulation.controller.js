(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationController', RegulationController);

    RegulationController.$inject = ['$rootScope', '$localStorage', 'Company', '$state', 'Regulation', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function RegulationController($rootScope, $localStorage, Company, $state, Regulation, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "regulation";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        loadAll();

        function loadAll() {
            Regulation.query({
                page: pagingParams.page - 1,
                size: 500,
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
                vm.regulations = data;
                vm.page = pagingParams.page;
                vm.isReady = true;
                angular.forEach(data, function (regulation, key) {
                    if (regulation.companyId != null) {
                        Company.get({id: parseInt(regulation.companyId)}, function (company) {
                            regulation.company = company;
                        })
                    }
                });

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


        vm.watchChapters = function (regulation) {
            $localStorage.regulationSelected = regulation;
            $state.go('chapter')
        }

    }
})();
