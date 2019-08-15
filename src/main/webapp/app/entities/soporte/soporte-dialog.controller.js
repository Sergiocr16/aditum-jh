(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteDialogController', SoporteDialogController);

    SoporteDialogController.$inject = ['$rootScope','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Soporte', 'House', 'Company'];

    function SoporteDialogController ($rootScope,$timeout, $scope, $stateParams, $uibModalInstance, entity, Soporte, House, Company) {
        var vm = this;

        vm.soporte = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();
        $rootScope.active = "soporte";
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.soporte.id !== null) {
                Soporte.update(vm.soporte, onSaveSuccess, onSaveError);
            } else {
                Soporte.save(vm.soporte, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:soporteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
