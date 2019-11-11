(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureVisitsDialogController', ProcedureVisitsDialogController);

    ProcedureVisitsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProcedureVisits', 'Procedures'];

    function ProcedureVisitsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProcedureVisits, Procedures) {
        var vm = this;

        vm.procedureVisits = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.procedures = Procedures.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.procedureVisits.id !== null) {
                ProcedureVisits.update(vm.procedureVisits, onSaveSuccess, onSaveError);
            } else {
                ProcedureVisits.save(vm.procedureVisits, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:procedureVisitsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.visitDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
