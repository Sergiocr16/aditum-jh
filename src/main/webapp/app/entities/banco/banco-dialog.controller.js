(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDialogController', BancoDialogController);

    BancoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity','$state', 'Banco', 'Company','$rootScope','CommonMethods'];

    function BancoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,$state, Banco, Company,$rootScope,CommonMethods) {
        var vm = this;
        CommonMethods.validateNumbers();
        vm.banco = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query();
        if (vm.banco.id !== null) {
             vm.banco.capitalInicial = parseInt(vm.banco.capitalInicial);
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.banco.id !== null) {
                Banco.update(vm.banco, onSaveSuccess, onSaveError);
            } else {
            if(vm.banco.cuentaCorriente==null){
            vm.banco.cuentaCorriente = 'No registrado'
            }
             if(vm.banco.cuentaCliente==null){
                        vm.banco.cuentaCliente = 'No registrado'
                        }
                vm.banco.companyId = $rootScope.companyId;
                 vm.banco.saldo = vm.banco.capitalInicial;
                  vm.banco.deleted = 1;
                Banco.save(vm.banco, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:bancoUpdate', result);
            $state.go('banco-configuration');
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
