(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroAdminAccountDetailController', MacroAdminAccountDetailController);

    MacroAdminAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MacroAdminAccount', 'MacroCondominium', 'User'];

    function MacroAdminAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, MacroAdminAccount, MacroCondominium, User) {
        var vm = this;

        vm.macroAdminAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:macroAdminAccountUpdate', function(event, result) {
            vm.macroAdminAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
