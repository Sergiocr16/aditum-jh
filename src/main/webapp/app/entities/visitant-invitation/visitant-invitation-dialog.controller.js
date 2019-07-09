(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitationDialogController', VisitantInvitationDialogController);

    VisitantInvitationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VisitantInvitation'];

    function VisitantInvitationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VisitantInvitation) {
        var vm = this;

        vm.visitantInvitation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.visitantInvitation.id !== null) {
                VisitantInvitation.update(vm.visitantInvitation, onSaveSuccess, onSaveError);
            } else {
                VisitantInvitation.save(vm.visitantInvitation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:visitantInvitationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.invitationstartingtime = false;
        vm.datePickerOpenStatus.invitationlimittime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
