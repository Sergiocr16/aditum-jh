(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationHouseRestrictionsDetailController', ReservationHouseRestrictionsDetailController);

    ReservationHouseRestrictionsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReservationHouseRestrictions', 'House', 'CommonArea'];

    function ReservationHouseRestrictionsDetailController($scope, $rootScope, $stateParams, previousState, entity, ReservationHouseRestrictions, House, CommonArea) {
        var vm = this;

        vm.reservationHouseRestrictions = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:reservationHouseRestrictionsUpdate', function(event, result) {
            vm.reservationHouseRestrictions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
