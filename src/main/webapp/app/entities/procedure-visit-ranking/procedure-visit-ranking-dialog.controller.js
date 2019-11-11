(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureVisitRankingDialogController', ProcedureVisitRankingDialogController);

    ProcedureVisitRankingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProcedureVisitRanking', 'ProcedureVisits'];

    function ProcedureVisitRankingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProcedureVisitRanking, ProcedureVisits) {
        var vm = this;

        vm.procedureVisitRanking = entity;
        vm.clear = clear;
        vm.save = save;
        vm.procedurevisits = ProcedureVisits.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.procedureVisitRanking.id !== null) {
                ProcedureVisitRanking.update(vm.procedureVisitRanking, onSaveSuccess, onSaveError);
            } else {
                ProcedureVisitRanking.save(vm.procedureVisitRanking, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:procedureVisitRankingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
