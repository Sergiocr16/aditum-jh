(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmailConfigurationDetailController', EmailConfigurationDetailController);

    EmailConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EmailConfiguration', 'Company'];

    function EmailConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, EmailConfiguration, Company) {
        var vm = this;

        vm.emailConfiguration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:emailConfigurationUpdate', function(event, result) {
            vm.emailConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
