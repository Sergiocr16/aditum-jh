(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerARDetailController', OfficerARDetailController);

    OfficerARDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfficerAR', 'Company', 'User', 'House'];

    function OfficerARDetailController($scope, $rootScope, $stateParams, previousState, entity, OfficerAR, Company, User, House) {
        var vm = this;

        vm.officerAR = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:officerARUpdate', function(event, result) {
            vm.officerAR = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
