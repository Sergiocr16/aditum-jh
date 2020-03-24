(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigTaskDeleteController',RevisionConfigTaskDeleteController);

    RevisionConfigTaskDeleteController.$inject = ['$uibModalInstance', 'entity', 'RevisionConfigTask'];

    function RevisionConfigTaskDeleteController($uibModalInstance, entity, RevisionConfigTask) {
        var vm = this;

        vm.revisionConfigTask = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RevisionConfigTask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
