(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JhiConfigurationController', JhiConfigurationController);

    JhiConfigurationController.$inject = ['$filter','JhiConfigurationService','Principal','$rootScope'];

    function JhiConfigurationController (filter,JhiConfigurationService,Principal, $rootScope) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.allConfiguration = null;
        vm.configuration = null;
        $rootScope.active = "configuration";
        JhiConfigurationService.get().then(function(configuration) {
            vm.configuration = configuration;
        });
        JhiConfigurationService.getEnv().then(function (configuration) {
            vm.allConfiguration = configuration;
        });
    }
})();
