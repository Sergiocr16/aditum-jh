(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeDeleteController',CustomChargeTypeDeleteController);

    CustomChargeTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomChargeType'];

    function CustomChargeTypeDeleteController($uibModalInstance, entity, CustomChargeType) {
        var vm = this;

        vm.customChargeType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomChargeType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
