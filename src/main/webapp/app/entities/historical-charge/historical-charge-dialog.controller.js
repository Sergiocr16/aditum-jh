(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalChargeDialogController', HistoricalChargeDialogController);

    HistoricalChargeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HistoricalCharge', 'House'];

    function HistoricalChargeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HistoricalCharge, House) {
        var vm = this;

        vm.historicalCharge = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.historicalCharge.id !== null) {
                HistoricalCharge.update(vm.historicalCharge, onSaveSuccess, onSaveError);
            } else {
                HistoricalCharge.save(vm.historicalCharge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:historicalChargeUpdate', result);
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
