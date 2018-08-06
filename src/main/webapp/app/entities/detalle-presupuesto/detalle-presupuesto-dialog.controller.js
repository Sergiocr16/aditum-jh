(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DetallePresupuestoDialogController', DetallePresupuestoDialogController);

    DetallePresupuestoDialogController.$inject = ['$localStorage','$state','Presupuesto','CommonMethods','$timeout', '$scope', '$stateParams', 'entity', 'DetallePresupuesto','$rootScope','EgressCategory'];

    function DetallePresupuestoDialogController ($localStorage,$state,Presupuesto,CommonMethods,$timeout, $scope, $stateParams, entity, DetallePresupuesto,$rootScope,EgressCategory) {
        var vm = this;
           $rootScope.active = "presupuestos";
        vm.presupuesto = {};
        vm.detallePresupuesto = entity;
        vm.save = save;
        vm.mantenimientoValues = [];
        vm.extraordinariaValues = [];
        vm.areasComunesValues = [];
        vm.otrosIngresosValues = [];
        vm.budgetYearsToSelect = [];
        vm.totalIngressByMonth = [];
        vm.totalEgressByMonth = [];
        var invalidInputs = 0;
           vm.expanding = false;
        var inputsFullQuantity = 0;
        for(var i=1;i<=12;i++){
            var mantenimientoValue = {month:i,valuePerMonth:"0"}
            var extraordinariaValue = {month:i,valuePerMonth:"0"}
            var areasComunesValue = {month:i,valuePerMonth:"0"}
            var otrosIngresosValue = {month:i,valuePerMonth:"0"}
            vm.mantenimientoValues.push(mantenimientoValue);
            vm.extraordinariaValues.push(extraordinariaValue);
            vm.areasComunesValues.push(areasComunesValue);
            vm.otrosIngresosValues.push(otrosIngresosValue);
            var month1 = {month:i,valuePerMonth:0}
            var month2 = {month:i,valuePerMonth:0}
            vm.totalEgressByMonth.push(month1)
            vm.totalIngressByMonth.push(month2)

        }

        vm.eliminateZero = function(item){
            if(item.valuePerMonth=="0"){
                item.valuePerMonth = "";
            }

        }
        vm.putZero = function(item){
            if(item.valuePerMonth=="" || item.valuePerMonth==null || item.valuePerMonth==undefined){
                item.valuePerMonth = "0";
            }

        }

         $timeout(function (){
           angular.element('.form-group:eq(1)>input').focus();
         });
         setTimeout(function(){
            createBudgetYears();
            EgressCategory.query({companyId: $rootScope.companyId}).$promise.then(onSuccessEgressCategories);

         },900)
        function onSuccessEgressCategories(data, headers) {

            angular.forEach(data,function(egressCategory,key){
                egressCategory.valuesPerMonth = []
                for(var i=1;i<=12;i++){
                   var item = {month:i,valuePerMonth:"0"}
                   egressCategory.valuesPerMonth.push(item);
                 }
            })
            vm.egressCategories = data;
            setTimeout(function() {
               $("#loadingIcon").fadeOut(300);
            }, 400)
            setTimeout(function() {
                $("#budgetContainer").fadeIn('slow');
            }, 700)
        }
        function createBudgetYears(){
            vm.budgetYearsToSelect = []
            var actualYear = parseInt(moment(new Date()).format('YYYY'))
            for(var i=0;i<4;i++){
                vm.budgetYearsToSelect.push({year:actualYear+i});
            }
               Presupuesto.query({companyId:$rootScope.companyId},function(result) {
                     angular.forEach(result,function(item,key){
                         angular.forEach(vm.budgetYearsToSelect,function(yearItem,key){
                             if(item.anno == yearItem.year){
                                 vm.budgetYearsToSelect.splice(key, 1);
                             }
                          })
                      })
                });
        }
          vm.expand = function(){

                setTimeout(function () {
                        $scope.$apply(function () {
                             vm.expanding = !vm.expanding;
                        });
                    }, 200);

                }

        function getValuesPerMonth () {
            invalidInputs=0;
            inputsFullQuantity=0;
            vm.mantenimientoStringValuesPerMonth = "";
            vm.extraordinariaStringValuesPerMonth = "";
            vm.areasComunesStringValuesPerMonth = "";
            vm.otrosIngresosStringValuesPerMonth = "";
                angular.forEach(vm.mantenimientoValues,function(item,key){
                     if(vm.hasLettersOrSpecial(item.valuePerMonth)){
                        invalidInputs++;
                     }
                     vm.mantenimientoStringValuesPerMonth = vm.mantenimientoStringValuesPerMonth + sortMonthValues(item)
                })
                angular.forEach(vm.extraordinariaValues,function(item,key){
                      if(vm.hasLettersOrSpecial(item.valuePerMonth)){
                        invalidInputs++;
                     }
                     vm.extraordinariaStringValuesPerMonth = vm.extraordinariaStringValuesPerMonth + sortMonthValues(item)
                })
                angular.forEach(vm.areasComunesValues,function(item,key){
                      if(vm.hasLettersOrSpecial(item.valuePerMonth)){
                        invalidInputs++;
                     }
                     vm.areasComunesStringValuesPerMonth = vm.areasComunesStringValuesPerMonth + sortMonthValues(item)
                })
                angular.forEach(vm.otrosIngresosValues,function(item,key){
                      if(vm.hasLettersOrSpecial(item.valuePerMonth)){
                        invalidInputs++;
                     }
                     vm.otrosIngresosStringValuesPerMonth = vm.otrosIngresosStringValuesPerMonth + sortMonthValues(item)
                })
                 angular.forEach(vm.egressCategories,function(item,key){

                     var valuesPerMonth = ""
                     item.valuePerMonth = "";
                     angular.forEach(item.valuesPerMonth,function(item2,key){
                          if(vm.hasLettersOrSpecial(item2.valuePerMonth)){
                             invalidInputs++;
                          }
                          item.valuePerMonth = item.valuePerMonth + sortMonthValues(item2)
                     })

                })

        }
         vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
             var rgx = /(\d+)(\d{3})/;
             while (rgx.test(x1)) {
                     x1 = x1.replace(rgx, '$1' + ',' + '$2');
             }
             return x1 + x2;
         }
         function sortMonthValues(item) {
               var valuePerMonth = "";
               if(item.valuePerMonth=="" || item.valuePerMonth==undefined || item.valuePerMonth=="0"){
                  valuePerMonth = "0" + ","
               }else{
                  inputsFullQuantity++;
                  valuePerMonth = item.valuePerMonth + ","
               }
               return valuePerMonth;
         }

       vm.setTotalIngressByMonth = function(index,month){
           if(vm.hasLettersOrSpecial(month.valuePerMonth)){
             month.valido = false;
           }else{
             month.valido = true;
           }
           vm.totalIngressByMonth[index].valuePerMonth = 0;
           angular.forEach(vm.mantenimientoValues,function(item,key){
                 if(index==key){
                    var value = item.valuePerMonth;
                      if(value=="" || value==undefined){
                          value = "0";
                      }
                     vm.totalIngressByMonth[key].valuePerMonth = vm.totalIngressByMonth[key].valuePerMonth + parseInt(value);
                  }
           })
           angular.forEach(vm.extraordinariaValues,function(item,key){
                if(index==key){
                   var value = item.valuePerMonth;
                     if(value=="" || value==undefined){
                         value = "0";
                     }
                    vm.totalIngressByMonth[key].valuePerMonth = vm.totalIngressByMonth[key].valuePerMonth + parseInt(value);
                 }
           })
           angular.forEach(vm.areasComunesValues,function(item,key){
                if(index==key){
                   var value = item.valuePerMonth;
                     if(value=="" || value==undefined){
                         value = "0";
                     }
                    vm.totalIngressByMonth[key].valuePerMonth = vm.totalIngressByMonth[key].valuePerMonth + parseInt(value);
                 }
           })
          angular.forEach(vm.otrosIngresosValues,function(item,key){
               if(index==key){
                  var value = item.valuePerMonth;
                    if(value=="" || value==undefined){
                        value = "0";
                    }
                   vm.totalIngressByMonth[key].valuePerMonth = vm.totalIngressByMonth[key].valuePerMonth + parseInt(value);
                }
          })
        }
       vm.setTotalEgressByMonth = function(index,month){
          if(vm.hasLettersOrSpecial(month.valuePerMonth)){
            month.valido = false;
          }else{
            month.valido = true;
          }
           vm.totalEgressByMonth[index].valuePerMonth = 0;
           angular.forEach(vm.egressCategories,function(item,key){
              angular.forEach(item.valuesPerMonth,function(item2,key){
                  if(index==key){
                     var value = item2.valuePerMonth;
                       if(value=="" || value==undefined){
                           value = "0";
                       }
                      vm.totalEgressByMonth[key].valuePerMonth = vm.totalEgressByMonth[key].valuePerMonth + parseInt(value);
                   }
              })


           })

        }
        function onSaveError () {
            vm.isSaving = false;
        }
         vm.confirmSave = function() {

                bootbox.confirm({

                    message: "¿Está seguro que desea registrar el presupuesto con los valores ingresados?",

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
                            save ();
                        }
                    }
                });
            };
         function save (){

           if(vm.presupuesto.anno==undefined){
                toastr["error"]("Debe seleccionar el año a presupuestar");
           }else{
                getValuesPerMonth();
                if(invalidInputs>0){
                    toastr["error"]("No puede ingresar letras ni carácteres especiales");
                }else if(inputsFullQuantity==0){
                    toastr["error"]("Debe ingresar al menos un valor en algún campo");}
                else{
                  var date = new Date();
                  date.setDate(1);
                  date.setMonth(0);
                  date.setYear(vm.presupuesto.anno.year);
                  vm.presupuesto.date = date;
                  vm.presupuesto.anno = vm.presupuesto.anno.year;
                    vm.presupuesto.modificationDate = moment(new Date(), 'DD/MM/YYYY').toDate();
                    vm.presupuesto.companyId = $rootScope.companyId;
                    vm.presupuesto.deleted = 0;
                    Presupuesto.save(vm.presupuesto, saveIngresosValues, onSaveError);
                }
            }

        }

       vm.hasLettersOrSpecial = function(s){
            var caracteres = ['´','Ç','_','ñ','Ñ','¨',';','{','}','[',']','"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres,function(val,index){
                if (s!=undefined){
                    for(var i=0;i<s.length;i++){
                        if(s.charAt(i).toUpperCase()==val.toUpperCase()){
                            invalido++;

                        }
                    }
                }
            })
            if(invalido==0){
                return false;
            }else{
                return true;
            }
        }

        function saveIngresosValues (result) {
            for(var i=0;i<=3;i++){
            var detallePresupuesto = {};
            switch(i){
                case 0:
                    detallePresupuesto.category = 'mantenimiento';
                    detallePresupuesto.valuePerMonth = vm.mantenimientoStringValuesPerMonth;

                    break;
                case 1:
                    detallePresupuesto.category = 'extraordinarias';
                    detallePresupuesto.valuePerMonth = vm.extraordinariaStringValuesPerMonth;

                    break;
                case 2:
                    detallePresupuesto.category = 'areas comunes';
                    detallePresupuesto.valuePerMonth = vm.areasComunesStringValuesPerMonth;

                    break;
               case 3:
                    detallePresupuesto.category = 'otros ingresos';
                    detallePresupuesto.valuePerMonth = vm.otrosIngresosStringValuesPerMonth;


                    break;
                }

               detallePresupuesto.type = 1;
               detallePresupuesto.presupuestoId = result.id;
               DetallePresupuesto.save(detallePresupuesto);
            }
           saveEgressValues(result);

         }
        function saveEgressValues (result) {
          angular.forEach(vm.egressCategories,function(item,key){
               var detallePresupuesto = {};
                detallePresupuesto.category = item.id;
                detallePresupuesto.valuePerMonth = item.valuePerMonth;
                detallePresupuesto.type = 2;
                detallePresupuesto.presupuestoId = result.id;
                DetallePresupuesto.save(detallePresupuesto);

          })
           toastr["success"]("Se ha creado el presupuesto correctamente");
           $localStorage.budgetAction = 1;
           $state.go('presupuesto-detail', {id:result.id});

        }


    }
})();
