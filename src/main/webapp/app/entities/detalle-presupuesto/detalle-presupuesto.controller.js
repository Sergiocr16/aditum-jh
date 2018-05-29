(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DetallePresupuestoController', DetallePresupuestoController);

    DetallePresupuestoController.$inject = ['DetallePresupuesto'];

    function DetallePresupuestoController(DetallePresupuesto) {

        var vm = this;

        vm.detallePresupuestos = [];

        loadAll();

        function loadAll() {
            DetallePresupuesto.query(function(result) {
                vm.detallePresupuestos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
