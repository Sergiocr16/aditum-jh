(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProcedureCommentsDetailController', ProcedureCommentsDetailController);

    ProcedureCommentsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProcedureComments', 'Procedures', 'AdminInfo'];

    function ProcedureCommentsDetailController($scope, $rootScope, $stateParams, previousState, entity, ProcedureComments, Procedures, AdminInfo) {
        var vm = this;

        vm.procedureComments = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:procedureCommentsUpdate', function(event, result) {
            vm.procedureComments = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
