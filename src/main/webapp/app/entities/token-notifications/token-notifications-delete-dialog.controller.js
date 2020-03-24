(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TokenNotificationsDeleteController',TokenNotificationsDeleteController);

    TokenNotificationsDeleteController.$inject = ['$uibModalInstance', 'entity', 'TokenNotifications'];

    function TokenNotificationsDeleteController($uibModalInstance, entity, TokenNotifications) {
        var vm = this;

        vm.tokenNotifications = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TokenNotifications.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
