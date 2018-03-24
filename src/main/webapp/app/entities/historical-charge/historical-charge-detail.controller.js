(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalChargeDetailController', HistoricalChargeDetailController);

    HistoricalChargeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HistoricalCharge', 'House'];

    function HistoricalChargeDetailController($scope, $rootScope, $stateParams, previousState, entity, HistoricalCharge, House) {
        var vm = this;

        vm.historicalCharge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:historicalChargeUpdate', function(event, result) {
            vm.historicalCharge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
