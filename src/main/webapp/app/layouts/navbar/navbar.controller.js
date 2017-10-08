(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['CommonMethods','$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','MultiCompany','$rootScope','$scope','companyUser','Company','House'];

    function NavbarController (CommonMethods,$state, Auth, Principal, ProfileService, LoginService,MultiCompany,$rootScope,$scope,companyUser,Company,House) {
    var vm = this;
    $rootScope.isAuthenticated = Principal.isAuthenticated;
    if($rootScope.inicieSesion == undefined){
    $rootScope.inicieSesion = true;
    }
    vm.annoActual = moment(new Date()).format("YYYY");
       vm.editMyInfoAsManager = function(){
             $state.go('admin-info-edit')
       }
       vm.editMyInfoAsUser = function(){
          var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyUser.id)
          $state.go('residentByHouse.edit', {
              id: encryptedId
          })
       }
       angular.element(document).ready(function () {
                $('body').addClass("gray");
      });
     vm.viewWatch = function(){
      var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyId)
     $state.go('watch',{companyId:encryptedId})
     }

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


        vm.getAcount = function(){
                Principal.identity().then(function(account){

                 switch (account.authorities[0]){
                     case "ROLE_ADMIN":
                     vm.contextLiving = "Dios de Aditum"
                     $rootScope.contextLiving = vm.contextLiving;
                     $rootScope.currentUserImage = null;
                      break;
                     case "ROLE_MANAGER":
                      MultiCompany.getCurrentUserCompany().then(function(data){
                          if(data.companies.length>1){$rootScope.showSelectCompany = true;}
                          if(data.companies.length>1 && $rootScope.companyId == undefined){
                           $state.go('dashboard.selectCompany');
                          }else{
                           if($rootScope.companyId == undefined){
                            $rootScope.companyUser = data;
                            $rootScope.companyUser.companyId = data.companies[0].id;
                            $rootScope.companyId = data.companies[0].id;
                            }
                             Company.get({id: $rootScope.companyId},function(condo){
                              vm.contextLiving = " / "+ condo.name;
                              $rootScope.contextLiving = vm.contextLiving;
                              $rootScope.currentUserImage = data.image_url;
                              if(condo.active == 0){
                              logout();
                              }
                             })
                          }

                   })
                     break;
                     case "ROLE_OFFICER":
                    MultiCompany.getCurrentUserCompany().then(function(data){
                     $rootScope.companyUser = data;
                     $rootScope.companyId = data.companyId;
                     if(data!=null){
                      vm.contextLiving = $rootScope.companyUser.name;
                      $rootScope.contextLiving = vm.contextLiving;
                      $rootScope.currentUserImage = null;
                     }
                     })
                     break;
                      case "ROLE_USER":
                       House.get({id: companyUser.houseId},function(house){
                               vm.contextLiving = " / Casa " + house.housenumber;
                               $rootScope.contextLiving = vm.contextLiving;
                               $rootScope.companyId = companyUser.companyId;
                               $rootScope.currentUserImage = companyUser.image_url;
                               $rootScope.companyUser = companyUser;
                       })
                     break;
                    case "ROLE_RH":
                    MultiCompany.getCurrentUserCompany().then(function(data){
                     $rootScope.companyUser = data;
                     if(data!=null){
                      vm.contextLiving = " / " + data.enterprisename;
                      $rootScope.contextLiving = vm.contextLiving;
                      $rootScope.currentUserImage = null;
                     }
                     })
                    break;
                 }
                 })
        }

        setTimeout(function(){
        Principal.identity().then(function(account){
        if(account!==null){
          $rootScope.companyUser = companyUser;
           vm.getAcount();
           }
        })
        })
        var subLogin = $scope.$on('authenticationSuccess',  vm.getAcount);


        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.companyId = undefined;
             $rootScope.showLogin = true;
             $rootScope.inicieSesion = false;
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
