(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DestiniesController', DestiniesController);

    DestiniesController.$inject = ['Destinies','$rootScope'];

    function DestiniesController(Destinies,$rootScope) {

        var vm = this;
vm.isReady = false;
        vm.destinies = [];
        $rootScope.active = "destinies";
        loadAll();

        function loadAll() {
            Destinies.query(function(result) {
                vm.destinies = result;
                vm.searchQuery = null;
                vm.isReady = true;
            });
        }
    }
})();
