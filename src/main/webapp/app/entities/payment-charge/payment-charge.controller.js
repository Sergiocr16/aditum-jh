(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentChargeController', PaymentChargeController);

    PaymentChargeController.$inject = ['PaymentCharge'];

    function PaymentChargeController(PaymentCharge) {

        var vm = this;

        vm.paymentCharges = [];

        loadAll();

        function loadAll() {
            PaymentCharge.query(function(result) {
                vm.paymentCharges = result;
                vm.searchQuery = null;
            });
        }
    }
})();
