(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$rootScope','$state','CompanyConfiguration','Company', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','CommonMethods'];

    function CompanyController($rootScope,$state,CompanyConfiguration,Company, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,CommonMethods) {
        $rootScope.active = "condons";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.isReady = false;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.viewAdmins = function(companyId){
          var encryptedId = CommonMethods.encryptIdUrl(companyId)
        $state.go('admins', {
                      companyId: encryptedId
                  })
        }
       loadAll();
       function getCurrentUserCompanyId(){
            Principal.identity().then(function(account){
            console.log(account)
            })
        }

               vm.viewOfficerAccounts = function(id){
                    var encryptedId = CommonMethods.encryptIdUrl(id)
                               $state.go('officerAccounts', {
                                   companyId: encryptedId
                               })
                    }
        function loadAll () {

            Company.query({
                page: pagingParams.page - 1,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                vm.isReady = true;
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
                angular.forEach(data, function(company, key) {
                    CompanyConfiguration.getByCompanyId({companyId:company.id}).$promise.then(function (configuration) {
                        company.configuration = configuration;
                    }, onError);

                });
            console.log(data)
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        vm.getCompanyConfiguration = function(company) {
            $state.go('companyConfiguration', { id :company.configuration[0].id});
        };

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
