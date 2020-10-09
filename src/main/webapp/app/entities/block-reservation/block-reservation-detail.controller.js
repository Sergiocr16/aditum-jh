(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BlockReservationDetailController', BlockReservationDetailController);

    BlockReservationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BlockReservation', 'House'];

    function BlockReservationDetailController($scope, $rootScope, $stateParams, previousState, entity, BlockReservation, House) {
        var vm = this;

        vm.blockReservation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:blockReservationUpdate', function(event, result) {
            vm.blockReservation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
