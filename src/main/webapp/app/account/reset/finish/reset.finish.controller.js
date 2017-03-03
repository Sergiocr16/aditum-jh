(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams','$rootScope', '$timeout', 'Auth', 'LoginService','Principal','$state'];

    function ResetFinishController ($stateParams,$rootScope, $timeout, Auth, LoginService,Principal,$state) {
       angular.element(document).ready(function () {
                       $('body').removeClass("gray");
                       $('#page-content').hide();
                  });
        var vm = this;

        vm.signIn = function(){
         $state.go('login')
        }
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.keyMissing = angular.isUndefined($stateParams.key);
        vm.confirmPassword = null;
        vm.doNotMatch = null;
        vm.error = null;
        vm.finishReset = finishReset;
        vm.login = LoginService.open;
        vm.resetAccount = {};
        vm.success = null;

        $timeout(function (){angular.element('#password').focus();});

        function finishReset() {
            vm.doNotMatch = null;
            vm.error = null;
            if (vm.resetAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                Auth.resetPasswordFinish({key: $stateParams.key, newPassword: vm.resetAccount.password}).then(function () {
                    vm.success = 'OK';
                }).catch(function () {
                    vm.success = null;
                    vm.error = 'ERROR';
                });
            }
        }
    }
})();
