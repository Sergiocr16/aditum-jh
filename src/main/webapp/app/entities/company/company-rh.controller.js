(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyRHController', CompanyRHController);

    CompanyRHController.$inject = ['$rootScope','$state','MultiCompany','Company', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','CommonMethods'];

    function CompanyRHController($rootScope,$state,MultiCompany,Company, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,CommonMethods) {
        $rootScope.active = "company-rh";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;

        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        setTimeout(function(){
        loadAll();

        },500)

        vm.viewAdmins = function(companyId){
          var encryptedId = CommonMethods.encryptIdUrl(companyId)

        $state.go('admins-rh', {
                      companyId: encryptedId
                  })
        }
        vm.viewWatch = function(companyId){
         var encryptedId = CommonMethods.encryptIdUrl(companyId)
        $state.go('turno',{companyId:encryptedId})
        }

          vm.reportarTurno = function(companyId){
          console.log(companyId)
            var encryptedId = CommonMethods.encryptIdUrl(companyId)
                 $state.go("company-rh.newWatch", {companyId: encryptedId})
          }

       function getCurrentUserCompanyId(){
            Principal.identity().then(function(account){
            console.log(account)
            })
        }
        function loadAll () {
           MultiCompany.getCurrentUserCompany().then(function(data){
            if(data!=null){
            $rootScope.companyUser = data;
            vm.companies = $rootScope.companyUser.companies
             setTimeout(function() {
             $("#loadingIcon").fadeOut(300);
                    }, 400)
                     setTimeout(function() {
                         $("#tableData").fadeIn('slow');
                     },700 )
            }
            })
        }

        vm.editCompany = function(id){
        var encryptedId = CommonMethods.encryptIdUrl(id)
                   $state.go('company-rh.edit', {
                       id: encryptedId
                   })
        }

        vm.viewOfficerAccounts = function(id){
            var encryptedId = CommonMethods.encryptIdUrl(id)
                       $state.go('officerAccounts', {
                           companyId: encryptedId
                       })
            }

        vm.getCompanyConfiguration = function(idCompany) {
            CompanyConfiguration.getByCompanyId({companyId:idCompany}).$promise.then(onSuccessCompany, onError);
        }
        function onSuccessCompany (data) {
        console.log(data)
            angular.forEach(data, function(configuration, key) {
                vm.companyConfiguration = configuration;
                $state.go('companyConfiguration', { id :configuration.id});
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
