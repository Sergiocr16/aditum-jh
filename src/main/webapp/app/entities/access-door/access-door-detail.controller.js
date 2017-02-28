(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorDetailController', AccessDoorDetailController);

    AccessDoorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccessDoor', 'Company', 'Watch'];

    function AccessDoorDetailController($scope, $rootScope, $stateParams, previousState, entity, AccessDoor, Company, Watch) {
        var vm = this;

        vm.accessDoor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:accessDoorUpdate', function(event, result) {
            vm.accessDoor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
