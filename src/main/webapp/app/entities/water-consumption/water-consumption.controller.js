(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterConsumptionController', WaterConsumptionController);

    WaterConsumptionController.$inject = ['CommonMethods','$state', 'WaterConsumption', 'House', 'AdministrationConfiguration', 'globalCompany', '$rootScope', 'Modal'];

    function WaterConsumptionController(CommonMethods, $state, WaterConsumption, House, AdministrationConfiguration, globalCompany, $rootScope, Modal) {

        var vm = this;
        vm.isReady = false;
        vm.houses = [];
        vm.loadAll = loadAll;
        moment.locale("es");
        $rootScope.mainTitle = "Consumo de agua";
        $rootScope.active = "waterConsumption";
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        vm.date = new Date(y, m, 1)
        vm.currentWCIndex = undefined;
        vm.waterConsumptions = [];
        vm.adminConfig = {waterPrice: 0};

        vm.editingPrice = false;

        vm.editWaterPrice = function () {
            Modal.confirmDialog("¿Está seguro que desea modificar el precio por metro cúbico?", "Esto afectará el calculo de las cuotas de agua de ahora en adelante.", function () {
                vm.isSaving = true;
                AdministrationConfiguration.update(vm.adminConfig, function (result) {
                    vm.adminConfig = result;
                    vm.editingWaterPriceToogle();
                    Modal.toast("Se modifico el precio por metro cúbico.");
                }, onSaveError);
            })
        };

        vm.editingWaterPriceToogle = function () {
            vm.editingPrice = !vm.editingPrice;
        }

        loadAll();

        vm.toPay = function (wC) {
            return wC.consumptionInt * parseFloat(vm.adminConfig.waterPrice);
        };

        function loadAll() {
            vm.isReady = false;
            vm.waterConsumptions = [];
            WaterConsumption.queryByDate({
                    companyId: globalCompany.getId(),
                    date: moment(vm.date).format()
                },
                function (result) {
                    for (var i = 0; i < result.length; i++) {
                        result[i].consumptionInt = parseFloat(result[i].consumption);
                        vm.waterConsumptions.push(result[i]);
                    }
                    vm.searchQuery = null;
                    AdministrationConfiguration.get({
                        companyId: globalCompany.getId()
                    }).$promise.then(function (result) {
                        vm.adminConfig = result;
                        vm.isReady = true;
                    })
                }
            );
        }

        vm.saveWc = function (wC, i) {
            wC.consumption = wC.consumptionInt;
            vm.currentWCIndex = i;
            if (wC.consumptionInt != 0) {
                if (wC.id !== null) {
                    WaterConsumption.update(wC, onSaveSuccess, onSaveError);
                } else {
                    WaterConsumption.save(wC, onSaveSuccess, onSaveError);
                }
            }
        }

        function onSaveSuccess(result) {
            result.consumptionInt = parseFloat(result.consumption);
            vm.waterConsumptions[vm.currentWCIndex] = result;
            vm.isSaving = false;
            Modal.toast("Guardado.")
        }

        vm.createCharge = function (wC) {
            var encryptedId = CommonMethods.encryptIdUrl(wC.id);
            $state.go('water-consumption.createCharge',{id: encryptedId})
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }

})();
