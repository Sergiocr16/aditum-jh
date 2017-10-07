(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['CommonMethods','$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','MultiCompany','$rootScope','$scope','companyUser','Company','House'];

    function NavbarController (CommonMethods,$state, Auth, Principal, ProfileService, LoginService,MultiCompany,$rootScope,$scope,companyUser,Company,House) {
    var vm = this;
    $rootScope.isAuthenticated = Principal.isAuthenticated;
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
                 console.log(account)
                 switch (account.authorities[0]){
                     case "ROLE_ADMIN":
                     vm.contextLiving = "Dios de Aditum"
                     $rootScope.contextLiving = vm.contextLiving;
                     $rootScope.currentUserImage = null;
                      break;
                     case "ROLE_MANAGER":
                          if($rootScope.companyId==undefined){
                          $state.go('dashboard.selectCompany');
                          }else{
                             Company.get({id: $rootScope.companyUser.companyId},function(condo){
                              vm.contextLiving = " / "+ condo.name;
                              $rootScope.contextLiving = vm.contextLiving;
                               $rootScope.currentUserImage = companyUser.image_url;
                              if(condo.active == 0){
                              logout();
                              }
                             })
                          }
                     break;
                     case "ROLE_OFFICER":

                     break;
                      case "ROLE_USER":
                       House.get({id: $rootScope.companyUser.houseId},function(house){
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
                      vm.contextLiving = $rootScope.companyUser.name + " " +$rootScope.companyUser.lastname+" / " + $rootScope.companyUser.enterprisename;
                      $rootScope.contextLiving = vm.contextLiving;
                      $rootScope.currentUserImage = null;
                     }
                     })
                    break;
                 }
                 })
        }

        vm.getAcount();
        var subLogin = $scope.$on('authenticationSuccess',  vm.getAcount);
//   function getContextLiving(){
//       if($rootScope.companyUser!=undefined){
//            if($rootScope.companyUser.houseId == undefined){
//             Company.get({id: $rootScope.companyUser.companyId},function(condo){
//             vm.contextLiving = " / "+ condo.name;
//             $rootScope.contextLiving = vm.contextLiving;
//             if(condo.active == 0){
//              logout();
//             }
//             })
//            }else{
//             House.get({id: $rootScope.companyUser.houseId},function(house){
//                     vm.contextLiving = " / Casa " + house.housenumber;
//                     $rootScope.contextLiving = vm.contextLiving;
//             })
//            }
//       }
//   }



//   setTimeout(function(){
//
//      if(companyUser!=undefined){
//        $rootScope.companyUser = companyUser;
//        if(companyUser.companyId!=undefined){
//        $rootScope.companyId = companyUser.companyId;
//        $rootScope.currentUserImage = companyUser.image_url;
//        $rootScope.companyUser = companyUser;
//         getContextLiving();
//         }else{
//        MultiCompany.getCurrentUserCompany().then(function(data){
//         $rootScope.companyUser = data;
//         if(data!=null){
//          vm.contextLiving = $rootScope.companyUser.name + " " +$rootScope.companyUser.lastname+" / " + $rootScope.companyUser.enterprisename;
//          $rootScope.contextLiving = vm.contextLiving;
//          $rootScope.currentUserImage = null;
//         }
//         })
//
//         }
//        }else{
//         vm.contextLiving = "Dios de Aditum"
//         $rootScope.contextLiving = vm.contextLiving;
//         $rootScope.currentUserImage = null;
//        }
//   },500)


//        var subChangeState  = $rootScope.$on('$stateChangeStart', getAccount);
//        function getAccount(){
//         Principal.identity().then(function(account){
//         console.log(account)
//         switch (account.authorities[0]){
//             case "ROLE_ADMIN":
//
//                  return undefined;
//              break;
//             case "ROLE_MANAGER":
//             console.log($rootScope.companyId)
//                  if($rootScope.companyId==undefined){
//                  $state.go('dashboard.selectCompany');
//                  }
//             break;
//             case "ROLE_OFFICER":
//                return isOfficer(account.id);
//             break;
//              case "ROLE_USER":
//                return isResident(account.id);
//             break;
//            case "ROLE_RH":
//               return isRH(account.id);
//            break;
//         }
//
//
//         })
//            $rootScope.companyUser=undefined;
//            MultiCompany.getCurrentUserCompany().then(function(data){
//            if(data!=null){
//            $rootScope.companyUser = data;
//            $rootScope.companyId = data.companyId;
//            $rootScope.companyUser = $rootScope.companyUser;
//            getContextLiving();
//          if($rootScope.companyUser.image_url==undefined){
//            $rootScope.companyUser.image_url = null;
//            }else{
//           $rootScope.currentUserImage = data.image_url;
//           }
//             }else{
//             vm.contextLiving = "Dios de Aditum"
//             $rootScope.contextLiving = vm.contextLiving;
//             $rootScope.currentUserImage = null;
////              $rootScope.companyUser.image_url = null;
//             }
//
//            })
//        }

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
