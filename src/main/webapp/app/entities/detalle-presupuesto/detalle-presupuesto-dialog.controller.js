(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DetallePresupuestoDialogController', DetallePresupuestoDialogController);

    DetallePresupuestoDialogController.$inject = ['CommonMethods','$timeout', '$scope', '$stateParams', 'entity', 'DetallePresupuesto','$rootScope','EgressCategory'];

    function DetallePresupuestoDialogController (CommonMethods,$timeout, $scope, $stateParams, entity, DetallePresupuesto,$rootScope,EgressCategory) {
        var vm = this;

        vm.detallePresupuesto = entity;
        vm.save = save;
        vm.mantenimientoValues = [];
        vm.extraordinariaValues = [];
        vm.areasComunesValues = [];
        vm.otrosIngresosValues = [];

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

        CommonMethods.validateNumbers();
        function save (){
            getValuesPerMonth();
            vm.presupuesto
            Presupuesto.save(vm.presupuesto, onSaveSuccess, onSaveError);

        }
        function getValuesPerMonth () {
            vm.mantenimientoStringValuesPerMonth = ""
            vm.extraordinariaStringValuesPerMonth = ""
            vm.areasComunesStringValuesPerMonth = ""
            vm.otrosIngresosStringValuesPerMonth = ""

            angular.forEach(vm.mantenimientoValues,function(item,key){
                 mantenimientoStringValuesPerMonth = mantenimientoStringValuesPerMonth + sortMonthValues(item)
            })
            angular.forEach(vm.extraordinariaValues,function(item,key){
                 extraordinariaStringValuesPerMonth = extraordinariaStringValuesPerMonth + sortMonthValues(item)
            })
            angular.forEach(vm.areasComunesValues,function(item,key){
                 areasComunesStringValuesPerMonth = areasComunesStringValuesPerMonth + sortMonthValues(item)
            })
            angular.forEach(vm.otrosIngresosValues,function(item,key){
                 otrosIngresosStringValuesPerMonth = otrosIngresosStringValuesPerMonth + sortMonthValues(item)
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
        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:detallePresupuestoUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
