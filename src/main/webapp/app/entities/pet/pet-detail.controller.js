(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PetDetailController', PetDetailController);

    PetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pet', 'House', 'Company'];

    function PetDetailController($scope, $rootScope, $stateParams, previousState, entity, Pet, House, Company) {
        var vm = this;

        vm.pet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:petUpdate', function(event, result) {
            vm.pet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
