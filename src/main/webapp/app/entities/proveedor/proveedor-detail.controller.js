(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProveedorDetailController', ProveedorDetailController);

    ProveedorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Proveedor', 'Company'];

    function ProveedorDetailController($scope, $rootScope, $stateParams, previousState, entity, Proveedor, Company) {
        var vm = this;

        vm.proveedor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:proveedorUpdate', function(event, result) {
            vm.proveedor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
