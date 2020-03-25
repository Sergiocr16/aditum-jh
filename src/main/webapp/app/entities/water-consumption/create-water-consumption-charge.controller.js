(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CreateWaterConsumptionChargeDialogController', CreateWaterConsumptionChargeDialogController);

    CreateWaterConsumptionChargeDialogController.$inject = ['globalCompany', '$scope', '$stateParams', '$uibModalInstance', 'WaterConsumption', 'entity', 'AdministrationConfiguration', 'Modal'];

    function CreateWaterConsumptionChargeDialogController(globalCompany, $scope, $stateParams, $uibModalInstance, WaterConsumption, entity, AdministrationConfiguration, Modal) {
        var vm = this;
        vm.clear = clear;
        vm.wC = entity;
        vm.sendEmail = false;
        vm.date = vm.wC.recordDate;
        vm.date.setMonth(vm.date.getMonth() + 1);
        vm.wC.consumptionInt = parseFloat(vm.wC.consumption);

        vm.autoCalculated = true;
        AdministrationConfiguration.get({
            companyId: globalCompany.getId()
        }).$promise.then(function (result) {
            vm.adminConfig = result;
            vm.isReady = true;
            vm.concepDate = moment(vm.date).subtract(1, 'months').format("MMMM YYYY");
            vm.concept = "Cuota de Agua " + vm.concepDate;
            vm.charge = {
                concept: vm.concept,
                total: vm.adminConfig.waterPrice * parseFloat(vm.wC.consumption)
            }
        });


        vm.saveWc = function (wC) {
            wC.consumption = wC.consumptionInt;
            if (wC.id !== null) {
                WaterConsumption.update(wC, onSaveWcSuccess, onSaveError);
            } else {
                WaterConsumption.save(wC, onSaveWcSuccess, onSaveError);
            }

        }

        function onSaveWcSuccess(result) {
            result.consumptionInt = parseFloat(result.consumption);
            vm.isSaving = false;
            Modal.toast("Guardado.")
        }

        vm.confirm = function () {
            Modal.confirmDialog("¿Está seguro que desea crear esta cuota de agua?", "", function () {
                Modal.showLoadingBar();
                vm.isSaving = true;
                var o = {
                    wCid: vm.wC.id,
                    companyId: globalCompany.getId(),
                    date: moment(vm.date).format(),
                    concept: vm.concept,
                    sendEmail: vm.sendEmail,
                    autoCalculated: vm.autoCalculated
                }
                WaterConsumption.bilWaterConsumption(o, onSaveSuccess)
            })
        }

        function onSaveSuccess(result) {
            Modal.toast("Se creó la cuota de agua correctamente.")
            Modal.hideLoadingBar();
            $uibModalInstance.close(result);
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
