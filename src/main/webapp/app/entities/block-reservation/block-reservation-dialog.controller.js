(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BlockReservationDialogController', BlockReservationDialogController);

    BlockReservationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BlockReservation', 'House'];

    function BlockReservationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BlockReservation, House) {
        var vm = this;

        vm.blockReservation = entity;
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
            if (vm.blockReservation.id !== null) {
                BlockReservation.update(vm.blockReservation, onSaveSuccess, onSaveError);
            } else {
                BlockReservation.save(vm.blockReservation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:blockReservationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
