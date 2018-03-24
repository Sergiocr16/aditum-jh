(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDialogController', EgressDialogController);

    EgressDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Egress', 'Company'];

    function EgressDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Egress, Company) {
        var vm = this;

        vm.egress = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.egress.id !== null) {
                Egress.update(vm.egress, onSaveSuccess, onSaveError);
            } else {
                Egress.save(vm.egress, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:egressUpdate', result);
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
