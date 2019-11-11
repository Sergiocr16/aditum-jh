(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureStepsDetailController', ProcedureStepsDetailController);

    ProcedureStepsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProcedureSteps', 'Procedures'];

    function ProcedureStepsDetailController($scope, $rootScope, $stateParams, previousState, entity, ProcedureSteps, Procedures) {
        var vm = this;

        vm.procedureSteps = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:procedureStepsUpdate', function(event, result) {
            vm.procedureSteps = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
