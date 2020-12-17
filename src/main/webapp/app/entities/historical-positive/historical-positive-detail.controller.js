(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalPositiveDetailController', HistoricalPositiveDetailController);

    HistoricalPositiveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HistoricalPositive', 'Company', 'House'];

    function HistoricalPositiveDetailController($scope, $rootScope, $stateParams, previousState, entity, HistoricalPositive, Company, House) {
        var vm = this;

        vm.historicalPositive = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:historicalPositiveUpdate', function(event, result) {
            vm.historicalPositive = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
