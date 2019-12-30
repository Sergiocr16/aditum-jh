(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofDeleteController',PaymentProofDeleteController);

    PaymentProofDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentProof'];

    function PaymentProofDeleteController($uibModalInstance, entity, PaymentProof) {
        var vm = this;

        vm.paymentProof = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentProof.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
