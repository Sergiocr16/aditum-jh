(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InvitationScheduleDialogController', InvitationScheduleDialogController);

    InvitationScheduleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InvitationSchedule'];

    function InvitationScheduleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InvitationSchedule) {
        var vm = this;

        vm.invitationSchedule = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invitationSchedule.id !== null) {
                InvitationSchedule.update(vm.invitationSchedule, onSaveSuccess, onSaveError);
            } else {
                InvitationSchedule.save(vm.invitationSchedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:invitationScheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
