(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChapterDetailController', ChapterDetailController);

    ChapterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chapter', 'Regulation'];

    function ChapterDetailController($scope, $rootScope, $stateParams, previousState, entity, Chapter, Regulation) {
        var vm = this;

        vm.chapter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:chapterUpdate', function(event, result) {
            vm.chapter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
