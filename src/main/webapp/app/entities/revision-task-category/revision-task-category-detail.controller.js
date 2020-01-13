(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionTaskCategoryDetailController', RevisionTaskCategoryDetailController);

    RevisionTaskCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RevisionTaskCategory'];

    function RevisionTaskCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, RevisionTaskCategory) {
        var vm = this;

        vm.revisionTaskCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:revisionTaskCategoryUpdate', function(event, result) {
            vm.revisionTaskCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
