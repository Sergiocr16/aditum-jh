(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorDeleteController',AccessDoorDeleteController);

    AccessDoorDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccessDoor'];

    function AccessDoorDeleteController($uibModalInstance, entity, AccessDoor) {
        var vm = this;

        vm.accessDoor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccessDoor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
