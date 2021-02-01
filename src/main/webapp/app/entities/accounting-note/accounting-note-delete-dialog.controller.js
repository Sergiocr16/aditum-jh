(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountingNoteDeleteController',AccountingNoteDeleteController);

    AccountingNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountingNote'];

    function AccountingNoteDeleteController($uibModalInstance, entity, AccountingNote) {
        var vm = this;

        vm.accountingNote = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountingNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
