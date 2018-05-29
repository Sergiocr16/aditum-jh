(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DetallePresupuestoDeleteController',DetallePresupuestoDeleteController);

    DetallePresupuestoDeleteController.$inject = ['$uibModalInstance', 'entity', 'DetallePresupuesto'];

    function DetallePresupuestoDeleteController($uibModalInstance, entity, DetallePresupuesto) {
        var vm = this;

        vm.detallePresupuesto = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DetallePresupuesto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
