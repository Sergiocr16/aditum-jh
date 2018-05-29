(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoDialogController', PresupuestoDialogController);

    PresupuestoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Presupuesto'];

    function PresupuestoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Presupuesto) {
        var vm = this;

        vm.presupuesto = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });



        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.presupuesto.id !== null) {
                Presupuesto.update(vm.presupuesto, onSaveSuccess, onSaveError);
            } else {
                Presupuesto.save(vm.presupuesto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:presupuestoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.modificationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
