(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','MultiCompany','$rootScope','$scope','companyUser','Company','House'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService,MultiCompany,$rootScope,$scope,companyUser,Company,House) {
    var vm = this;
 $rootScope.isAuthenticated = Principal.isAuthenticated;
   function getContextLiving(){
       if(vm.currentUser!=undefined){
            if(vm.currentUser.houseId == undefined){
             Company.get({id: vm.currentUser.companyId},function(condo){
             vm.contextLiving = " / "+ condo.name;
             if(condo.active == 0){
              logout();
             }
             })
            }else{
             House.get({id: vm.currentUser.houseId},function(house){
                     vm.contextLiving = " / Casa " + house.housenumber;
             })
            }
       }
   }




    if(companyUser!=undefined){
     $rootScope.companyUser = companyUser;
     $rootScope.companyId = companyUser.companyId;
     $rootScope.currentUserImage = companyUser.image;
     $rootScope.currentUserImageContentType = companyUser.imageContentType;
      vm.currentUser = companyUser;
      getContextLiving();
     }else{
      vm.contextLiving = "Dios de Aditum"
     }
        var vm = this;
        angular.element(document).ready(function () {
                 $('body').addClass("gray");
       });


        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

         var subLogin = $scope.$on('authenticationSuccess', getAccount);
//         var subChangeState  = $rootScope.$on('$stateChangeStart', getAccount);
        function getAccount(){
            vm.currentUser=undefined;
            MultiCompany.getCurrentUserCompany().then(function(data){
            if(data!=null){
            $rootScope.companyUser = data;
            $rootScope.companyId = data.companyId;
            vm.currentUser = data;
            getContextLiving();
            $rootScope.currentUserImage = data.image;
            $rootScope.currentUserImageContentType = data.imageContentType;
             }else{
             vm.contextLiving = "Dios de Aditum"
              $rootScope.currentUserImage = undefined;
              $rootScope.currentUserImageContentType = undefined;
             }

            })
        }

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            vm.currentUser = undefined;
             $rootScope.showLogin = true;
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

//        $scope.$on('$destroy', subChangeState);
        $scope.$on('$destroy', subLogin);
    }
})();
