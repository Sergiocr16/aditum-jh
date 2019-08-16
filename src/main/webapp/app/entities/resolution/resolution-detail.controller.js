(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResolutionDetailController', ResolutionDetailController);

    ResolutionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Resolution', 'Article', 'KeyWords', 'ArticleCategory'];

    function ResolutionDetailController($scope, $rootScope, $stateParams, previousState, entity, Resolution, Article, KeyWords, ArticleCategory) {
        var vm = this;

        vm.resolution = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:resolutionUpdate', function(event, result) {
            vm.resolution = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
