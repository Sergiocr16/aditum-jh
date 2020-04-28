(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerARDetailController', OfficerARDetailController);

    OfficerARDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Officer', 'User', 'Company','Principal'];

    function OfficerARDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Officer, User, Company,Principal) {
        var vm = this;
         vm.isAuthenticated = Principal.isAuthenticated;
        vm.officer = entity;
        $rootScope.active = "officersAR";
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        var unsubscribe = $rootScope.$on('aditumApp:officerUpdate', function(event, result) {
            vm.officer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
