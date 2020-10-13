(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeDetailController', CustomChargeTypeDetailController);

    CustomChargeTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CustomChargeType', 'Company'];

    function CustomChargeTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, CustomChargeType, Company) {
        var vm = this;

        vm.customChargeType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:customChargeTypeUpdate', function(event, result) {
            vm.customChargeType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
