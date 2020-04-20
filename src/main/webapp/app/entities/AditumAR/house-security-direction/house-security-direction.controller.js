(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseSecurityDirectionController', HouseSecurityDirectionController);

    HouseSecurityDirectionController.$inject = ['HouseSecurityDirection'];

    function HouseSecurityDirectionController(HouseSecurityDirection) {

        var vm = this;

        vm.houseSecurityDirections = [];

        loadAll();

        function loadAll() {
            HouseSecurityDirection.query(function(result) {
                vm.houseSecurityDirections = result;
                vm.searchQuery = null;
            });
        }
    }
})();
