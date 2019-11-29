(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdministrationConfigurationDeleteController',AdministrationConfigurationDeleteController);

    AdministrationConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdministrationConfiguration'];

    function AdministrationConfigurationDeleteController($uibModalInstance, entity, AdministrationConfiguration) {
        var vm = this;

        vm.administrationConfiguration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AdministrationConfiguration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
