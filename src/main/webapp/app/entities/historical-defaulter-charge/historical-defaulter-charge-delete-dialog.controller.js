(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterChargeDeleteController',HistoricalDefaulterChargeDeleteController);

    HistoricalDefaulterChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'HistoricalDefaulterCharge'];

    function HistoricalDefaulterChargeDeleteController($uibModalInstance, entity, HistoricalDefaulterCharge) {
        var vm = this;

        vm.historicalDefaulterCharge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HistoricalDefaulterCharge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
