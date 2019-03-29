(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BitacoraAccionesDetailController', BitacoraAccionesDetailController);

    BitacoraAccionesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BitacoraAcciones', 'Company'];

    function BitacoraAccionesDetailController($scope, $rootScope, $stateParams, previousState, entity, BitacoraAcciones, Company) {
        var vm = this;

        vm.bitacoraAcciones = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:bitacoraAccionesUpdate', function(event, result) {
            vm.bitacoraAcciones = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
