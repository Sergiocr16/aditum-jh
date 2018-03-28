(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargeDetailController', ChargeDetailController);

    ChargeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Charge', 'House', 'Payment'];

    function ChargeDetailController($scope, $rootScope, $stateParams, previousState, entity, Charge, House, Payment) {
        var vm = this;

        vm.charge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:chargeUpdate', function(event, result) {
            vm.charge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();