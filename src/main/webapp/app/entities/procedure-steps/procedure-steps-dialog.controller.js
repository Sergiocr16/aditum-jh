(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureStepsDialogController', ProcedureStepsDialogController);

    ProcedureStepsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProcedureSteps', 'Procedures'];

    function ProcedureStepsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProcedureSteps, Procedures) {
        var vm = this;

        vm.procedureSteps = entity;
        vm.clear = clear;
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
            if (vm.procedureSteps.id !== null) {
                ProcedureSteps.update(vm.procedureSteps, onSaveSuccess, onSaveError);
            } else {
                ProcedureSteps.save(vm.procedureSteps, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:procedureStepsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
