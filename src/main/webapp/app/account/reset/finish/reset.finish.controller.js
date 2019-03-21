(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams', '$rootScope', '$timeout', 'Auth', 'LoginService', 'Principal', '$state'];

    function ResetFinishController($stateParams, $rootScope, $timeout, Auth, LoginService, Principal, $state) {
        angular.element(document).ready(function () {
            $('#content').hide();
        });
        $rootScope.showLogin = false;
        var vm = this;
        $rootScope.menu = false;
        vm.signIn = function () {
            Auth.logout();
            $('#content').show();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
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

        $timeout(function () {
            angular.element('#password').focus();
        });

        function finishReset() {
            vm.doNotMatch = null;
            vm.error = null;
            if (vm.resetAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                Auth.resetPasswordFinish({
                    key: $stateParams.key,
                    newPassword: vm.resetAccount.password
                }).then(function () {
                    vm.success = 'OK';
                    Auth.logout();
                }).catch(function () {
                    vm.success = null;
                    vm.error = 'ERROR';
                });
            }
        }
    }
})();
