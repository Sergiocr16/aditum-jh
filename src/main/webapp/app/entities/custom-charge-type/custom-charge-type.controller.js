(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeController', CustomChargeTypeController);

    CustomChargeTypeController.$inject = ['CustomChargeType'];

    function CustomChargeTypeController(CustomChargeType) {

        var vm = this;

        vm.customChargeTypes = [];

        loadAll();

        function loadAll() {
            CustomChargeType.query(function(result) {
                vm.customChargeTypes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
