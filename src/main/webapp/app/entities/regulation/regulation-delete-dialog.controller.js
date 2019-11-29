(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDeleteController',RegulationDeleteController);

    RegulationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Regulation'];

    function RegulationDeleteController($uibModalInstance, entity, Regulation) {
        var vm = this;

        vm.regulation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Regulation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
