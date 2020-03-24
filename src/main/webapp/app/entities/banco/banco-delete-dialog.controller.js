(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDeleteController',BancoDeleteController);

    BancoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Banco'];

    function BancoDeleteController($uibModalInstance, entity, Banco) {
        var vm = this;

        vm.banco = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Banco.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
