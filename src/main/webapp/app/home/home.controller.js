(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope','$rootScope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope,$rootScope, Principal, LoginService, $state) {
        var vm = this;
        vm.isInLogin = $state.includes('home');
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
