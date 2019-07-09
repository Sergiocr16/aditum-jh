(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroCondominiumDetailController', MacroCondominiumDetailController);

    MacroCondominiumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MacroCondominium', 'Company'];

    function MacroCondominiumDetailController($scope, $rootScope, $stateParams, previousState, entity, MacroCondominium, Company) {
        var vm = this;

        vm.macroCondominium = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:macroCondominiumUpdate', function(event, result) {
            vm.macroCondominium = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
