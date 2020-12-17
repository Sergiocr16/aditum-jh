(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalPositiveController', HistoricalPositiveController);

    HistoricalPositiveController.$inject = ['HistoricalPositive'];

    function HistoricalPositiveController(HistoricalPositive) {

        var vm = this;

        vm.historicalPositives = [];

        loadAll();

        function loadAll() {
            HistoricalPositive.query(function(result) {
                vm.historicalPositives = result;
                vm.searchQuery = null;
            });
        }
    }
})();
