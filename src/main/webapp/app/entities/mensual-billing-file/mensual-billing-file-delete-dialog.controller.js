(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualBillingFileDeleteController',MensualBillingFileDeleteController);

    MensualBillingFileDeleteController.$inject = ['$uibModalInstance', 'entity', 'MensualBillingFile'];

    function MensualBillingFileDeleteController($uibModalInstance, entity, MensualBillingFile) {
        var vm = this;

        vm.mensualBillingFile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MensualBillingFile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
