(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoController', PresupuestoController);

    PresupuestoController.$inject = ['Presupuesto','$rootScope','$state','$localStorage','CommonMethods'];

    function PresupuestoController(Presupuesto,$rootScope,$state,$localStorage,CommonMethods) {

        var vm = this;
        $rootScope.active = "presupuestos";
        vm.presupuestos = [];

        setTimeout(function(){ loadAll();},900)

        function loadAll() {

            Presupuesto.query({companyId:$rootScope.companyId},function(result) {
                vm.presupuestos = result;
                vm.searchQuery = null;
               setTimeout(function() {
                     $("#loadingIcon").fadeOut(300);
                 }, 400)
                 setTimeout(function() {
                     $("#tableData").fadeIn('slow');
                 },900 )
            });

        }

        vm.deleteBudget = function(budget) {

            bootbox.confirm({

                message: "¿Está seguro que desea eliminar el presupuesto " + moment(budget.date).format("YYYY")  + "?",

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
                          CommonMethods.waitingMessage();
                          budget.deleted = 1;
                          $("#loadingIcon").fadeIn(200);
                          $("#tableData").fadeOut(0);

                          Presupuesto.update(budget, updatedPresupusstoSuccess);
                    }
                }
            });
        };

       function updatedPresupusstoSuccess(){
             bootbox.hideAll();
             toastr["success"]("Se eliminó el presupuesto correctamente");
             loadAll()
       }
       vm.registerBudget = function(){
            if(vm.presupuestos.length>=4){
             toastr["error"]("Ya se crearon todos los presupuestos disponibles");
            }else{
                $state.go('detalle-presupuesto.new');
            }


       }
      vm.showBudgetEdit = function(budget){
          $localStorage.budgetAction = 2;
           $state.go('presupuesto-detail', {id:budget.id});
      }
       vm.showBudgetDetail = function(budget){
            $localStorage.budgetAction = 1;
             $state.go('presupuesto-detail', {id:budget.id});
        }
       function onDeleteSuccess (result) {
            bootbox.hideAll()
            loadAll();
             toastr["success"]("Se eliminó la cuenta correctamente");
            $state.go('banco-configuration');
            vm.isSaving = false;
        }
    }
})();
