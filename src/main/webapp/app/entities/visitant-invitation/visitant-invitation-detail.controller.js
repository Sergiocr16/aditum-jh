(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitationDetailController', VisitantInvitationDetailController);

    VisitantInvitationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VisitantInvitation'];

    function VisitantInvitationDetailController($scope, $rootScope, $stateParams, previousState, entity, VisitantInvitation) {
        var vm = this;

        vm.visitantInvitation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:visitantInvitationUpdate', function(event, result) {
            vm.visitantInvitation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
