(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseLoginTrackerController', HouseLoginTrackerController);

    HouseLoginTrackerController.$inject = ['HouseLoginTracker'];

    function HouseLoginTrackerController(HouseLoginTracker) {

        var vm = this;

        vm.houseLoginTrackers = [];

        loadAll();

        function loadAll() {
            HouseLoginTracker.query(function(result) {
                vm.houseLoginTrackers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
