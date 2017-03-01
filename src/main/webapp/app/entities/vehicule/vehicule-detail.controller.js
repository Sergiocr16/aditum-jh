(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDetailController', VehiculeDetailController);

    VehiculeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vehicule', 'House', 'Company'];

    function VehiculeDetailController($scope, $rootScope, $stateParams, previousState, entity, Vehicule, House, Company) {
        var vm = this;

        vm.vehicule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:vehiculeUpdate', function(event, result) {
            vm.vehicule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
