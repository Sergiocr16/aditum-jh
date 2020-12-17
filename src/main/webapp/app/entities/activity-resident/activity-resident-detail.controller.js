(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivityResidentDetailController', ActivityResidentDetailController);

    ActivityResidentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ActivityResident'];

    function ActivityResidentDetailController($scope, $rootScope, $stateParams, previousState, entity, ActivityResident) {
        var vm = this;

        vm.activityResident = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:activityResidentUpdate', function(event, result) {
            vm.activityResident = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
