(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DestiniesController', DestiniesController);

    DestiniesController.$inject = ['Destinies','$rootScope','Charge'];

    function DestiniesController(Destinies,$rootScope,Charge) {

        var vm = this;
        vm.isReady = false;
        vm.destinies = [];
        $rootScope.active = "destinies";
        loadAll();

        vm.format = function(){
            Charge.format({},function(){

            })
        }


        function loadAll() {
            Destinies.query(function(result) {
                vm.destinies = result;
                vm.searchQuery = null;
                vm.isReady = true;
            });
        }
    }
})();
