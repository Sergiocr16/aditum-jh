(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope','$rootScope', 'Principal', 'LoginService', '$state','Dashboard'];

    function HomeController ($scope,$rootScope, Principal, LoginService, $state, Dashboard) {
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
        loadAll();

        function loadAll() {
            Dashboard.query({companyId : $rootScope.companyId},function(result) {
                vm.dashboard = result;
                console.log(vm.dashboard)
            });
        }
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
