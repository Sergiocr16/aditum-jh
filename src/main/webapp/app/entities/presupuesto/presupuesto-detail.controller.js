(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoDetailController', PresupuestoDetailController);

    PresupuestoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Presupuesto'];

    function PresupuestoDetailController($scope, $rootScope, $stateParams, previousState, entity, Presupuesto) {
        var vm = this;

        vm.presupuesto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:presupuestoUpdate', function(event, result) {
            vm.presupuesto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
