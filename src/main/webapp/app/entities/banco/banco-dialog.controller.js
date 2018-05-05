(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDialogController', BancoDialogController);

    BancoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Banco', 'Company'];

    function BancoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Banco, Company) {
        var vm = this;

        vm.banco = entity;
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
            if (vm.banco.id !== null) {
            console.log(vm.banco)
                Banco.update(vm.banco, onSaveSuccess, onSaveError);
            } else {
                Banco.save(vm.banco, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:bancoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaCapitalInicial = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
