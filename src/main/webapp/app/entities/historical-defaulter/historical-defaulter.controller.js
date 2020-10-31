(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaulterController', HistoricalDefaulterController);

    HistoricalDefaulterController.$inject = ['HistoricalDefaulter'];

    function HistoricalDefaulterController(HistoricalDefaulter) {

        var vm = this;

        vm.historicalDefaulters = [];

        loadAll();

        function loadAll() {
            HistoricalDefaulter.query(function(result) {
                vm.historicalDefaulters = result;
                vm.searchQuery = null;
            });
        }
    }
})();
