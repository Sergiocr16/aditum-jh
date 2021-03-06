(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargeDialogController', ChargeDialogController);

    ChargeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Charge', 'House', 'Payment', 'Company'];

    function ChargeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Charge, House, Payment, Company) {
        var vm = this;

        vm.charge = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();
        vm.payments = Payment.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.charge.id !== null) {
                Charge.update(vm.charge, onSaveSuccess, onSaveError);
            } else {
                Charge.save(vm.charge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:chargeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.paymentDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
