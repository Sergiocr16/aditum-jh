(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceByAccountDialogController', BalanceByAccountDialogController);

    BalanceByAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BalanceByAccount'];

    function BalanceByAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BalanceByAccount) {
        var vm = this;

        vm.balanceByAccount = entity;
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
            if (vm.balanceByAccount.id !== null) {
                BalanceByAccount.update(vm.balanceByAccount, onSaveSuccess, onSaveError);
            } else {
                BalanceByAccount.save(vm.balanceByAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:balanceByAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
