(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$state','CompanyConfiguration','Company', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal'];

    function CompanyController($state,CompanyConfiguration,Company, ParseLinks, AlertService, paginationConstants, pagingParams,Principal) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

       loadAll();
       function getCurrentUserCompanyId(){
            Principal.identity().then(function(account){
            console.log(account)
            })
        }
        function loadAll () {

            Company.query({
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
                vm.companies = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        vm.getCompanyConfiguration = function(idCompany) {
            CompanyConfiguration.getByCompanyId({companyId:idCompany}).$promise.then(onSuccessCompany, onError);
        }
        function onSuccessCompany (data) {
            angular.forEach(data, function(configuration, key) {
                vm.companyConfiguration = configuration;
                $state.go('companyConfiguration', { 'id':configuration.id});
            });

        }

        function onError () {

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
