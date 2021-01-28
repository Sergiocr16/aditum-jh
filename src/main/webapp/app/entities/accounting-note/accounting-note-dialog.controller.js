(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountingNoteDialogController', AccountingNoteDialogController);

    AccountingNoteDialogController.$inject = ['$localStorage','Modal', 'globalCompany', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountingNote', 'House', 'Company'];

    function AccountingNoteDialogController($localStorage,Modal, globalCompany, $timeout, $scope, $stateParams, $uibModalInstance, entity, AccountingNote, House, Company) {
        var vm = this;

        vm.accountingNote = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();
        moment.locale("es");
        if (vm.accountingNote.id == undefined) {
            vm.accountingNote.fixed = 0;
            vm.accountingNote.color = "accounting-note-white";
            vm.accountingNote.deleted = 0;
        }
        $timeout(function () {
            angular.element('#description').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.setColor = function (accountingNote, posColor) {
            var colors = ['accounting-note-white', 'accounting-note-red', 'accounting-note-orange', 'accounting-note-yellow', 'accounting-note-purple', 'accounting-note-blue'];
            accountingNote.color = colors[posColor];
        }

        vm.fix = function (accountingNote) {
            accountingNote.fixed = accountingNote.fixed == 1 ? 0 : 1;
        }

        function save() {
            vm.isSaving = true;
            vm.accountingNote.companyId = globalCompany.getId();
            vm.accountingNote.houseId = $localStorage.houseSelected.id;
            if (vm.accountingNote.id !== null) {
                vm.accountingNote.modificationDate = moment(new Date()).format()
                AccountingNote.update(vm.accountingNote, onSaveSuccess, onSaveError);
            } else {
                vm.accountingNote.fileName = moment(new Date()).format("MMM DD,YYYY h:mm a");
                vm.accountingNote.modificationDate = moment(new Date()).format();
                AccountingNote.save(vm.accountingNote, onSaveSuccess, onSaveError);
            }
        }

        vm.delete = function (accountingNote) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la nota?", "", function () {
                AccountingNote.delete({id: accountingNote.id},
                    function () {
                        Modal.toast("Nota eliminada correctamente.")
                        $uibModalInstance.close();
                        vm.isSaving = false;
                    });
            })
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:accountingNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.modificationDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
