(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmailConfigurationDeleteController',EmailConfigurationDeleteController);

    EmailConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmailConfiguration'];

    function EmailConfigurationDeleteController($uibModalInstance, entity, EmailConfiguration) {
        var vm = this;

        vm.emailConfiguration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EmailConfiguration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
