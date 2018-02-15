(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', 'Auth','$state','$rootScope'];

    function RequestResetController ($timeout, Auth,$state,$rootScope) {
      angular.element(document).ready(function () {
                          $('body').removeClass("gray");
         $rootScope.showLogin = false;
          $("#resetRequestPanel").fadeIn(2000);
       });
        var vm = this;
        vm.error = null;
        vm.errorEmailNotExists = null;
        vm.requestReset = requestReset;
        vm.resetAccount = {};
        vm.success = null;
//        $rootScope.showLogin = false;
        vm.back = function(){
//         $('#page-content').show();
//        $rootScope.showLogin = true;
        $state.go('home');
        }
        $timeout(function (){angular.element('#email').focus();});

        function requestReset () {

            vm.error = null;
            vm.errorEmailNotExists = null;

            Auth.resetPasswordInit(vm.resetAccount.email).then(function () {
                vm.success = 'OK';
            }).catch(function (response) {
                vm.success = null;
                if (response.status === 400 && response.data === 'e-mail address not registered') {
                    vm.errorEmailNotExists = 'ERROR';
                } else {
                    vm.error = 'ERROR';
                }
            });
        }
    }
})();
