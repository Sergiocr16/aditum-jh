(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigTaskDetailController', RevisionConfigTaskDetailController);

    RevisionConfigTaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RevisionConfigTask', 'RevisionTaskCategory', 'RevisionConfig'];

    function RevisionConfigTaskDetailController($scope, $rootScope, $stateParams, previousState, entity, RevisionConfigTask, RevisionTaskCategory, RevisionConfig) {
        var vm = this;

        vm.revisionConfigTask = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:revisionConfigTaskUpdate', function(event, result) {
            vm.revisionConfigTask = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
