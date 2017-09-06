(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountDeleteController',OfficerAccountDeleteController);

    OfficerAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfficerAccount'];

    function OfficerAccountDeleteController($uibModalInstance, entity, OfficerAccount) {
        var vm = this;

        vm.officerAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfficerAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
