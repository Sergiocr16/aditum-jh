(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaScheduleDeleteController',CommonAreaScheduleDeleteController);

    CommonAreaScheduleDeleteController.$inject = ['$uibModalInstance', 'entity', 'CommonAreaSchedule'];

    function CommonAreaScheduleDeleteController($uibModalInstance, entity, CommonAreaSchedule) {
        var vm = this;

        vm.commonAreaSchedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CommonAreaSchedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
