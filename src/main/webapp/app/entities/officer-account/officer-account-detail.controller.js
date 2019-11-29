(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountDetailController', OfficerAccountDetailController);

    OfficerAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfficerAccount', 'Company', 'User'];

    function OfficerAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, OfficerAccount, Company, User) {
        var vm = this;

        vm.officerAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:officerAccountUpdate', function(event, result) {
            vm.officerAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
