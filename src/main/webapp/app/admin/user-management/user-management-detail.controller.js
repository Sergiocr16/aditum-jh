(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['$stateParams', 'User','Principal'];

    function UserManagementDetailController ($stateParams, User,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.load = load;
        vm.user = {};

        vm.load($stateParams.login);

        function load (login) {
            User.get({login: login}, function(result) {
                vm.user = result;
            });
        }
    }
})();
