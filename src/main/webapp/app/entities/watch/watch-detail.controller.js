(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchDetailController', WatchDetailController);

    WatchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Watch', 'AccessDoor', 'Company'];

    function WatchDetailController($scope, $rootScope, $stateParams, previousState, entity, Watch, AccessDoor, Company) {
        var vm = this;

        vm.watch = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:watchUpdate', function(event, result) {
            vm.watch = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
