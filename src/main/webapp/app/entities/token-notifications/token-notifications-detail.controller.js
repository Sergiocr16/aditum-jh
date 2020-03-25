(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TokenNotificationsDetailController', TokenNotificationsDetailController);

    TokenNotificationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TokenNotifications', 'User'];

    function TokenNotificationsDetailController($scope, $rootScope, $stateParams, previousState, entity, TokenNotifications, User) {
        var vm = this;

        vm.tokenNotifications = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:tokenNotificationsUpdate', function(event, result) {
            vm.tokenNotifications = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
