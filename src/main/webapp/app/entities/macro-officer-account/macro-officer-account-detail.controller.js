(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroOfficerAccountDetailController', MacroOfficerAccountDetailController);

    MacroOfficerAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MacroOfficerAccount', 'User', 'MacroCondominium'];

    function MacroOfficerAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, MacroOfficerAccount, User, MacroCondominium) {
        var vm = this;

        vm.macroOfficerAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:macroOfficerAccountUpdate', function(event, result) {
            vm.macroOfficerAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
