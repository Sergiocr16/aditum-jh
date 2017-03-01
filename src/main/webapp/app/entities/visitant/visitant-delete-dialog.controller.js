(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDeleteController',VisitantDeleteController);

    VisitantDeleteController.$inject = ['$uibModalInstance', 'entity', 'Visitant'];

    function VisitantDeleteController($uibModalInstance, entity, Visitant) {
        var vm = this;

        vm.visitant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Visitant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
