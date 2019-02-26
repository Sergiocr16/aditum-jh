(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivosFijosDeleteController',ActivosFijosDeleteController);

    ActivosFijosDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActivosFijos'];

    function ActivosFijosDeleteController($uibModalInstance, entity, ActivosFijos) {
        var vm = this;

        vm.activosFijos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ActivosFijos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
