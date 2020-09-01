(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterConsumptionController', WaterConsumptionController);

    WaterConsumptionController.$inject = ['Resident','CommonMethods', '$state', 'WaterConsumption', 'House', 'AdministrationConfiguration', 'globalCompany', '$rootScope', 'Modal','Charge','$mdDialog','$scope'];

    function WaterConsumptionController(Resident,CommonMethods, $state, WaterConsumption, House, AdministrationConfiguration, globalCompany, $rootScope, Modal,Charge,$mdDialog,$scope) {

        var vm = this;
        vm.isReady = false;
        vm.houses = [];

        vm.autoCalculated = true;
        vm.loadAll = loadAll;
        moment.locale("es");
        $rootScope.mainTitle = "Consumo de agua";
        $rootScope.active = "waterConsumption";
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        vm.date = new Date(y, m, 1);
        vm.date.setMonth(vm.date.getMonth() - 1);
        vm.concepDate = new Date(y, m, 1);
        vm.concepDate.setMonth(vm.date.getMonth() + 1);
        vm.sendEmail = false;

        vm.currentWCIndex = undefined;
        vm.waterConsumptions = [];
        vm.adminConfig = {waterPrice: 0};
        vm.confirming = false;
        vm.fechaCobro = vm.concepDate;
        vm.lastDay = new Date(vm.fechaCobro.getFullYear(), vm.fechaCobro.getMonth() + 1, 0)

        vm.editingPrice = false;

        vm.open = function(waterConsumption) {
            vm.checkedType=3;
            vm.waterConsumptionSelected = waterConsumption;
            vm.residents = [];
            Resident.getOwners({
                page: 0,
                size: 1000,
                companyId: globalCompany.getId(),
                name: " ",
                houseId: waterConsumption.houseId
            }, function (residents) {
                vm.residents = residents;
                Resident.getTenants({
                    page: 0,
                    size: 1000,
                    companyId: globalCompany.getId(),
                    name: " ",
                    houseId:  waterConsumption.houseId
                }, function (tenants) {
                    angular.forEach(tenants, function (tenant, i){
                        tenant.selected = true;
                        vm.residents.push(tenant)
                    });
                    $mdDialog.show({
                        templateUrl: 'app/entities/charge/charge-send-email-form.html',
                        scope: $scope,
                        preserveScope: true
                    });
                }, onError);


            }, onError);

            function onError() {

            }

        };

        vm.selectPrincipalContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.principalContact == 1) {
                    resident.selected = true;
                }
            });
        }
        vm.selectAllContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if(resident.email!=null){
                    resident.selected = true;
                }else{
                    resident.selected = false;
                }
            });
        }

        vm.selectTenant = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if(resident.type==4 && resident.email!=null){
                    resident.selected = true;
                }else{
                    resident.selected = false;
                }
            });
        }

        vm.close = function() {
            $mdDialog.hide();
        };


        vm.editWaterPrice = function () {
            Modal.confirmDialog("¿Está seguro que desea modificar el precio por metro cúbico?", "Esto afectará el calculo de las cuotas de agua de ahora en adelante.", function () {
                vm.isSaving = true;
                Modal.showLoadingBar();
                AdministrationConfiguration.update(vm.adminConfig, function (result) {
                    vm.adminConfig = result;
                    vm.editingWaterPriceToogle();
                    Modal.hideLoadingBar();
                    Modal.toast("Se modifico el precio por metro cúbico.");
                }, onSaveError);
            })
        };

        vm.editingWaterPriceToogle = function () {
            vm.editingPrice = !vm.editingPrice;
        }

        vm.sendByEmail = function () {
            Modal.showLoadingBar();
            var residentsToSendEmails =   obtainEmailToList().slice(0, -1);
            Charge.sendChargeEmail({
                companyId: globalCompany.getId(),
                houseId: vm.waterConsumptionSelected.chargeId,
                emailTo: residentsToSendEmails
            },     function (result) {
                $mdDialog.hide();
                Modal.hideLoadingBar();
                Modal.toast("Se envió la cuota por correo correctamente.");
            });
        };

        function obtainEmailToList() {
            var residentsToSendEmails = "";
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.selected == true) {
                    if(residentsToSendEmails.indexOf(resident) === -1){
                        residentsToSendEmails = residentsToSendEmails + resident.id  + ",";
                    }
                }
            });
            return residentsToSendEmails;
        }



        loadAll();

        vm.toPay = function (wC) {
            if (vm.autoCalculated) {
                return wC.consumptionInt * parseFloat(vm.adminConfig.waterPrice);
            }
        };

        function loadAll() {
            vm.isReady = false;
            vm.waterConsumptions = [];
            vm.concepDate.setMonth(vm.date.getMonth() + 1);
            vm.lastDay = new Date(vm.fechaCobro.getFullYear(), vm.fechaCobro.getMonth() + 1, 0)

            WaterConsumption.queryByDate({
                    companyId: globalCompany.getId(),
                    date: moment(vm.date).format()
                },
                function (result) {
                    for (var i = 0; i < result.length; i++) {
                        result[i].consumptionInt = parseFloat(result[i].consumption);
                        result[i].medicionActualInt = parseFloat(result[i].medicionActual);
                        result[i].medicionAnteriorInt = parseFloat(result[i].medicionAnterior);
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
            wC.consumption = (wC.medicionActualInt - wC.medicionAnteriorInt).toFixed(2);
            wC.medicionActual = wC.medicionActualInt+"";
            wC.medicionAnterior = wC.medicionAnteriorInt+"";
            vm.currentWCIndex = i;
            if (wC.id !== null) {
                WaterConsumption.update(wC, onSaveWcSuccess, onSaveError);
            } else {
                WaterConsumption.save(wC, onSaveWcSuccess, onSaveError);
            }
        }

        function onSaveWcSuccess(result) {
            result.consumptionInt = parseFloat(result.consumption);
            result.medicionActualInt = parseFloat(result.medicionActual);
            result.medicionAnteriorInt = parseFloat(result.medicionAnterior);
            vm.waterConsumptions[vm.currentWCIndex] = result;
            vm.isSaving = false;
            Modal.toast("Guardado.")
        }

        vm.createCharge = function (wC) {
            var encryptedId = CommonMethods.encryptIdUrl(wC.id);
            $state.go('water-consumption.createCharge', {id: encryptedId})
        }

        function onSaveError() {
            vm.isSaving = false;
        }


        vm.createAllCharges = function () {
            Modal.confirmDialog("¿Está seguro que desea facturar todas las cuotas de consumo de agua?", "Solo se crearán las cuotas de las filiales que tienen registrado el consumo de agua.", function () {
                Modal.showLoadingBar();
                vm.isSaving = true;
                WaterConsumption.bilAllWaterConsumption({
                    companyId: globalCompany.getId(),
                    date: moment(vm.date).format(),
                    sendEmail: vm.sendEmail,
                    autoCalculated: vm.autoCalculated,
                    chargeDate: moment(vm.fechaCobro).format(),
                    concept: "Cuota de Agua " + moment(vm.date).format("MMMM YYYY"),
                }, onSaveSuccess)
            })
        }

        function onSaveSuccess(result) {
            Modal.toast("Se crearon las cuotas de agua correctamente.")
            Modal.hideLoadingBar();
            vm.toogleConfirmation();
            vm.isSaving = false;
            vm.loadAll();
        }

        vm.toogleConfirmation = function () {
            vm.confirming = !vm.confirming;
        }

    }

})();
