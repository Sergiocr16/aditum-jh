(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDetailController', EgressDetailController);

    EgressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Egress', 'Company'];

    function EgressDetailController($scope, $rootScope, $stateParams, previousState, entity, Egress, Company) {
        var vm = this;

        vm.egress = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:egressUpdate', function(event, result) {
            vm.egress = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
