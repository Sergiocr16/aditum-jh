(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDetailController', HouseDetailController);

    HouseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'House', 'Vehicule', 'Visitant', 'Note', 'Resident', 'Emergency', 'Company'];

    function HouseDetailController($scope, $rootScope, $stateParams, previousState, entity, House, Vehicule, Visitant, Note, Resident, Emergency, Company) {
        var vm = this;

        vm.house = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:houseUpdate', function(event, result) {
            vm.house = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
