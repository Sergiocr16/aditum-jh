(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TokenNotificationsController', TokenNotificationsController);

    TokenNotificationsController.$inject = ['TokenNotifications'];

    function TokenNotificationsController(TokenNotifications) {

        var vm = this;

        vm.tokenNotifications = [];

        loadAll();

        function loadAll() {
            TokenNotifications.query(function(result) {
                vm.tokenNotifications = result;
                vm.searchQuery = null;
            });
        }
    }
})();
