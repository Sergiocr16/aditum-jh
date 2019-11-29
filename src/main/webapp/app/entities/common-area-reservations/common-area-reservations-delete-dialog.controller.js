(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsDeleteController',CommonAreaReservationsDeleteController);

    CommonAreaReservationsDeleteController.$inject = ['$uibModalInstance', 'entity', 'CommonAreaReservations'];

    function CommonAreaReservationsDeleteController($uibModalInstance, entity, CommonAreaReservations) {
        var vm = this;

        vm.commonAreaReservations = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CommonAreaReservations.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
