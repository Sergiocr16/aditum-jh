(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProceduresDialogController', ProceduresDialogController);

    ProceduresDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Procedures', 'Company'];

    function ProceduresDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Procedures, Company) {
        var vm = this;

        vm.procedures = entity;
        vm.clear = clear;
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
            if (vm.procedures.id !== null) {
                Procedures.update(vm.procedures, onSaveSuccess, onSaveError);
            } else {
                Procedures.save(vm.procedures, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:proceduresUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
