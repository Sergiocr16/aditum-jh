(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BitacoraAccionesDialogController', BitacoraAccionesDialogController);

    BitacoraAccionesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BitacoraAcciones', 'Company'];

    function BitacoraAccionesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BitacoraAcciones, Company) {
        var vm = this;

        vm.bitacoraAcciones = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bitacoraAcciones.id !== null) {
                BitacoraAcciones.update(vm.bitacoraAcciones, onSaveSuccess, onSaveError);
            } else {
                BitacoraAcciones.save(vm.bitacoraAcciones, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:bitacoraAccionesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.ejecutionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
