(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountingNoteDialogController', AccountingNoteDialogController);

    AccountingNoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountingNote', 'House', 'Company'];

    function AccountingNoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccountingNote, House, Company) {
        var vm = this;

        vm.accountingNote = entity;
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
            if (vm.accountingNote.id !== null) {
                AccountingNote.update(vm.accountingNote, onSaveSuccess, onSaveError);
            } else {
                AccountingNote.save(vm.accountingNote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:accountingNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.modificationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
