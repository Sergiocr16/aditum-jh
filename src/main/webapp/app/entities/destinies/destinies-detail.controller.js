(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DestiniesDetailController', DestiniesDetailController);

    DestiniesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Destinies'];

    function DestiniesDetailController($scope, $rootScope, $stateParams, previousState, entity, Destinies) {
        var vm = this;

        vm.destinies = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:destiniesUpdate', function(event, result) {
            vm.destinies = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
