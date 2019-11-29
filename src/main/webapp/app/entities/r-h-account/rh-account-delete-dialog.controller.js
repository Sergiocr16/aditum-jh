(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RHAccountDeleteController',RHAccountDeleteController);

    RHAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'RHAccount'];

    function RHAccountDeleteController($uibModalInstance, entity, RHAccount) {
        var vm = this;

        vm.rHAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RHAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
