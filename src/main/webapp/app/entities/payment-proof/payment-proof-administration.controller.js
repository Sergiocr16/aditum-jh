(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofAdministrationController', PaymentProofAdministrationController);

    PaymentProofAdministrationController.$inject = ['AlertService','$rootScope'];

    function PaymentProofAdministrationController(AlertService,$rootScope) {
        $rootScope.active = "paymentProof";
        $rootScope.mainTitle = "Comprobantes de pago";
    }
})();
