(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDeleteController',ResidentDeleteController);

    ResidentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resident'];

    function ResidentDeleteController($uibModalInstance, entity, Resident) {
        var vm = this;

        vm.resident = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resident.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
