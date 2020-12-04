(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NotificationSendedDetailController', NotificationSendedDetailController);

    NotificationSendedDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NotificationSended', 'Company'];

    function NotificationSendedDetailController($scope, $rootScope, $stateParams, previousState, entity, NotificationSended, Company) {
        var vm = this;

        vm.notificationSended = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:notificationSendedUpdate', function(event, result) {
            vm.notificationSended = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
