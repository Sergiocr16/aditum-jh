(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TransferenciaDialogController', TransferenciaDialogController);

    TransferenciaDialogController.$inject = ['$state','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transferencia','Banco','$rootScope'];

    function TransferenciaDialogController ($state,$timeout, $scope, $stateParams, $uibModalInstance, entity, Transferencia,Banco,$rootScope) {
        var vm = this;

        vm.transferencia = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        setTimeout(function(){
          Banco.query({companyId: $rootScope.companyId}).$promise.then(onSuccessBancos);
          function onSuccessBancos(data, headers) {
                vm.bancos = data;

          }

        },700)
        function clear () {

            $uibModalInstance.dismiss('cancel');
        }

        function save () {

            vm.isSaving = true;
            if(vm.transferencia.cuentaOrigen.id == vm.transferencia.cuentaDestino.id){
               toastr["error"]("No puede seleccionar la misma cuenta");
               vm.isSaving = false;
            } else {
                  Banco.get({id : vm.transferencia.cuentaOrigen.id}).$promise.then(onSuccesTransferenciaOrigen);

            }
//
//            vm.transferencia.idCompany =  $rootScope.companyId;
//            if (vm.transferencia.id !== null) {
//                Transferencia.update(vm.transferencia, onSaveSuccess, onSaveError);
//            } else {
//            vm.transferencia.idBancoOrigen = vm.transferencia.cuentaOrigen.id;
//            vm.transferencia.cuentaOrigen = vm.transferencia.cuentaOrigen.beneficiario;
//            vm.transferencia.idBancoDestino = vm.transferencia.cuentaDestino.id;
//            vm.transferencia.cuentaDestino = vm.transferencia.cuentaDestino.beneficiario;
//            Transferencia.save(vm.transferencia, onSaveSuccess, onSaveError);
//            }
        }
         function onSuccesTransferenciaOrigen (result) {
            result.saldo = parseInt(result.saldo) - parseInt(vm.transferencia.monto);
            Banco.update(result, updateCuentaOrigen, onSaveError);
        }
        function updateCuentaOrigen (result) {
            Banco.get({id : vm.transferencia.cuentaDestino.id}).$promise.then(onSuccesTransferenciaDestino);
        }
        function onSuccesTransferenciaDestino (result) {
            result.saldo = parseInt(result.saldo) + parseInt(vm.transferencia.monto);
            Banco.update(result, updateCuentaDestino, onSaveError);
        }
        function updateCuentaDestino (result) {
            vm.transferencia.idCompany =  $rootScope.companyId;
            vm.transferencia.idBancoOrigen = vm.transferencia.cuentaOrigen.id;
            vm.transferencia.cuentaOrigen = vm.transferencia.cuentaOrigen.beneficiario;
            vm.transferencia.idBancoDestino = vm.transferencia.cuentaDestino.id;
            vm.transferencia.cuentaDestino = vm.transferencia.cuentaDestino.beneficiario;
            Transferencia.save(vm.transferencia, onSaveSuccess, onSaveError);
        }
        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:transferenciaUpdate', result);
              $state.go('banco-detail({id:$stateParams.id})')
            $uibModalInstance.close(result);

            vm.isSaving = false;
        }
         vm.picker3 = {
                      datepickerOptions: {
                          enableTime: false,
                          showWeeks: false,
                      }
                  }
        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();