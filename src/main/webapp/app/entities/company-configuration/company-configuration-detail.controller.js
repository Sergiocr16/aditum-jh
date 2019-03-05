(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyConfigurationDetailController', CompanyConfigurationDetailController);

    CompanyConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyConfiguration', 'Company'];

    function CompanyConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyConfiguration, Company) {
        var vm = this;

        vm.companyConfiguration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:companyConfigurationUpdate', function(event, result) {
            vm.companyConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
