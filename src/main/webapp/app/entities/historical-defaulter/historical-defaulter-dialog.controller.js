(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterDialogController', HistoricalDefaulterDialogController);

    HistoricalDefaulterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HistoricalDefaulter', 'Company', 'House'];

    function HistoricalDefaulterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HistoricalDefaulter, Company, House) {
        var vm = this;

        vm.historicalDefaulter = entity;
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
            if (vm.historicalDefaulter.id !== null) {
                HistoricalDefaulter.update(vm.historicalDefaulter, onSaveSuccess, onSaveError);
            } else {
                HistoricalDefaulter.save(vm.historicalDefaulter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:historicalDefaulterUpdate', result);
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
