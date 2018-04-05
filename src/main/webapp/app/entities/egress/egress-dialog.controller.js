(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDialogController', EgressDialogController);

    EgressDialogController.$inject = ['CommonMethods','$timeout','$state', '$scope', '$stateParams','previousState', 'entity', 'Egress', 'Company','Principal','Proveedor','$rootScope','Banco'];

    function EgressDialogController (CommonMethods,$timeout, $state, $scope, $stateParams, previousState,  entity, Egress, Company,Principal,Proveedor,$rootScope,Banco) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.egress = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query();
        CommonMethods.validateNumbers();





        setTimeout(function(){
        Proveedor.query({companyId: $rootScope.companyId}).$promise.then(onSuccessProveedores);
        function onSuccessProveedores(data, headers) {
              vm.proveedores = data;

              Banco.query({companyId: $rootScope.companyId}).$promise.then(onSuccessBancos);
                  function onSuccessBancos(data, headers) {
                        vm.bancos = data;

                  }

               setTimeout(function() {
                            $("#loadingIcon").fadeOut(300);
                }, 400)
                   setTimeout(function() {
                       $("#new_egress_form").fadeIn('slow');
                },900 )

        }},600)




          if(vm.egress.id !== null){
            vm.title = 'Reportar pago';
            vm.button = "Reportar";
            vm.picker3 = {
                 datepickerOptions: {
                     minDate:vm.egress.date,
                     enableTime: false,
                     showWeeks: false,
                 }
             }
            Proveedor.get({id: vm.egress.proveedor},onSuccessProovedor)
            function onSuccessProovedor(proovedor, headers) {
             vm.egress.empresa = proovedor.empresa;

            }

        } else{
          vm.title = "Registrar gasto";
          vm.button = "Registrar";
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.egress.id !== null) {

                Egress.update(vm.egress, onSaveReport, onSaveError);
            } else {
            console.log(Egress)
                vm.egress.companyId = $rootScope.companyId;
                vm.egress.paymentMethod = 0;
                vm.egress.account = 0;
                Egress.save(vm.egress, onSaveSuccess, onSaveError);
            }
        }
        function onSaveReport (result) {
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress');
            toastr["success"]("Se reportó el pago correctamente");
            vm.isSaving = false;
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress');
            toastr["success"]("Se registró el gasto correctamente");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
         vm.updatePicker = function() {
            vm.picker1 = {
                 datepickerOptions: {
                     maxDate: vm.egress.expirationDate == undefined ? new Date() : vm.egress.expirationDate,
                     enableTime: false,
                     showWeeks: false,
                 }
             };
             vm.picker2 = {
                 datepickerOptions: {
                     minDate:vm.egress.date,
                     enableTime: false,
                     showWeeks: false,
                 }
             }
        }
        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.paymentDate = false;
        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
