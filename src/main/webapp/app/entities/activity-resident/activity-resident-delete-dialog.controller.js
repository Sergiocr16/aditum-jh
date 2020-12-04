(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivityResidentDeleteController',ActivityResidentDeleteController);

    ActivityResidentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActivityResident'];

    function ActivityResidentDeleteController($uibModalInstance, entity, ActivityResident) {
        var vm = this;

        vm.activityResident = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ActivityResident.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
