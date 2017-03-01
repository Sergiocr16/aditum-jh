(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NoteDialogController', NoteDialogController);

    NoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Note', 'House', 'Company'];

    function NoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Note, House, Company) {
        var vm = this;

        vm.note = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.note.id !== null) {
                Note.update(vm.note, onSaveSuccess, onSaveError);
            } else {
                Note.save(vm.note, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:noteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationdate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
