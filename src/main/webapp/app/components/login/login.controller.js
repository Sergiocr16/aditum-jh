(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth'];

    function LoginController ($rootScope, $state,Principal, $timeout, Auth,rc) {


    angular.element(document).ready(function () {
         $('body').removeClass("gray");
             $('.carousel').fadeIn("slow");
                   $('.carousel').carousel({
                       intervals: 2000
                   });
        });
        var vm = this;
        vm.isIdentityResolved = Principal.isIdentityResolved;


        $rootScope.showLogin = true;

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
                   console.log(account)
                    $rootScope.showLogin = false;
                    if(account.authorities[0]=='ROLE_OFFICER'){
                     setTimeout(function(){   $state.go('main-access-door');}, 300);
                    }else if(account.authorities[0]=='ROLE_MANAGER'){
                      setTimeout(function(){  $state.go('dashboard');}, 300);

                    } else if(account.authorities[0]=='ROLE_USER'){
                    setTimeout(function(){   $state.go('residentByHouse');}, 300);

                    } else  if ($state.current.name === 'register' || $state.current.name === 'activate' ||
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
