(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ContractDetailController', ContractDetailController);

    ContractDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contract', 'Company'];

    function ContractDetailController($scope, $rootScope, $stateParams, previousState, entity, Contract, Company) {
        var vm = this;

        vm.contract = entity;
        vm.previousState = previousState.name;
        vm.isReady = true;
        var unsubscribe = $rootScope.$on('aditumApp:contractUpdate', function(event, result) {
            vm.contract = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
