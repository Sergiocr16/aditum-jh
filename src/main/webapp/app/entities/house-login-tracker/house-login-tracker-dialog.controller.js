(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseLoginTrackerDialogController', HouseLoginTrackerDialogController);

    HouseLoginTrackerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HouseLoginTracker', 'House'];

    function HouseLoginTrackerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HouseLoginTracker, House) {
        var vm = this;

        vm.houseLoginTracker = entity;
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
            if (vm.houseLoginTracker.id !== null) {
                HouseLoginTracker.update(vm.houseLoginTracker, onSaveSuccess, onSaveError);
            } else {
                HouseLoginTracker.save(vm.houseLoginTracker, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:houseLoginTrackerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
