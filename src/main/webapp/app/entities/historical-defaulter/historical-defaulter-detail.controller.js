(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterDetailController', HistoricalDefaulterDetailController);

    HistoricalDefaulterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HistoricalDefaulter', 'Company', 'House'];

    function HistoricalDefaulterDetailController($scope, $rootScope, $stateParams, previousState, entity, HistoricalDefaulter, Company, House) {
        var vm = this;

        vm.historicalDefaulter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:historicalDefaulterUpdate', function(event, result) {
            vm.historicalDefaulter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
