(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureVisitsDeleteController',ProcedureVisitsDeleteController);

    ProcedureVisitsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProcedureVisits'];

    function ProcedureVisitsDeleteController($uibModalInstance, entity, ProcedureVisits) {
        var vm = this;

        vm.procedureVisits = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProcedureVisits.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
