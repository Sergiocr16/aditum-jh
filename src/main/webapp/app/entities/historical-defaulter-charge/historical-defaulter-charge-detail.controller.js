(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterChargeDetailController', HistoricalDefaulterChargeDetailController);

    HistoricalDefaulterChargeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HistoricalDefaulterCharge', 'HistoricalDefaulter'];

    function HistoricalDefaulterChargeDetailController($scope, $rootScope, $stateParams, previousState, entity, HistoricalDefaulterCharge, HistoricalDefaulter) {
        var vm = this;

        vm.historicalDefaulterCharge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:historicalDefaulterChargeUpdate', function(event, result) {
            vm.historicalDefaulterCharge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
