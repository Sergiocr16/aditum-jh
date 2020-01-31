(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationHouseRestrictionsDeleteController',ReservationHouseRestrictionsDeleteController);

    ReservationHouseRestrictionsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReservationHouseRestrictions'];

    function ReservationHouseRestrictionsDeleteController($uibModalInstance, entity, ReservationHouseRestrictions) {
        var vm = this;

        vm.reservationHouseRestrictions = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReservationHouseRestrictions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
