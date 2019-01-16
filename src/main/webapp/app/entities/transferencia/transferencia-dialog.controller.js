(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TransferenciaDialogController', TransferenciaDialogController);

    TransferenciaDialogController.$inject = ['$state', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transferencia', 'Banco', '$rootScope', 'globalCompany', 'Modal'];

    function TransferenciaDialogController($state, $timeout, $scope, $stateParams, $uibModalInstance, entity, Transferencia, Banco, $rootScope, globalCompany, Modal) {
        var vm = this;

        vm.transferencia = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $rootScope.mainTitle = "Transferir fondos"
        var transferInfo = {};
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);

        function onSuccessBancos(data, headers) {
            vm.bancos = data;
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }


        function save() {

            if (vm.transferencia.cuentaOrigen.id == vm.transferencia.cuentaDestino.id) {
                Modal.toast("No puede seleccionar la misma cuenta");
                vm.isSaving = false;
            } else {
                confirmTransferencia();
            }

            function confirmTransferencia() {
              Modal.confirmDialog("¿Está seguro que desea realizar la transferencia?","Una vez registrada esta información no se podrá editar",function(){
                  Modal.showLoadingBar();
                  Banco.get({id: vm.transferencia.cuentaOrigen.id}).$promise.then(onSuccesTransferenciaOrigen);
              });
            }
        }

        function onSuccesTransferenciaOrigen(result) {
            result.saldo = parseInt(result.saldo) - parseInt(vm.transferencia.monto);
            Banco.update(result, updateCuentaOrigen, onSaveError);
        }

        function updateCuentaOrigen(result) {
            Banco.get({id: vm.transferencia.cuentaDestino.id}).$promise.then(onSuccesTransferenciaDestino);
        }

        function onSuccesTransferenciaDestino(result) {
            result.saldo = parseInt(result.saldo) + parseInt(vm.transferencia.monto);
            Banco.update(result, updateCuentaDestino, onSaveError);
        }

        function updateCuentaDestino(result) {
            vm.transferencia.idCompany = globalCompany.getId();
            vm.transferencia.idBancoOrigen = vm.transferencia.cuentaOrigen.id;
            vm.transferencia.cuentaOrigen = vm.transferencia.cuentaOrigen.beneficiario;
            vm.transferencia.idBancoDestino = vm.transferencia.cuentaDestino.id;
            vm.transferencia.cuentaDestino = vm.transferencia.cuentaDestino.beneficiario;
            Transferencia.save(vm.transferencia, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:transferenciaUpdate', result);
            $state.go('banco-configuration');
            $uibModalInstance.close(result);
            vm.isSaving = false;
            setTimeout(function(){
                Modal.toast("Transferencia realizada exitosamente.");
                Modal.hideLoadingBar();
            },700)
        }

        vm.picker3 = {
            datepickerOptions: {
                enableTime: false,
                showWeeks: false,
            }
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
