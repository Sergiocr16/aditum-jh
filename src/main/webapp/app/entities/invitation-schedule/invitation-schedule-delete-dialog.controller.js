(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InvitationScheduleDeleteController',InvitationScheduleDeleteController);

    InvitationScheduleDeleteController.$inject = ['$uibModalInstance', 'entity', 'InvitationSchedule'];

    function InvitationScheduleDeleteController($uibModalInstance, entity, InvitationSchedule) {
        var vm = this;

        vm.invitationSchedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InvitationSchedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
