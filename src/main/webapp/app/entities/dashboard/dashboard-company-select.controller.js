(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DashboardCompanySelectController', DashboardCompanySelectController);

    DashboardCompanySelectController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','Watch', 'AccessDoor', 'Company','MultiCompany','$rootScope','$state'];

    function DashboardCompanySelectController ($timeout, $scope, $stateParams, $uibModalInstance, Watch, AccessDoor, Company,MultiCompany, $rootScope,$state) {
        var vm = this;
        vm.clear = clear;
         $rootScope.active = "selectCondominio";
        setTimeout(function(){
        loadAll();
        },500)
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        vm.selectCompany = function(id){
        $rootScope.companyUser.companyId = id;
        $rootScope.companyId = id;
        $state.go('dashboard');
        vm.clear();
        }
        function loadAll () {
           MultiCompany.getCurrentUserCompany().then(function(data){
            if(data!=null){
            $rootScope.companyUser = data;
            vm.companies = $rootScope.companyUser.companies
            }
            })
        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }


    }
})();
