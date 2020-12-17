(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentChargeDetailController', PaymentChargeDetailController);

    PaymentChargeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentCharge', 'Payment'];

    function PaymentChargeDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentCharge, Payment) {
        var vm = this;

        vm.paymentCharge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:paymentChargeUpdate', function(event, result) {
            vm.paymentCharge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
