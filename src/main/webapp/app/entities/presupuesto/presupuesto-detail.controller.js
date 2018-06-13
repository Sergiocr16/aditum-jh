(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoDetailController', PresupuestoDetailController);

    PresupuestoDetailController.$inject = ['DetallePresupuesto','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Presupuesto','$localStorage'];

    function PresupuestoDetailController(DetallePresupuesto,$scope, $rootScope, $stateParams, previousState, entity, Presupuesto,$localStorage) {
        var vm = this;
        vm.presupuesto = entity;
        vm.previousState = previousState.name;
        vm.ingressCategories = [];
        vm.egressCategories = [];
        DetallePresupuesto.getCategoriesByBudget({budgetId:vm.presupuesto.id},onSuccess, onError);
        vm.totalEgressValue = 0;
        vm.totalIngressValue = 0;
         function onSuccess(data){
            vm.budgetCategories = data;
            if($localStorage.budgetAction==1){
                vm.totalIngressByMonth = [];
                vm.totalEgressByMonth = [];
                for(var i=0;i<12;i++){
                   var month1 = {month:i,valuePerMonth:0}
                   var month2 = {month:i,valuePerMonth:0}
                   vm.totalEgressByMonth.push(month1)
                   vm.totalIngressByMonth.push(month2)
                }
            }
            angular.forEach(data,function(item,key){
                 item.valuesPerMonth = []
                 item.total = 0;
                 var values = item.valuePerMonth.split(",");
                 for(var i = 0;i<12;i++){
                   var monthValues = {month:i+1,valuePerMonth:values[i]}
                   item.valuesPerMonth.push(monthValues)
                   item.total = item.total + parseInt(values[i]);
                   if(item.category=='mantenimiento' || item.category=='extraordinaria' ||item.category=='areas comunes' || item.category=='otros ingresos'){
                           vm.totalIngressByMonth[i].valuePerMonth = vm.totalIngressByMonth[i].valuePerMonth + parseInt(values[i]);

                   }else{
                         vm.totalEgressByMonth[i].valuePerMonth = vm.totalEgressByMonth[i].valuePerMonth + parseInt(values[i]);
                   }

                 }
                 if(item.category=='mantenimiento' || item.category=='extraordinaria' ||item.category=='areas comunes' || item.category=='otros ingresos'){
                      vm.ingressCategories.push(item);
                      vm.totalIngressValue = vm.totalIngressValue + item.total;
                 }else{
                      vm.egressCategories.push(item);
                      vm.totalEgressValue = vm.totalIngressValue + item.total;
                 }

            })
            if($localStorage.budgetAction==1){
                vm.budgetAction=1;
            }else{
                vm.budgetAction=2;
            }
           setTimeout(function() {
                 $("#loadingIcon").fadeOut(300);
             }, 400)
             setTimeout(function() {
                 $("#budgetContainer").fadeIn('slow');
             },900 )
        };

        vm.showEditOptions = function(){
          vm.budgetAction=2;
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
   function onError(error) {
                AlertService.error(error.data.message);
            }

        var unsubscribe = $rootScope.$on('aditumApp:presupuestoUpdate', function(event, result) {
            vm.presupuesto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
