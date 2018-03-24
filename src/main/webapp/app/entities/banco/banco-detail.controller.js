(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDetailController', BancoDetailController);

    BancoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Banco', 'Company'];

    function BancoDetailController($scope, $rootScope, $stateParams, previousState, entity, Banco, Company) {
        var vm = this;

        vm.banco = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:bancoUpdate', function(event, result) {
            vm.banco = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
