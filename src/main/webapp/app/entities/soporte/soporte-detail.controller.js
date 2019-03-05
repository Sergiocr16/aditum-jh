(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteDetailController', SoporteDetailController);

    SoporteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Soporte', 'House', 'Company'];

    function SoporteDetailController($scope, $rootScope, $stateParams, previousState, entity, Soporte, House, Company) {
        var vm = this;

        vm.soporte = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:soporteUpdate', function(event, result) {
            vm.soporte = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
