(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofDetailController', PaymentProofDetailController);

    PaymentProofDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentProof', 'House'];

    function PaymentProofDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentProof, House) {
        var vm = this;

        vm.paymentProof = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:paymentProofUpdate', function(event, result) {
            vm.paymentProof = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
