(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceByAccountDeleteController',BalanceByAccountDeleteController);

    BalanceByAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'BalanceByAccount'];

    function BalanceByAccountDeleteController($uibModalInstance, entity, BalanceByAccount) {
        var vm = this;

        vm.balanceByAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BalanceByAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
