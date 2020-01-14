(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDeleteController',RevisionDeleteController);

    RevisionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Revision'];

    function RevisionDeleteController($uibModalInstance, entity, Revision) {
        var vm = this;

        vm.revision = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Revision.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
