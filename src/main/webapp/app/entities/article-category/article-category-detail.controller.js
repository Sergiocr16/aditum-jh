(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleCategoryDetailController', ArticleCategoryDetailController);

    ArticleCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ArticleCategory'];

    function ArticleCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ArticleCategory) {
        var vm = this;

        vm.articleCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:articleCategoryUpdate', function(event, result) {
            vm.articleCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
