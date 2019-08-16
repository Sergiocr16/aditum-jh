(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDetailController', RegulationDetailController);

    RegulationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Regulation', 'Company'];

    function RegulationDetailController($scope, $rootScope, $stateParams, previousState, entity, Regulation, Company) {
        var vm = this;

        vm.regulation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:regulationUpdate', function(event, result) {
            vm.regulation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
