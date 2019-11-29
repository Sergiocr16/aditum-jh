(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BitacoraAccionesDeleteController',BitacoraAccionesDeleteController);

    BitacoraAccionesDeleteController.$inject = ['$uibModalInstance', 'entity', 'BitacoraAcciones'];

    function BitacoraAccionesDeleteController($uibModalInstance, entity, BitacoraAcciones) {
        var vm = this;

        vm.bitacoraAcciones = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BitacoraAcciones.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
