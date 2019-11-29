(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoDeleteController',PresupuestoDeleteController);

    PresupuestoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Presupuesto'];

    function PresupuestoDeleteController($uibModalInstance, entity, Presupuesto) {
        var vm = this;

        vm.presupuesto = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Presupuesto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
