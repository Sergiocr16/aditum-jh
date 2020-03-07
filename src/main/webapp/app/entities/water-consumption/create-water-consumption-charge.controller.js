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

        vm.date = vm.wC.recordDate;
        vm.sendEmail = true;

        AdministrationConfiguration.get({
            companyId: globalCompany.getId()
        }).$promise.then(function (result) {
            vm.adminConfig = result;
            vm.isReady = true;
            vm.charge = {
                concept: "Cuota de Agua " + moment(vm.wC.recordDate).format("MMMM YYYY"),
                total: vm.adminConfig.waterPrice * parseFloat(vm.wC.consumption)
            }
        });


        vm.confirm = function () {
            Modal.confirmDialog("¿Está seguro que desea crear esta cuota de agua?", "", function () {
                WaterConsumption.bilWaterConsumption({
                    wcId: vm.wC.id,
                    date: moment(vm.date).format(),
                    sendEmail: vm.sendEmail
                }, onSaveSuccess)
            })
        }

        function onSaveSuccess(result) {
            Modal.toast("Se creó la cuota de agua correctamente.")
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
