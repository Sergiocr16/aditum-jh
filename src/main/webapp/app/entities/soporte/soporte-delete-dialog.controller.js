(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteDeleteController',SoporteDeleteController);

    SoporteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Soporte'];

    function SoporteDeleteController($uibModalInstance, entity, Soporte) {
        var vm = this;

        vm.soporte = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Soporte.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
