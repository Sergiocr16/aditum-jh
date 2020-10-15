(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeController', CustomChargeTypeController);

    CustomChargeTypeController.$inject = ['CustomChargeType', '$rootScope', 'globalCompany', '$state'];

    function CustomChargeTypeController(CustomChargeType, $rootScope, globalCompany, $state) {
        var vm = this;
        vm.customChargeTypes = [];
        $rootScope.active = "custom-charge-type";
        $rootScope.mainTitle = "Configurar tipos de cuota personalizados";
        vm.isReady = false;
        loadAll();
        function loadAll() {
            CustomChargeType.getByCompany({companyId: globalCompany.getId()}, function (result) {
                vm.customChargeTypes = result;
                vm.searchQuery = null;
                vm.isReady = true;
            });
        }

        vm.edit = function (cc) {
            $state.go("custom-charge-type.edit", {id: cc.id})
        }
    }
})();
