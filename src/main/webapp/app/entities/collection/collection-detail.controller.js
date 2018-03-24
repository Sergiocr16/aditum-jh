(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CollectionDetailController', CollectionDetailController);

    CollectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Collection', 'House'];

    function CollectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Collection, House) {
        var vm = this;

        vm.collection = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:collectionUpdate', function(event, result) {
            vm.collection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
