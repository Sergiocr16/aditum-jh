(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ContractDetailController', ContractDetailController);

    ContractDetailController.$inject = ['Modal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contract', 'Company'];

    function ContractDetailController(Modal,$scope, $rootScope, $stateParams, previousState, entity, Contract, Company) {
        var vm = this;

        vm.contract = entity;
        vm.previousState = previousState.name;
        vm.isReady = true;
        var unsubscribe = $rootScope.$on('aditumApp:contractUpdate', function(event, result) {
            vm.contract = result;
        });
        $scope.$on('$destroy', unsubscribe);
        $rootScope.mainTitle = "Detalle del contrato";
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
    }
})();
