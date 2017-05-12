(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['CommonMethods','$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','MultiCompany','$rootScope','$scope','companyUser','Company','House'];

    function NavbarController (CommonMethods,$state, Auth, Principal, ProfileService, LoginService,MultiCompany,$rootScope,$scope,companyUser,Company,House) {
    var vm = this;
 $rootScope.isAuthenticated = Principal.isAuthenticated;

 vm.editMyInfoAsUser = function(){
    var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyUser.id)
        $state.go('residentByHouse.edit', {
            id: encryptedId
        })
 }

  vm.editMyInfoAsManager = function(){
         $state.go('admin-info-edit')
  }

   function getContextLiving(){
       if($rootScope.companyUser!=undefined){
            if($rootScope.companyUser.houseId == undefined){
             Company.get({id: $rootScope.companyUser.companyId},function(condo){
             vm.contextLiving = " / "+ condo.name;
             if(condo.active == 0){
              logout();
             }
             })
            }else{
             House.get({id: $rootScope.companyUser.houseId},function(house){
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
      $rootScope.companyUser = companyUser;
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
            $rootScope.companyUser=undefined;
            MultiCompany.getCurrentUserCompany().then(function(data){
            if(data!=null){
            $rootScope.companyUser = data;
            $rootScope.companyId = data.companyId;
            $rootScope.companyUser = $rootScope.companyUser;
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
            $rootScope.companyUser = undefined;
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
