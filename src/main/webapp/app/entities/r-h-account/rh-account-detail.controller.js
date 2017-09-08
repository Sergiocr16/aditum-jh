(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RHAccountDetailController', RHAccountDetailController);

    RHAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RHAccount', 'User', 'Company','Principal'];

    function RHAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, RHAccount, User, Company, Principal) {
        var vm = this;

        vm.rHAccount = entity;
        vm.previousState = previousState.name;
         vm.isAuthenticated = Principal.isAuthenticated;

        var unsubscribe = $rootScope.$on('aditumApp:rHAccountUpdate', function(event, result) {
            vm.rHAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
