(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InitialConfigurationController', InitialConfigurationController);

    InitialConfigurationController.$inject = ['$uibModalInstance', 'Modal', '$scope', '$rootScope', '$stateParams', 'entity', 'AdministrationConfiguration', 'globalCompany', 'Banco', 'CommonMethods'];

    function InitialConfigurationController($uibModalInstance, Modal, $scope, $rootScope, $stateParams, entity, AdministrationConfiguration, globalCompany, Banco, CommonMethods) {
        var vm = this;
        $rootScope.active = "administrationConfiguration";
        vm.administrationConfiguration = entity;
        vm.isReady = false;
        vm.save = save;
        vm.data = {
            cb1: true,
            cb4: true,
            cb5: false
        };

        Banco.query({
            companyId: globalCompany.getId()
        }, function (result) {
            angular.forEach(result, function (value, key) {
                if (value.beneficiario == "Caja chica") {
                    vm.banco = value;
                    vm.isReady = true;
                }

            });
        });

        vm.message = 'false';

        vm.onChange = function (cbState) {
            vm.message = cbState;
        };
        var unsubscribe = $rootScope.$on('aditumApp:administrationConfigurationUpdate', function (event, result) {
            vm.administrationConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function save() {

            Modal.confirmDialog("¿Está seguro que desea guardar los cambios?", "Toda la configuración puede ser modificada en cualquier momento, exepto el saldo inicial de la caja chica.",
                function () {
                    if (vm.administrationConfiguration.usingSubchargePercentage === "1") {
                        vm.administrationConfiguration.usingSubchargePercentage = true;
                    } else {
                        vm.administrationConfiguration.usingSubchargePercentage = false;
                    }
                    if (vm.administrationConfiguration.residentsViewComments == true) {
                        vm.administrationConfiguration.residentsViewComments = 1;
                    } else {
                        vm.administrationConfiguration.residentsViewComments = 0;
                    }
                    vm.isSaving = true;
                    vm.administrationConfiguration.saveInBitacora = 1;
                    vm.administrationConfiguration.exchangeRateDate = moment().format();

                    console.log(vm.administrationConfiguration)
                    if (vm.administrationConfiguration.id !== null) {
                        vm.administrationConfiguration.initialConfiguration = 1;
                        AdministrationConfiguration.update(vm.administrationConfiguration, onSaveSuccess, onSaveError);
                    } else {
                        AdministrationConfiguration.save(vm.administrationConfiguration, onSaveSuccess, onSaveError);
                    }
                });
        }

        function onSaveSuccess(result) {
            vm.banco.saldo = vm.banco.capitalInicial;
            Banco.update(vm.banco, function () {
                CommonMethods.setInitialConfigReady(globalCompany.getId())
                $uibModalInstance.close(result);
                Modal.toast("Se han guardado los cambios existosamente.")
                vm.administrationConfiguration = result;
                if (vm.administrationConfiguration.usingSubchargePercentage === true) {
                    vm.administrationConfiguration.usingSubchargePercentage = "0";
                } else {
                    vm.administrationConfiguration.usingSubchargePercentage = "1";
                }
                vm.isSaving = false;
            }, onSaveError);


        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
