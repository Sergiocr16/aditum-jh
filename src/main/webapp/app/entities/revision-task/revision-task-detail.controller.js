(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionTaskDetailController', RevisionTaskDetailController);

    RevisionTaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RevisionTask', 'Revision'];

    function RevisionTaskDetailController($scope, $rootScope, $stateParams, previousState, entity, RevisionTask, Revision) {
        var vm = this;

        vm.revisionTask = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:revisionTaskUpdate', function(event, result) {
            vm.revisionTask = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
