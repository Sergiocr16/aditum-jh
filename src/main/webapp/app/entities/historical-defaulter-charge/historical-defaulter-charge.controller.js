(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterChargeController', HistoricalDefaulterChargeController);

    HistoricalDefaulterChargeController.$inject = ['HistoricalDefaulterCharge'];

    function HistoricalDefaulterChargeController(HistoricalDefaulterCharge) {

        var vm = this;

        vm.historicalDefaulterCharges = [];

        loadAll();

        function loadAll() {
            HistoricalDefaulterCharge.query(function(result) {
                vm.historicalDefaulterCharges = result;
                vm.searchQuery = null;
            });
        }
    }
})();
