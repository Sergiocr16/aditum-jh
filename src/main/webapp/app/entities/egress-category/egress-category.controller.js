(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressCategoryController', EgressCategoryController);

    EgressCategoryController.$inject = ['EgressCategory'];

    function EgressCategoryController(EgressCategory) {

        var vm = this;

        vm.egressCategories = [];

        loadAll();

        function loadAll() {
            EgressCategory.query(function(result) {
                vm.egressCategories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
