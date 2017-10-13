(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DashboardCompanySelectController', DashboardCompanySelectController);

    DashboardCompanySelectController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','Watch', 'AccessDoor', 'Company','MultiCompany','$rootScope','$state','Auth'];

    function DashboardCompanySelectController ($timeout, $scope, $stateParams, $uibModalInstance, Watch, AccessDoor, Company,MultiCompany, $rootScope,$state,Auth) {
        var vm = this;
        vm.clear = clear;
         $rootScope.active = "selectCondominio";
        setTimeout(function(){
        loadAll();
        },500)
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
           function logout() {
                Auth.logout();
                $rootScope.companyUser = undefined;
                $state.go('home');
               $rootScope.menu = false;
                $rootScope.companyId = undefined;
                 $rootScope.showLogin = true;
                 $rootScope.inicieSesion = false;
            }
        vm.selectCompany = function(id){
        $rootScope.companyUser.companyId = id;
         $rootScope.companyId = id;
            MultiCompany.getCurrentUserCompany().then(function(data){
                   Company.get({id: $rootScope.companyUser.companyId},function(condo){
                    vm.contextLiving = " / "+ condo.name;
                    $rootScope.contextLiving = vm.contextLiving;
                    $rootScope.currentUserImage = data.image_url;
                    if(condo.active == 0){
                    logout();
                    }
                   })
            $state.go('dashboard');
           vm.clear();
         })

        }
        function loadAll () {
           MultiCompany.getCurrentUserCompany().then(function(data){
            if(data!=null){
            $rootScope.companyUser = data;
            vm.companies = data.companies;
            setTimeout(function() {
                      $("#loadingIcon").fadeOut(300);
            }, 400)
             setTimeout(function() {
                 $("#tableData").fadeIn('slow');
             },700 )
            }
            })
        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }


    }
})();
