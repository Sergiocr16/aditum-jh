(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterChargeDialogController', HistoricalDefaulterChargeDialogController);

    HistoricalDefaulterChargeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HistoricalDefaulterCharge', 'HistoricalDefaulter'];

    function HistoricalDefaulterChargeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HistoricalDefaulterCharge, HistoricalDefaulter) {
        var vm = this;

        vm.historicalDefaulterCharge = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.historicaldefaulters = HistoricalDefaulter.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.historicalDefaulterCharge.id !== null) {
                HistoricalDefaulterCharge.update(vm.historicalDefaulterCharge, onSaveSuccess, onSaveError);
            } else {
                HistoricalDefaulterCharge.save(vm.historicalDefaulterCharge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:historicalDefaulterChargeUpdate', result);
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
