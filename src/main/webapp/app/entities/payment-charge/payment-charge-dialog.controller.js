(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentChargeDialogController', PaymentChargeDialogController);

    PaymentChargeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentCharge', 'Payment'];

    function PaymentChargeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentCharge, Payment) {
        var vm = this;

        vm.paymentCharge = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.payments = Payment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paymentCharge.id !== null) {
                PaymentCharge.update(vm.paymentCharge, onSaveSuccess, onSaveError);
            } else {
                PaymentCharge.save(vm.paymentCharge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:paymentChargeUpdate', result);
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
