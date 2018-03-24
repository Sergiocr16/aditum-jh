(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargeDeleteController',ChargeDeleteController);

    ChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Charge'];

    function ChargeDeleteController($uibModalInstance, entity, Charge) {
        var vm = this;

        vm.charge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Charge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
