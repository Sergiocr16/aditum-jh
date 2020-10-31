(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterDeleteController',HistoricalDefaulterDeleteController);

    HistoricalDefaulterDeleteController.$inject = ['$uibModalInstance', 'entity', 'HistoricalDefaulter'];

    function HistoricalDefaulterDeleteController($uibModalInstance, entity, HistoricalDefaulter) {
        var vm = this;

        vm.historicalDefaulter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HistoricalDefaulter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
