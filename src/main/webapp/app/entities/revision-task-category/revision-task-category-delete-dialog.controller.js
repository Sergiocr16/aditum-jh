(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionTaskCategoryDeleteController',RevisionTaskCategoryDeleteController);

    RevisionTaskCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'RevisionTaskCategory'];

    function RevisionTaskCategoryDeleteController($uibModalInstance, entity, RevisionTaskCategory) {
        var vm = this;

        vm.revisionTaskCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RevisionTaskCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
