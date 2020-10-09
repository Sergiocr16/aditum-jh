(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BlockReservationController', BlockReservationController);

    BlockReservationController.$inject = ['BlockReservation'];

    function BlockReservationController(BlockReservation) {

        var vm = this;

        vm.blockReservations = [];

        loadAll();

        function loadAll() {
            BlockReservation.query(function(result) {
                vm.blockReservations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
