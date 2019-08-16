(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResolutionCommentsDialogController', ResolutionCommentsDialogController);

    ResolutionCommentsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ResolutionComments', 'AdminInfo', 'Resolution'];

    function ResolutionCommentsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ResolutionComments, AdminInfo, Resolution) {
        var vm = this;

        vm.resolutionComments = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.admininfos = AdminInfo.query();
        vm.resolutions = Resolution.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resolutionComments.id !== null) {
                ResolutionComments.update(vm.resolutionComments, onSaveSuccess, onSaveError);
            } else {
                ResolutionComments.save(vm.resolutionComments, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:resolutionCommentsUpdate', result);
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
