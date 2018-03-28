(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdministrationConfigurationDetailController', AdministrationConfigurationDetailController);

    AdministrationConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AdministrationConfiguration', 'Company'];

    function AdministrationConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, AdministrationConfiguration, Company) {
        var vm = this;

        vm.administrationConfiguration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:administrationConfigurationUpdate', function(event, result) {
            vm.administrationConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
