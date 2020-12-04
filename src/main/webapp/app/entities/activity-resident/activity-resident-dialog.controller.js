(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivityResidentDialogController', ActivityResidentDialogController);

    ActivityResidentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActivityResident'];

    function ActivityResidentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActivityResident) {
        var vm = this;

        vm.activityResident = entity;
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
            if (vm.activityResident.id !== null) {
                ActivityResident.update(vm.activityResident, onSaveSuccess, onSaveError);
            } else {
                ActivityResident.save(vm.activityResident, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:activityResidentUpdate', result);
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
