(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureVisitRankingDetailController', ProcedureVisitRankingDetailController);

    ProcedureVisitRankingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProcedureVisitRanking', 'ProcedureVisits'];

    function ProcedureVisitRankingDetailController($scope, $rootScope, $stateParams, previousState, entity, ProcedureVisitRanking, ProcedureVisits) {
        var vm = this;

        vm.procedureVisitRanking = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:procedureVisitRankingUpdate', function(event, result) {
            vm.procedureVisitRanking = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
