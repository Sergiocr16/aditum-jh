(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InvitationScheduleDetailController', InvitationScheduleDetailController);

    InvitationScheduleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InvitationSchedule'];

    function InvitationScheduleDetailController($scope, $rootScope, $stateParams, previousState, entity, InvitationSchedule) {
        var vm = this;

        vm.invitationSchedule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:invitationScheduleUpdate', function(event, result) {
            vm.invitationSchedule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
