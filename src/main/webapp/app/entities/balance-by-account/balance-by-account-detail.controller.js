(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceByAccountDetailController', BalanceByAccountDetailController);

    BalanceByAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BalanceByAccount'];

    function BalanceByAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, BalanceByAccount) {
        var vm = this;

        vm.balanceByAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:balanceByAccountUpdate', function(event, result) {
            vm.balanceByAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
