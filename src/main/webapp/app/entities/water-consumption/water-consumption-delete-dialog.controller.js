(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterConsumptionDeleteController',WaterConsumptionDeleteController);

    WaterConsumptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'WaterConsumption'];

    function WaterConsumptionDeleteController($uibModalInstance, entity, WaterConsumption) {
        var vm = this;

        vm.waterConsumption = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WaterConsumption.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
