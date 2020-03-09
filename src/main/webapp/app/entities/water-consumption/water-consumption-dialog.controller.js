(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterConsumptionDialogController', WaterConsumptionDialogController);

    WaterConsumptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WaterConsumption', 'House'];

    function WaterConsumptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WaterConsumption, House) {
        var vm = this;

        vm.waterConsumption = entity;
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
            if (vm.waterConsumption.id !== null) {
                WaterConsumption.update(vm.waterConsumption, onSaveSuccess, onSaveError);
            } else {
                WaterConsumption.save(vm.waterConsumption, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.recordDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
