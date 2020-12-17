(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentChargeDeleteController',PaymentChargeDeleteController);

    PaymentChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentCharge'];

    function PaymentChargeDeleteController($uibModalInstance, entity, PaymentCharge) {
        var vm = this;

        vm.paymentCharge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentCharge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
