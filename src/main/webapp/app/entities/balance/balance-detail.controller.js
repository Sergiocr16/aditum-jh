(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceDetailController', BalanceDetailController);

    BalanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Balance', 'House'];

    function BalanceDetailController($scope, $rootScope, $stateParams, previousState, entity, Balance, House) {
        var vm = this;

        vm.balance = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:balanceUpdate', function(event, result) {
            vm.balance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
