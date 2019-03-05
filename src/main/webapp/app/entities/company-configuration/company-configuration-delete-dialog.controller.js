(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyConfigurationDeleteController',CompanyConfigurationDeleteController);

    CompanyConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyConfiguration'];

    function CompanyConfigurationDeleteController($uibModalInstance, entity, CompanyConfiguration) {
        var vm = this;

        vm.companyConfiguration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyConfiguration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
