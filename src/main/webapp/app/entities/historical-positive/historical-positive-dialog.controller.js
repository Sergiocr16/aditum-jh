(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalPositiveDialogController', HistoricalPositiveDialogController);

    HistoricalPositiveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HistoricalPositive', 'Company', 'House'];

    function HistoricalPositiveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HistoricalPositive, Company, House) {
        var vm = this;

        vm.historicalPositive = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query();
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.historicalPositive.id !== null) {
                HistoricalPositive.update(vm.historicalPositive, onSaveSuccess, onSaveError);
            } else {
                HistoricalPositive.save(vm.historicalPositive, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:historicalPositiveUpdate', result);
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
