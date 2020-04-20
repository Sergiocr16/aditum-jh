(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseSecurityDirectionDetailController', HouseSecurityDirectionDetailController);

    HouseSecurityDirectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HouseSecurityDirection', 'House', 'Company'];

    function HouseSecurityDirectionDetailController($scope, $rootScope, $stateParams, previousState, entity, HouseSecurityDirection, House, Company) {
        var vm = this;

        vm.houseSecurityDirection = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:houseSecurityDirectionUpdate', function(event, result) {
            vm.houseSecurityDirection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
