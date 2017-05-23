(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyController', EmergencyController);

    EmergencyController.$inject = ['Emergency', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$rootScope','Principal'];

    function EmergencyController(Emergency, ParseLinks, AlertService, paginationConstants, pagingParams,$rootScope,Principal) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();

        function loadAll () {
            Emergency.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: $rootScope.companyId
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
                vm.emergencies = data;
                vm.page = pagingParams.page;
                 setTimeout(function() {
                           $("#loadingIcon").fadeOut(300);
                 }, 400)
                  setTimeout(function() {
                      $("#tableData").fadeIn('slow');
                  },700 )
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
