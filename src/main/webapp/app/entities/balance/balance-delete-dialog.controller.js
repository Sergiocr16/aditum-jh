(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceDeleteController',BalanceDeleteController);

    BalanceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Balance'];

    function BalanceDeleteController($uibModalInstance, entity, Balance) {
        var vm = this;

        vm.balance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Balance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
