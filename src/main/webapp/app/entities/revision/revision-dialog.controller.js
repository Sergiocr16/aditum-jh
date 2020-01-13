(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDialogController', RevisionDialogController);

    RevisionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Revision', 'RevisionTask', 'Company'];

    function RevisionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Revision, RevisionTask, Company) {
        var vm = this;

        vm.revision = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.revisiontasks = RevisionTask.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.revision.id !== null) {
                Revision.update(vm.revision, onSaveSuccess, onSaveError);
            } else {
                Revision.save(vm.revision, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:revisionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.executionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
