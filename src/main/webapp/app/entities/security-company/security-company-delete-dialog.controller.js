(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SecurityCompanyDeleteController',SecurityCompanyDeleteController);

    SecurityCompanyDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityCompany'];

    function SecurityCompanyDeleteController($uibModalInstance, entity, SecurityCompany) {
        var vm = this;

        vm.securityCompany = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SecurityCompany.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
