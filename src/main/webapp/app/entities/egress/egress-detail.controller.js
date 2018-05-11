(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDetailController', EgressDetailController);

    EgressDetailController.$inject = ['$scope', '$state','$rootScope', '$stateParams', 'previousState', 'entity', 'Egress', 'Company','Proveedor','Banco','Principal'];

    function EgressDetailController($scope,$state, $rootScope, $stateParams, previousState, entity, Egress, Company,Proveedor,Banco,Principal) {
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
        console.log( vm.egress)
        $scope.$on('$destroy', unsubscribe);
//      vm.egress.account = null;
//      vm.egress.paymentMethod = null;
//      vm.egress.paymentDate = null;
           if(vm.egress.folio == null || vm.egress.folio == 'undefined' ){
              vm.egress.folio = 'Sin Registrar'
             }
             if(vm.egress.billNumber == null || vm.egress.billNumber == 'undefined' || vm.egress.billNumber == '' ){
               vm.egress.billNumber = 'Sin Registrar'
              }

         if(vm.egress.paymentDate == null || vm.egress.paymentDate == undefined || vm.egress.paymentDate == 'No pagado' ){
             vm.egress.paymentDate = "No pagado";
         } else{
              Banco.get({id: vm.egress.account},onSuccessAccount)
         }
          function save () {
           var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
          var expirationTime = new Date(vm.egress.expirationDate).getTime();
          if (currentTime <= expirationTime) {
              vm.egress.state = 1;
           } else {
             vm.egress.state = 3;
           }
           if(vm.egress.paymentDate !== null || vm.egress.paymentDate == 'undefined' ){
                vm.egress.state = 2;
            }
            Egress.update(vm.egress, onSaveSuccess, onSaveError);


        }
        function onSaveError () {
                    vm.isSaving = false;
                }
        vm.confirmReportPayment = function(){
                   bootbox.confirm({
                         message: '<div class="text-center gray-font font-15"><h4 style="margin-bottom:30px;">¿Está seguro que desea reportar el pago de este egreso?</h4><h5 class="bold">Una vez registrada esta información no se podrá editar</h5></div>',
                            buttons: {
                                confirm: {
                                    label: 'Aceptar',
                                    className: 'btn-success'
                                },
                                cancel: {
                                    label: 'Cancelar',
                                    className: 'btn-danger'
                                }
                            },
                            callback: function(result) {

                                if (result) {
                                       save()

                                }else{
                                    vm.isSaving = false;

                                }
                            }
                        });
                }
         function onSaveSuccess (result) {
             angular.forEach(vm.bancos,function(banco,key){
                 if(banco.id == vm.egress.account){
                   banco.saldo = banco.saldo - vm.egress.total;
                   console.log(banco)
                   Banco.update(banco, onAccountBalanceSuccess, onSaveError);

                 }

             })


        }
        function  onAccountBalanceSuccess (result) {
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress');
            toastr["success"]("Se reportó el pago correctamente");
            vm.isSaving = false;
        }


        setTimeout(function(){Proveedor.get({id: vm.egress.proveedor},onSuccessProovedor)},700)

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
