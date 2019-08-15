(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteController', SoporteController);

    SoporteController.$inject = ['Soporte','Modal','$rootScope'];

    function SoporteController(Soporte,Modal,$rootScope) {

        var vm = this;
        vm.isReady = false;
        vm.soportes = [];
        $rootScope.active = "soporte";
        loadAll();

        function loadAll() {
            Soporte.query(function(result) {
                vm.soportes = result;
                vm.isReady = true;
                vm.searchQuery = null;
            });
        }

        vm.atenderAsunto = function (soporte,status) {
            soporte.status = status;
            Soporte.update(soporte, function () {
                Modal.toast("Se ha cambiado el estado correctamente.");
            });
        }

    }
})();
