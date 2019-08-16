(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Article', 'Chapter'];

    function ArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, Article, Chapter) {
        var vm = this;

        vm.article = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
