(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteController', SoporteController);

    SoporteController.$inject = ['Soporte'];

    function SoporteController(Soporte) {

        var vm = this;

        vm.soportes = [];

        loadAll();

        function loadAll() {
            Soporte.query(function(result) {
                vm.soportes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
