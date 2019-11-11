(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProceduresDeleteController',ProceduresDeleteController);

    ProceduresDeleteController.$inject = ['$uibModalInstance', 'entity', 'Procedures'];

    function ProceduresDeleteController($uibModalInstance, entity, Procedures) {
        var vm = this;

        vm.procedures = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Procedures.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
