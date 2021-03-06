(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofDetailController', PaymentProofDetailController);

    PaymentProofDetailController.$inject = ['$state','$localStorage','Modal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentProof', 'House'];

    function PaymentProofDetailController($state,$localStorage,Modal,$scope, $rootScope, $stateParams, previousState, entity, PaymentProof, House) {
        var vm = this;
        vm.isReady = true;
        vm.paymentProof = entity;
        vm.previousState = previousState.name;
        $rootScope.mainTitle = 'Comprobante de pago - Filial ' + vm.paymentProof.house.housenumber;
        var unsubscribe = $rootScope.$on('aditumApp:paymentProofUpdate', function(event, result) {
            vm.paymentProof = result;
        });
        $scope.$on('$destroy', unsubscribe);



        vm.generatePayment = function () {
            $localStorage.houseSelected = vm.paymentProof.house;
            $state.go('generatePayment');

        };
        vm.markAsChecked = function () {
            Modal.confirmDialog("¿Está seguro que desea marcar como revisado el comprobante de pago?", "", function () {
                vm.paymentProof.status = 2;
                Modal.showLoadingBar();
                PaymentProof.update(vm.paymentProof, onSaveSuccess, onSaveError);

            })

        };

        function onSaveSuccess() {
            Modal.toast("Se marcó el comprobante de pago como revisado correctamente.");
            Modal.hideLoadingBar();
        }

        function onSaveError() {
            Modal.hideLoadingBar();
            vm.paymentProof.status = 1;
            Modal.toast("Ocurrió un error inesperado.");
        }

    }
})();
