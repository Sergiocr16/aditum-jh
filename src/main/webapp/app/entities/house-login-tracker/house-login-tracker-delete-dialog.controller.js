(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseLoginTrackerDeleteController',HouseLoginTrackerDeleteController);

    HouseLoginTrackerDeleteController.$inject = ['$uibModalInstance', 'entity', 'HouseLoginTracker'];

    function HouseLoginTrackerDeleteController($uibModalInstance, entity, HouseLoginTracker) {
        var vm = this;

        vm.houseLoginTracker = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HouseLoginTracker.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
