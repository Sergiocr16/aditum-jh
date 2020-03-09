(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterConsumptionDetailController', WaterConsumptionDetailController);

    WaterConsumptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WaterConsumption', 'House'];

    function WaterConsumptionDetailController($scope, $rootScope, $stateParams, previousState, entity, WaterConsumption, House) {
        var vm = this;

        vm.waterConsumption = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:waterConsumptionUpdate', function(event, result) {
            vm.waterConsumption = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
