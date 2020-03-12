(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseLoginTrackerDetailController', HouseLoginTrackerDetailController);

    HouseLoginTrackerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HouseLoginTracker', 'House', 'Company'];

    function HouseLoginTrackerDetailController($scope, $rootScope, $stateParams, previousState, entity, HouseLoginTracker, House, Company) {
        var vm = this;

        vm.houseLoginTracker = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:houseLoginTrackerUpdate', function(event, result) {
            vm.houseLoginTracker = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
