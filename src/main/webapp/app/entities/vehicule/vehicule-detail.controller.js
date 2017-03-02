(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDetailController', VehiculeDetailController);

    VehiculeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vehicule', 'House', 'Company','Principal'];

    function VehiculeDetailController($scope, $rootScope, $stateParams, previousState, entity, Vehicule, House, Company,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:vehiculeUpdate', function(event, result) {
            vm.vehicule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
