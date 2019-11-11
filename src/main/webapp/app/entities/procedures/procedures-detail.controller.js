(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProceduresDetailController', ProceduresDetailController);

    ProceduresDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Procedures', 'Company'];

    function ProceduresDetailController($scope, $rootScope, $stateParams, previousState, entity, Procedures, Company) {
        var vm = this;

        vm.procedures = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:proceduresUpdate', function(event, result) {
            vm.procedures = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
