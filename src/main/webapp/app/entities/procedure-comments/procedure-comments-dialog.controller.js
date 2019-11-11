(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureCommentsDialogController', ProcedureCommentsDialogController);

    ProcedureCommentsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProcedureComments', 'Procedures', 'AdminInfo'];

    function ProcedureCommentsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProcedureComments, Procedures, AdminInfo) {
        var vm = this;

        vm.procedureComments = entity;
        vm.clear = clear;
        vm.save = save;
        vm.procedures = Procedures.query();
        vm.admininfos = AdminInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.procedureComments.id !== null) {
                ProcedureComments.update(vm.procedureComments, onSaveSuccess, onSaveError);
            } else {
                ProcedureComments.save(vm.procedureComments, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:procedureCommentsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
