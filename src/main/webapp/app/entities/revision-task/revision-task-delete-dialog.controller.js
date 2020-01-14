(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionTaskDeleteController',RevisionTaskDeleteController);

    RevisionTaskDeleteController.$inject = ['$uibModalInstance', 'entity', 'RevisionTask'];

    function RevisionTaskDeleteController($uibModalInstance, entity, RevisionTask) {
        var vm = this;

        vm.revisionTask = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RevisionTask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
