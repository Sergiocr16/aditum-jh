(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureCommentsDeleteController',ProcedureCommentsDeleteController);

    ProcedureCommentsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProcedureComments'];

    function ProcedureCommentsDeleteController($uibModalInstance, entity, ProcedureComments) {
        var vm = this;

        vm.procedureComments = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProcedureComments.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
