(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroVisitDetailController', MacroVisitDetailController);

    MacroVisitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MacroVisit', 'MacroCondominium', 'Company', 'House'];

    function MacroVisitDetailController($scope, $rootScope, $stateParams, previousState, entity, MacroVisit, MacroCondominium, Company, House) {
        var vm = this;

        vm.macroVisit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:macroVisitUpdate', function(event, result) {
            vm.macroVisit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
