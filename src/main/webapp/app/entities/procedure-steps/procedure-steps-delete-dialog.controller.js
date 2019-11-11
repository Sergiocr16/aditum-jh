(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureStepsDeleteController',ProcedureStepsDeleteController);

    ProcedureStepsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProcedureSteps'];

    function ProcedureStepsDeleteController($uibModalInstance, entity, ProcedureSteps) {
        var vm = this;

        vm.procedureSteps = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProcedureSteps.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
