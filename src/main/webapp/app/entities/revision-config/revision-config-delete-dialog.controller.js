(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigDeleteController',RevisionConfigDeleteController);

    RevisionConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'RevisionConfig'];

    function RevisionConfigDeleteController($uibModalInstance, entity, RevisionConfig) {
        var vm = this;

        vm.revisionConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RevisionConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
