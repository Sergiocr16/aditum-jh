(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDetailController', EgressDetailController);

    EgressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Egress', 'Company','Proveedor','Banco','Principal'];

    function EgressDetailController($scope, $rootScope, $stateParams, previousState, entity, Egress, Company,Proveedor,Banco,Principal) {
        var vm = this;
        vm.save = save;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.companies = Company.query();
        vm.egress = entity;
        vm.previousState = previousState.name;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        var unsubscribe = $rootScope.$on('aditumApp:egressUpdate', function(event, result) {
            vm.egress = result;
        });

        $scope.$on('$destroy', unsubscribe);
         var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
         var expirationTime = new Date(vm.egress.expirationDate).getTime();
         if(vm.egress.paymentDate == null || vm.egress.paymentDate == 'undefined' ){
             vm.egress.paymentDate = "No pagado";
             if (currentTime <= expirationTime) {

                vm.egress.state = 1;
             } else {
               vm.egress.state = 3;
             }


         } else{
              Banco.get({id: vm.egress.account},onSuccessAccount)
              vm.egress.state = 2;
         }
          function save () {
            vm.isSaving = true;

                Egress.update(vm.egress, onSaveSuccess, onSaveError);

        }
         function onSaveSuccess (result) {
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress');
            toastr["success"]("Se reportó el pago correctamente");
            vm.isSaving = false;
        }

        Proveedor.get({id: vm.egress.proveedor},onSuccessProovedor)
        function onSuccessProovedor(proovedor, headers) {
             vm.egress.empresa = proovedor.empresa;
            Banco.query({companyId: $rootScope.companyId}).$promise.then(onSuccessBancos);

         }
         function onSuccessBancos(data, headers) {

           setTimeout(function() {
                              $("#loadingIcon").fadeOut(300);
                      }, 400)
                         setTimeout(function() {
                             $("#new_egress_form").fadeIn('slow');
                      },900 )
              vm.bancos = data;
         }
         function onSuccessAccount(account, headers) {
          vm.egress.banco = account.beneficiario;

          }

          vm.datePickerOpenStatus.paymentDate = false;

          function openCalendar (date) {
              vm.datePickerOpenStatus[date] = true;
          }




        }


})();
