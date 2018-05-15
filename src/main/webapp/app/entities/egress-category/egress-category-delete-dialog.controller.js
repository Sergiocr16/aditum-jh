(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressCategoryDeleteController',EgressCategoryDeleteController);

    EgressCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'EgressCategory'];

    function EgressCategoryDeleteController($uibModalInstance, entity, EgressCategory) {
        var vm = this;

        vm.egressCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EgressCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
