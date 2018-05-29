(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoController', PresupuestoController);

    PresupuestoController.$inject = ['Presupuesto','$rootScope'];

    function PresupuestoController(Presupuesto,$rootScope) {

        var vm = this;

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

                    }
                }
            });
        };

       function onDeleteSuccess (result) {
            bootbox.hideAll()
            loadAll();
             toastr["success"]("Se eliminó la cuenta correctamente");
            $state.go('banco-configuration');
            vm.isSaving = false;
        }
    }
})();
