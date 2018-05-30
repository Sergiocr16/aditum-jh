(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DetallePresupuestoDialogController', DetallePresupuestoDialogController);

    DetallePresupuestoDialogController.$inject = ['Presupuesto','CommonMethods','$timeout', '$scope', '$stateParams', 'entity', 'DetallePresupuesto','$rootScope','EgressCategory'];

    function DetallePresupuestoDialogController (Presupuesto,CommonMethods,$timeout, $scope, $stateParams, entity, DetallePresupuesto,$rootScope,EgressCategory) {
        var vm = this;
        vm.presupuesto = {};
        vm.detallePresupuesto = entity;
        vm.save = save;
        vm.mantenimientoValues = [];
        vm.extraordinariaValues = [];
        vm.areasComunesValues = [];
        vm.otrosIngresosValues = [];
        vm.budgetYearsToSelect = [];

        for(var i=1;i<=12;i++){
            var mantenimientoValue = {month:i,valuePerMonth:""}
            var extraordinariaValue = {month:i,valuePerMonth:""}
            var areasComunesValue = {month:i,valuePerMonth:""}
            var otrosIngresosValue = {month:i,valuePerMonth:""}
            vm.mantenimientoValues.push(mantenimientoValue);
            vm.extraordinariaValues.push(extraordinariaValue);
            vm.areasComunesValues.push(areasComunesValue);
            vm.otrosIngresosValues.push(otrosIngresosValue);


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
                   var item = {month:i,valuePerMonth:""}
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
        function getValuesPerMonth () {
            vm.mantenimientoStringValuesPerMonth = ""
            vm.extraordinariaStringValuesPerMonth = ""
            vm.areasComunesStringValuesPerMonth = ""
            vm.otrosIngresosStringValuesPerMonth = ""

            angular.forEach(vm.mantenimientoValues,function(item,key){
                 vm.mantenimientoStringValuesPerMonth = vm.mantenimientoStringValuesPerMonth + sortMonthValues(item)
            })
            angular.forEach(vm.extraordinariaValues,function(item,key){
                 vm.extraordinariaStringValuesPerMonth = vm.extraordinariaStringValuesPerMonth + sortMonthValues(item)
            })
            angular.forEach(vm.areasComunesValues,function(item,key){
                 vm.areasComunesStringValuesPerMonth = vm.areasComunesStringValuesPerMonth + sortMonthValues(item)
            })
            angular.forEach(vm.otrosIngresosValues,function(item,key){
                 vm.otrosIngresosStringValuesPerMonth = vm.otrosIngresosStringValuesPerMonth + sortMonthValues(item)
            })
             angular.forEach(vm.egressCategories,function(item,key){
                 var valuesPerMonth = ""
                 item.valuePerMonth = "";
                 angular.forEach(item.valuesPerMonth,function(item2,key){
                  item.valuePerMonth = item.valuePerMonth + sortMonthValues(item2)
                 })

            })
        }
         function sortMonthValues(item) {
               var valuePerMonth = "";
               if(item.valuePerMonth=="" || item.valuePerMonth==undefined){
                  valuePerMonth = "0" + ","
               }else{
                  valuePerMonth = item.valuePerMonth + ","
               }
               return valuePerMonth;
         }


        function onSaveError () {
            vm.isSaving = false;
        }
         function save (){
           if(vm.presupuesto.anno==undefined){
                toastr["error"]("Debe seleccionar el año a presupuestar");
           }else{
                getValuesPerMonth();
                vm.presupuesto.anno = vm.presupuesto.anno.year;
                vm.presupuesto.date = moment(new Date(), 'DD/MM/YYYY').toDate();
                vm.presupuesto.modificationDate = moment(new Date(), 'DD/MM/YYYY').toDate();
                vm.presupuesto.companyId = $rootScope.companyId;
                Presupuesto.save(vm.presupuesto, saveIngresosValues, onSaveError);
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
                    detallePresupuesto.category = 'extraordinaria';
                    detallePresupuesto.valuePerMonth = vm.extraordinariaStringValuesPerMonth;

                    break;
                case 2:
                    detallePresupuesto.category = 'areascomunes';
                    detallePresupuesto.valuePerMonth = vm.areasComunesStringValuesPerMonth;

                    break;
               case 3:
                    detallePresupuesto.category = 'otrosingresos';
                    detallePresupuesto.valuePerMonth = vm.otrosIngresosStringValuesPerMonth;


                    break;
                }

               detallePresupuesto.type = 1;
               detallePresupuesto.presupuestoId = result.id;
               console.log(vm.detallePresupuesto)
               DetallePresupuesto.save(detallePresupuesto);
            }
           saveEgressValues(result);

         }
        function saveEgressValues (result) {
          angular.forEach(vm.egressCategories,function(item,key){
               var detallePresupuesto = {};
                detallePresupuesto.category = item.category;
                detallePresupuesto.valuePerMonth = item.valuePerMonth;
                detallePresupuesto.type = 1;
                detallePresupuesto.presupuestoId = result.id;
                DetallePresupuesto.save(detallePresupuesto);

          })



        }


    }
})();
