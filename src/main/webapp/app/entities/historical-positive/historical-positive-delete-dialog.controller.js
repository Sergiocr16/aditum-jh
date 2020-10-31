(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalPositiveDeleteController',HistoricalPositiveDeleteController);

    HistoricalPositiveDeleteController.$inject = ['$uibModalInstance', 'entity', 'HistoricalPositive'];

    function HistoricalPositiveDeleteController($uibModalInstance, entity, HistoricalPositive) {
        var vm = this;

        vm.historicalPositive = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HistoricalPositive.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
