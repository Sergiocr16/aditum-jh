(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','pdfDelegate'];

    function LoginController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,pdfDelegate) {




    angular.element(document).ready(function () {
         $('body').removeClass("gray");
             $('.carousel').fadeIn("slow");
                   $('.carousel').carousel({
                       intervals: 2000
                   });
        });
        var vm = this;
        vm.isIdentityResolved = Principal.isIdentityResolved;
    vm.pdfUrl = 'content/manuals/manualusuario.pdf';
    vm.loadNewFile = function(url) {
      pdfDelegate
        .$getByHandle('my-pdf-container')
        .load(url);
    };
        vm.anno = moment(new Date()).format('YYYY')
        $rootScope.showLogin = true;
        $rootScope.showSelectCompany = false;
        vm.isChangingPassword = $state.includes('finishReset');
        vm.isResetPassword = $state.includes('requestReset');
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.authenticationError = false;
        vm.cancel = cancel;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
//        vm.register = register;
        vm.rememberMe = true;
        vm.requestResetPassword = requestResetPassword;
        vm.username = null;

        $timeout(function (){angular.element('#username').focus();});

        function cancel () {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
//            $uibModalInstance.dismiss('cancel');
        }

        function login (event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function (data) {

                vm.authenticationError = false;
                   Principal.identity().then(function(account){
                    $rootScope.menu = true;
                    $rootScope.showLogin = false;
                    $rootScope.inicieSesion = true;
                switch (account.authorities[0]){
                     case "ROLE_ADMIN":
                       setTimeout(function(){   $state.go('company');}, 300);
                      break;
                     case "ROLE_MANAGER":
                       MultiCompany.getCurrentUserCompany().then(function(data){
                       $rootScope.companyUser = data;
                      if(data.companies.length>1 && $rootScope.companyId == undefined){
                      $rootScope.showSelectCompany = true;
                           setTimeout(function(){$state.go('dashboard.selectCompany');},300)
                      }else{
                      $rootScope.showSelectCompany = false;
                       $rootScope.companyId = data.companies[0].id;
                       console.log(data.companies[0].id)
                        setTimeout(function(){$state.go('dashboard');},300)
                      }
                     })
                     break;
                     case "ROLE_OFFICER":
                     setTimeout(function(){   $state.go('main-access-door');}, 300);
                     break;
                      case "ROLE_USER":
                        MultiCompany.getCurrentUserCompany().then(function(data){
                         House.get({id: data.houseId},function(house){
                        vm.contextLiving = " / Casa " + house.housenumber;
                                                       $rootScope.contextLiving = vm.contextLiving;
                                                       $rootScope.companyId = data.companyId;
                                                       $rootScope.currentUserImage = data.image_url;
                                                       $rootScope.companyUser = data;
                         setTimeout(function(){   $state.go('residentByHouse');}, 300);
                        })
     })
                     break;
                    case "ROLE_RH":
                     setTimeout(function(){  $rootScope.active = "company-rh";  $state.go('company-rh');}, 300);
                    break;
                 }
                  if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                      $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                      $state.go('home');    }
                })


                $rootScope.$broadcast('authenticationSuccess');
                $('body').addClass("gray");
                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is successful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }
            }).catch(function (a) {
                vm.authenticationError = true;
                toastr["error"]("Credenciales inválidos o cuenta deshabilitada.");
            });
        }

//        function register () {
////            $uibModalInstance.dismiss('cancel');
//            $state.go('register');
//        }

        function requestResetPassword () {
            $state.go('requestReset');
        }
    }
})();
