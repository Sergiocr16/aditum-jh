(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationHouseRestrictionsController', ReservationHouseRestrictionsController);

    ReservationHouseRestrictionsController.$inject = ['ReservationHouseRestrictions'];

    function ReservationHouseRestrictionsController(ReservationHouseRestrictions) {

        var vm = this;

        vm.reservationHouseRestrictions = [];

        loadAll();

        function loadAll() {
            ReservationHouseRestrictions.query(function(result) {
                vm.reservationHouseRestrictions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
