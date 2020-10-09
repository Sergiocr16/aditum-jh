(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BlockReservationDeleteController',BlockReservationDeleteController);

    BlockReservationDeleteController.$inject = ['$uibModalInstance', 'entity', 'BlockReservation'];

    function BlockReservationDeleteController($uibModalInstance, entity, BlockReservation) {
        var vm = this;

        vm.blockReservation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BlockReservation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
