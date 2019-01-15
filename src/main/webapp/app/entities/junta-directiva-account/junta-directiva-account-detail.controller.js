(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JuntaDirectivaAccountDetailController', JuntaDirectivaAccountDetailController);

    JuntaDirectivaAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JuntaDirectivaAccount', 'Company', 'User'];

    function JuntaDirectivaAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, JuntaDirectivaAccount, Company, User) {
        var vm = this;

        vm.juntaDirectivaAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:juntaDirectivaAccountUpdate', function(event, result) {
            vm.juntaDirectivaAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
