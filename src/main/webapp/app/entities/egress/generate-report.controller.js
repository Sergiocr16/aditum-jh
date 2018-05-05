(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressGenerateReportController', EgressGenerateReportController);

    EgressGenerateReportController.$inject = ['$scope','$state', 'Banco','Egress', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonMethods','Proveedor','$rootScope'];

    function EgressGenerateReportController($scope,$state, Banco,Egress, ParseLinks, AlertService, paginationConstants, pagingParams,CommonMethods,Proveedor,$rootScope) {

        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.propertyName = 'id';
        vm.reverse = true;
        vm.gastosQuantity = 0;
        vm.showNoResults=false;
        vm.hideReportForm = false;
        vm.selectedProveedores = [];
        vm.selectedCampos = [];
        vm.translationCampos ={checkAll:"Selecciona todos",buttonDefaultText: "Selecciona los campos",uncheckAll: "Deseleccionar todos",selectionCount:"elementos seleccionados",dynamicButtonTextSuffix: "elementos seleccionados"}
        vm.translation ={checkAll:"Selecciona todos",buttonDefaultText: "Selecciona los proveedores",uncheckAll: "Deseleccionar todos",selectionCount: "elementos seleccionados",dynamicButtonTextSuffix: "elementos seleccionados"}
        vm.dates = {
            initial_time: undefined,
            final_time: undefined
        };
         vm.updatePicker = function() {
            vm.picker1 = {
                datepickerOptions: {
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {

                    minDate: vm.dates.initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }
         vm.updatePicker();

        setTimeout(function(){loadProveedors();},800)
        function loadProveedors() {
            vm.companyName = $rootScope.companyName;
            Proveedor.query({companyId: $rootScope.companyId}).$promise.then(onSuccessProveedores);
            function onSuccessProveedores(data, headers) {
                vm.proveedores = data;
                vm.proveedoresMultiSelect= []
                 angular.forEach(vm.proveedores,function(proveedor,key){
                    var proveedorToshow = {}
                    proveedorToshow.label = proveedor.empresa;
                    proveedorToshow.id = proveedor.id;
                    vm.proveedoresMultiSelect.push(proveedorToshow)
                 })
                loadCampos();
            }

        }
        function loadCampos () {
            vm.camposMultiSelect = [{ id: 0, label: 'Concepto',attr:'concept' }, { id: 1, label: 'Folio',attr:'folio' }, { id: 2, label: 'Fecha de cobro' ,attr:'date'}, { id: 3, label: 'Fecha de vencimiento' ,attr:'expirationDate' }, { id: 4, label: 'Fecha pago realizado' ,attr:'paymentDate'}, { id: 5, label: '# Factura',attr:'billNumber'  } , { id:6, label: 'Referencia' ,attr:'reference' }  , { id: 7, label: 'Cuenta' ,attr:'account' } , { id: 8, label: 'Estado' ,attr:'state' } , { id: 9, label: 'Monto' ,attr:'total' }]
         loadAccounts();
        }
         function loadAccounts () {
              Banco.query({companyId: $rootScope.companyId}).$promise.then(onSuccessBancos);
         }

       function onSuccessBancos(data, headers) {
          vm.bancos = data;
       }

         function formatEgresses() {
             moment.locale('es');
             angular.forEach( vm.egresses,function(value,key){
              if(value.paymentDate == null || value.paymentDate == undefined ){
                  value.paymentDate = "No pagado";
                  value.account = "No pagado"
              }else{
               value.paymentDate = moment(value.paymentDate).format('LL')
              }
              angular.forEach(  vm.bancos,function(banco,key){
                if(banco.id ==value.account){
                  value.account = banco.beneficiario;
                }

              })

              if(value.billNumber == null || value.billNumber == undefined ||value.billNumber == '' ){
               value.billNumber = 'Sin Registrar'
              }
              if(value.folio == null || value.folio == undefined ){
               value.folio = 'Sin Registrar'
              }
              if(value.reference == null || value.reference == undefined ){
                value.reference = 'Sin Registrar'
               }
               value.date = moment(value.date).format('LL')
               value.expirationDate = moment(value.expirationDate).format('LL')
               if(value.state == 1){
                  value.state = 'Pendiente'
                } else if(value.state == 2){
                  value.state = 'Pagado'
                }else if(value.state == 3){
                  value.state = 'Vencido'
                }formatearNumero
                value.montoInt =  parseInt(value.total)
                value.total = '₡'+formatearNumero(value.total);
             })
             showResults()
         }

         function formatearNumero(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
             var rgx = /(\d+)(\d{3})/;
             while (rgx.test(x1)) {
                     x1 = x1.replace(rgx, '$1' + ',' + '$2');
             }
             return x1 + x2;
         }


         function onError(error) {
             AlertService.error(error.data.message);
         }

         vm.generateReport = function(){
               vm.gastosQuantity = 0;
            $("#reportResults").fadeOut(0);
            setTimeout(function() {
                $("#loadingIcon2").fadeIn(100);
            }, 200)
            Egress.findBetweenCobroDatesByCompany({
                 initial_time: moment(vm.dates.initial_time).format(),
                 final_time: moment(vm.dates.final_time).format(),
                 companyId: $rootScope.companyId,
            }).$promise.then(onSuccess);

            function onSuccess(data) {
                vm.egresses = data;
                formatEgresses()
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function showResults(){
             vm.reportResult = []
             vm.companyName = $rootScope.companyName;
              vm.reportInitialtime = vm.dates.initial_time;
              vm.reportFinaltime = vm.dates.final_time;
                 angular.forEach(vm.selectedProveedores,function(proveedorSelected,key){
                 var objectProveedor ={};
                    angular.forEach(vm.proveedores,function(proveedor,keyProveedor){
                          if(proveedorSelected.id==proveedor.id){
                                objectProveedor = {id: vm.proveedores[keyProveedor].id, nombre: vm.proveedores[keyProveedor].empresa,gastos: [],montoTotal:0};
                                angular.forEach(vm.egresses,function(egress,key){
                                      if(objectProveedor.id == egress.proveedor){
                                        var egressInfo = {}
                                         angular.forEach(vm.selectedCampos,function(selectedCampo,key){
                                              egressInfo[vm.camposMultiSelect[selectedCampo.id].attr] = egress[vm.camposMultiSelect[selectedCampo.id].attr]

                                         })
                                        if(egressInfo.total!=undefined || egressInfo.total!=null){
                                        objectProveedor.montoTotal += egress.montoInt
                                        console.log(egressInfo.total)

                                        }
                                        objectProveedor.gastos.push(egressInfo);
                                        vm.gastosQuantity = vm.gastosQuantity + 1;
                                      }

                                })
                                     objectProveedor.montoTotal = objectProveedor.montoTotal + '';
                                     objectProveedor.montoTotal = formatearNumero(objectProveedor.montoTotal)
                          }

                     })
                       vm.reportResult.push(objectProveedor)
                 })
              setTimeout(function() {
                           $("#loadingIcon2").fadeOut(300);
               }, 400)
                  setTimeout(function() {
                  console.log(vm.gastosQuantity)
                      $("#reportResults").fadeIn('slow');
               },900 )
               if(vm.gastosQuantity>0){
                    vm.showNoResults = false
                    vm.hideReportForm = true;
               }else{
               vm.showNoResults = true}
        }


        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
