(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BrandController', BrandController);

    BrandController.$inject = ['Brand','Principal'];

    function BrandController(Brand,Principal) {

        var vm = this;

        vm.brands = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();

        function loadAll() {
            Brand.query(function(result) {
                vm.brands = result;
                vm.searchQuery = null;
            });
        }
    }
})();
