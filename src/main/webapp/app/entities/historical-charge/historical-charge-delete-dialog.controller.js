(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalChargeDeleteController',HistoricalChargeDeleteController);

    HistoricalChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'HistoricalCharge'];

    function HistoricalChargeDeleteController($uibModalInstance, entity, HistoricalCharge) {
        var vm = this;

        vm.historicalCharge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HistoricalCharge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
