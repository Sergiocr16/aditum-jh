(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationHouseRestrictionsDialogController', ReservationHouseRestrictionsDialogController);

    ReservationHouseRestrictionsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReservationHouseRestrictions', 'House', 'CommonArea'];

    function ReservationHouseRestrictionsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReservationHouseRestrictions, House, CommonArea) {
        var vm = this;

        vm.reservationHouseRestrictions = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();
        vm.commonareas = CommonArea.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reservationHouseRestrictions.id !== null) {
                ReservationHouseRestrictions.update(vm.reservationHouseRestrictions, onSaveSuccess, onSaveError);
            } else {
                ReservationHouseRestrictions.save(vm.reservationHouseRestrictions, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:reservationHouseRestrictionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastTimeReservation = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
