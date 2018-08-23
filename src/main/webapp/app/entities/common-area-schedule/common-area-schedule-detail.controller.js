(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaScheduleDetailController', CommonAreaScheduleDetailController);

    CommonAreaScheduleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CommonAreaSchedule'];

    function CommonAreaScheduleDetailController($scope, $rootScope, $stateParams, previousState, entity, CommonAreaSchedule) {
        var vm = this;

        vm.commonAreaSchedule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:commonAreaScheduleUpdate', function(event, result) {
            vm.commonAreaSchedule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
