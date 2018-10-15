(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoController', PresupuestoController);

    PresupuestoController.$inject = ['Presupuesto', '$rootScope', '$state', '$localStorage', 'CommonMethods', 'globalCompany','Modal'];

    function PresupuestoController(Presupuesto, $rootScope, $state, $localStorage, CommonMethods, globalCompany,Modal) {

        var vm = this;
        $rootScope.active = "presupuestos";
        vm.presupuestos = [];
        vm.isReady = false;
        $rootScope.mainTitle = "Presupuestos";
        loadAll();

        function loadAll() {

            Presupuesto.query({companyId: globalCompany.getId()}, function (result) {
                vm.presupuestos = result;
                vm.searchQuery = null;
                vm.isReady = true;
            });

        }

        vm.deleteBudget = function (budget) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el presupuesto " + moment(budget.date).format("YYYY") + "?","",
                function(){
                    Modal.showLoadingBar();
                    budget.deleted = 1;
                    $("#loadingIcon").fadeIn(200);
                    $("#tableData").fadeOut(0);

                    Presupuesto.update(budget, updatedPresupusstoSuccess);
                });


        };

        function updatedPresupusstoSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se eliminó el presupuesto correctamente");
            loadAll()
        }

        vm.registerBudget = function () {
            if (vm.presupuestos.length >= 4) {
                Modal.toast("Ya se crearon todos los presupuestos disponibles");
            } else {
                $state.go('detalle-presupuesto.new');
            }


        }
        vm.showBudgetEdit = function (budget) {
            $localStorage.budgetAction = 2;
            $state.go('presupuesto-detail', {id: budget.id});
        }
        vm.showBudgetDetail = function (budget) {
            $localStorage.budgetAction = 1;
            $state.go('presupuesto-detail', {id: budget.id});
        }

        function onDeleteSuccess(result) {
            bootbox.hideAll()
            loadAll();
            Modal.toast("Se eliminó la cuenta correctamente");
            $state.go('banco-configuration');
            vm.isSaving = false;
        }
    }
})();
