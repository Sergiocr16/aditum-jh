(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintCommentDialogController', ComplaintCommentDialogController);

    ComplaintCommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ComplaintComment', 'Resident', 'AdminInfo'];

    function ComplaintCommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ComplaintComment, Resident, AdminInfo) {
        var vm = this;

        vm.complaintComment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.residents = Resident.query();
        vm.admininfos = AdminInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.complaintComment.id !== null) {
                ComplaintComment.update(vm.complaintComment, onSaveSuccess, onSaveError);
            } else {
                ComplaintComment.save(vm.complaintComment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:complaintCommentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.editedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
