(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProveedorDeleteController',ProveedorDeleteController);

    ProveedorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Proveedor'];

    function ProveedorDeleteController($uibModalInstance, entity, Proveedor) {
        var vm = this;

        vm.proveedor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Proveedor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
