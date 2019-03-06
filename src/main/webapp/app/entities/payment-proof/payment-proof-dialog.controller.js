(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofDialogController', PaymentProofDialogController);

    PaymentProofDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentProof', 'House'];

    function PaymentProofDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentProof, House) {
        var vm = this;

        vm.paymentProof = entity;
        vm.clear = clear;
        vm.save = save;
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paymentProof.id !== null) {
                PaymentProof.update(vm.paymentProof, onSaveSuccess, onSaveError);
            } else {
                PaymentProof.save(vm.paymentProof, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:paymentProofUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
