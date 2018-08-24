(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsDetailController', CommonAreaReservationsDetailController);

    CommonAreaReservationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CommonAreaReservations', 'CommonArea'];

    function CommonAreaReservationsDetailController($scope, $rootScope, $stateParams, previousState, entity, CommonAreaReservations, CommonArea) {
        var vm = this;

        vm.commonAreaReservations = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:commonAreaReservationsUpdate', function(event, result) {
            vm.commonAreaReservations = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
