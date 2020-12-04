(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NotificationSendedDeleteController',NotificationSendedDeleteController);

    NotificationSendedDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationSended'];

    function NotificationSendedDeleteController($uibModalInstance, entity, NotificationSended) {
        var vm = this;

        vm.notificationSended = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NotificationSended.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
