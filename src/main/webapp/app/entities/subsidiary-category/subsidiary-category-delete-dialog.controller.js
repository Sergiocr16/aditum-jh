(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryCategoryDeleteController',SubsidiaryCategoryDeleteController);

    SubsidiaryCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'SubsidiaryCategory'];

    function SubsidiaryCategoryDeleteController($uibModalInstance, entity, SubsidiaryCategory) {
        var vm = this;

        vm.subsidiaryCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SubsidiaryCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
