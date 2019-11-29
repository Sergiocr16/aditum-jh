(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResolutionCommentsDetailController', ResolutionCommentsDetailController);

    ResolutionCommentsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ResolutionComments', 'AdminInfo', 'Resolution'];

    function ResolutionCommentsDetailController($scope, $rootScope, $stateParams, previousState, entity, ResolutionComments, AdminInfo, Resolution) {
        var vm = this;

        vm.resolutionComments = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:resolutionCommentsUpdate', function(event, result) {
            vm.resolutionComments = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
