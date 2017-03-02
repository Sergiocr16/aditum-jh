(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JhiConfigurationController', JhiConfigurationController);

    JhiConfigurationController.$inject = ['$filter','JhiConfigurationService','Principal'];

    function JhiConfigurationController (filter,JhiConfigurationService,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.allConfiguration = null;
        vm.configuration = null;

        JhiConfigurationService.get().then(function(configuration) {
            vm.configuration = configuration;
        });
        JhiConfigurationService.getEnv().then(function (configuration) {
            vm.allConfiguration = configuration;
        });
    }
})();
