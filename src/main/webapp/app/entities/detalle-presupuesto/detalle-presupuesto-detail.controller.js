(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DetallePresupuestoDetailController', DetallePresupuestoDetailController);

    DetallePresupuestoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DetallePresupuesto'];

    function DetallePresupuestoDetailController($scope, $rootScope, $stateParams, previousState, entity, DetallePresupuesto) {
        var vm = this;

        vm.detallePresupuesto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:detallePresupuestoUpdate', function(event, result) {
            vm.detallePresupuesto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
