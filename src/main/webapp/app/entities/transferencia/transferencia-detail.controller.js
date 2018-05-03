(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TransferenciaDetailController', TransferenciaDetailController);

    TransferenciaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transferencia'];

    function TransferenciaDetailController($scope, $rootScope, $stateParams, previousState, entity, Transferencia) {
        var vm = this;

        vm.transferencia = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:transferenciaUpdate', function(event, result) {
            vm.transferencia = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
