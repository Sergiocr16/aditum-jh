(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyDetailController', EmergencyDetailController);

    EmergencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Emergency', 'Company', 'House'];

    function EmergencyDetailController($scope, $rootScope, $stateParams, previousState, entity, Emergency, Company, House) {
        var vm = this;

        vm.emergency = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:emergencyUpdate', function(event, result) {
            vm.emergency = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
