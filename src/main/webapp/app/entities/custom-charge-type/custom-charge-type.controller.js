(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeController', CustomChargeTypeController);

    CustomChargeTypeController.$inject = ['CustomChargeType','$rootScope'];

    function CustomChargeTypeController(CustomChargeType,$rootScope) {

        var vm = this;

        vm.customChargeTypes = [];
        $rootScope.active = "custom-charge-type";
        $rootScope.mainTitle = "Configurar tipos de cuota personalizados";
        vm.isReady = false;
        loadAll();
        function loadAll() {
            CustomChargeType.query(function(result) {
                vm.customChargeTypes = result;
                vm.searchQuery = null;
                vm.isReady = true;
            });
        }
    }
})();
