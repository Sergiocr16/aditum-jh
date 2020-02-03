(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyDeleteController',EmergencyDeleteController);

    EmergencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Emergency'];

    function EmergencyDeleteController($uibModalInstance, entity, Emergency) {
        var vm = this;

        vm.emergency = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Emergency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
