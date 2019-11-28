(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryTypeDeleteController',SubsidiaryTypeDeleteController);

    SubsidiaryTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'SubsidiaryType'];

    function SubsidiaryTypeDeleteController($uibModalInstance, entity, SubsidiaryType) {
        var vm = this;

        vm.subsidiaryType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SubsidiaryType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
