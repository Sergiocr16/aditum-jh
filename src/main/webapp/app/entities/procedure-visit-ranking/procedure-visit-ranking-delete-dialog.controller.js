(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureVisitRankingDeleteController',ProcedureVisitRankingDeleteController);

    ProcedureVisitRankingDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProcedureVisitRanking'];

    function ProcedureVisitRankingDeleteController($uibModalInstance, entity, ProcedureVisitRanking) {
        var vm = this;

        vm.procedureVisitRanking = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProcedureVisitRanking.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
