(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureVisitsDetailController', ProcedureVisitsDetailController);

    ProcedureVisitsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProcedureVisits', 'Procedures'];

    function ProcedureVisitsDetailController($scope, $rootScope, $stateParams, previousState, entity, ProcedureVisits, Procedures) {
        var vm = this;

        vm.procedureVisits = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:procedureVisitsUpdate', function(event, result) {
            vm.procedureVisits = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
