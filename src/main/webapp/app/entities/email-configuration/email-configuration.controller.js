(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmailConfigurationController', EmailConfigurationController);

    EmailConfigurationController.$inject = ['EmailConfiguration'];

    function EmailConfigurationController(EmailConfiguration) {

        var vm = this;

        vm.emailConfigurations = [];

        loadAll();

        function loadAll() {
            EmailConfiguration.query(function(result) {
                vm.emailConfigurations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
