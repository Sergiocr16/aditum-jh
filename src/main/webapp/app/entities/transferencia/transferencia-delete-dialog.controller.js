(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TransferenciaDeleteController',TransferenciaDeleteController);

    TransferenciaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Transferencia'];

    function TransferenciaDeleteController($uibModalInstance, entity, Transferencia) {
        var vm = this;

        vm.transferencia = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Transferencia.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
