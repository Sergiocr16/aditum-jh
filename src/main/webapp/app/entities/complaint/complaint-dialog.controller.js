(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDialogController', ComplaintDialogController);

    ComplaintDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Complaint', 'House'];

    function ComplaintDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Complaint, House) {
        var vm = this;

        vm.complaint = entity;
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
            if (vm.complaint.id !== null) {
                Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
            } else {
                Complaint.save(vm.complaint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:complaintUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.resolutionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
