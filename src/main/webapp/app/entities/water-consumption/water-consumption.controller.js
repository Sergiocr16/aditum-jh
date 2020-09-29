(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterConsumptionController', WaterConsumptionController);

    WaterConsumptionController.$inject = ['Resident', 'CommonMethods', '$state', 'WaterConsumption', 'House', 'AdministrationConfiguration', 'globalCompany', '$rootScope', 'Modal', 'Charge', '$mdDialog', '$scope'];

    function WaterConsumptionController(Resident, CommonMethods, $state, WaterConsumption, House, AdministrationConfiguration, globalCompany, $rootScope, Modal, Charge, $mdDialog, $scope) {

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
        vm.calcType = 1;
        vm.currentWCIndex = undefined;
        vm.waterConsumptions = [];
        vm.adminConfig = {waterPrice: 0};
        vm.confirming = false;
        vm.fechaCobro = vm.concepDate;
        vm.lastDay = new Date(vm.fechaCobro.getFullYear(), vm.fechaCobro.getMonth() + 1, 0)
        vm.montoFijo = 0;
        vm.editingPrice = false;


        vm.ayaTable = {
            nombre: "AYA",
            bloques: [
                {
                    minimo: 0,
                    maximo: 15,
                    tipos: [{nombre: "domiciliar", monto: 409}, {
                        nombre: "empresarial",
                        monto: 1620
                    }, {nombre: "preferencial", monto: 409}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 16,
                    maximo: 25,
                    tipos: [{nombre: "domiciliar", monto: 822}, {
                        nombre: "empresarial",
                        monto: 1964
                    }, {nombre: "preferencial", monto: 822}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 26,
                    maximo: 40,
                    tipos: [{nombre: "domiciliar", monto: 902}, {
                        nombre: "empresarial",
                        monto: 1964
                    }, {nombre: "preferencial", monto: 822}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 41,
                    maximo: 60,
                    tipos: [{nombre: "domiciliar", monto: 1071}, {
                        nombre: "empresarial",
                        monto: 1964
                    }, {nombre: "preferencial", monto: 822}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 61,
                    maximo: 80,
                    tipos: [{nombre: "domiciliar", monto: 1964}, {
                        nombre: "empresarial",
                        monto: 1964
                    }, {nombre: "preferencial", monto: 902}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 81,
                    maximo: 100,
                    tipos: [{nombre: "domiciliar", monto: 1964}, {
                        nombre: "empresarial",
                        monto: 1964
                    }, {nombre: "preferencial", monto: 902}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 101,
                    maximo: 120,
                    tipos: [{nombre: "domiciliar", monto: 1964}, {
                        nombre: "empresarial",
                        monto: 1964
                    }, {nombre: "preferencial", monto: 902}, {nombre: "gobierno", monto: 1620}]
                },
                {
                    minimo: 120,
                    maximo: "∞",
                    tipos: [{nombre: "domiciliar", monto: 2063}, {
                        nombre: "empresarial",
                        monto: 2063
                    }, {nombre: "preferencial", monto: 902}, {nombre: "gobierno", monto: 1620}]
                }],
            tipoSelected: 0,
            cargoFijo: {
                tipos: [{nombre: "domiciliar", monto: 2000}, {
                    nombre: "empresarial",
                    monto: 2000
                }, {nombre: "preferencial", monto: 2000}, {nombre: "gobierno", monto: 2000}]
            },
            tarifaFija: {
                tipos: [{nombre: "domiciliar", monto: 11211}, {
                    nombre: "empresarial",
                    monto: 38048
                }, {nombre: "preferencial", monto: 32947}, {nombre: "gobierno", monto: 143277}]
            }
        }
        vm.esphTable = {
            nombre: "ESPH",
            bloques: [
                {
                    minimo: 0,
                    maximo: 15,
                    tipos: [{nombre: "domiciliar", monto: 322}, {
                        nombre: "empresarial",
                        monto: 769
                    }, {nombre: "preferencial", monto: 322}, {nombre: "gobierno", monto: 769}]
                },
                {
                    minimo: 16,
                    maximo: 25,
                    tipos: [{nombre: "domiciliar", monto: 536}, {
                        nombre: "empresarial",
                        monto: 1281
                    }, {nombre: "preferencial", monto: 536}, {nombre: "gobierno", monto: 1281}]
                },
                {
                    minimo: 26,
                    maximo: 40,
                    tipos: [{nombre: "domiciliar", monto: 536}, {
                        nombre: "empresarial",
                        monto: 1281
                    }, {nombre: "preferencial", monto: 588}, {nombre: "gobierno", monto: 1281}]
                },
                {
                    minimo: 41,
                    maximo: 60,
                    tipos: [{nombre: "domiciliar", monto: 697}, {
                        nombre: "empresarial",
                        monto: 1281
                    }, {nombre: "preferencial", monto: 588}, {nombre: "gobierno", monto: 1281}]
                },
                {
                    minimo: 61,
                    maximo: 80,
                    tipos: [{nombre: "domiciliar", monto: 1281}, {
                        nombre: "empresarial",
                        monto: 1281
                    }, {nombre: "preferencial", monto: 642}, {nombre: "gobierno", monto: 1281}]
                },
                {
                    minimo: 81,
                    maximo: 100,
                    tipos: [{nombre: "domiciliar", monto: 1281}, {
                        nombre: "empresarial",
                        monto: 1281
                    }, {nombre: "preferencial", monto: 642}, {nombre: "gobierno", monto: 1281}]
                },
                {
                    minimo: 101,
                    maximo: 120,
                    tipos: [{nombre: "domiciliar", monto: 1281}, {
                        nombre: "empresarial",
                        monto: 1281
                    }, {nombre: "preferencial", monto: 642}, {nombre: "gobierno", monto: 1281}]
                },
                {
                    minimo: 120,
                    maximo: "∞",
                    tipos: [{nombre: "domiciliar", monto: 1346}, {
                        nombre: "empresarial",
                        monto: 1346
                    }, {nombre: "preferencial", monto: 642}, {nombre: "gobierno", monto: 1346}]
                }],
            tipoSelected: 0,
            cargoFijo: {
                tipos: [{nombre: "domiciliar", monto: 2000}, {
                    nombre: "empresarial",
                    monto: 2000
                }, {nombre: "preferencial", monto: 2000}, {nombre: "gobierno", monto: 2000}]
            },
            tarifaFija: {
                tipos: [{nombre: "domiciliar", monto: 11211}, {
                    nombre: "empresarial",
                    monto: 38048
                }, {nombre: "preferencial", monto: 32947}, {nombre: "gobierno", monto: 143277}]
            }
        }

        vm.tableCosts = vm.ayaTable;
        vm.defineTable = function () {
            if (vm.calcType == 2) {
                vm.tableCosts = vm.esphTable;
            }
            if (vm.calcType == 1) {
                vm.tableCosts = vm.ayaTable;
            }
        }
        vm.open = function (waterConsumption) {
            vm.checkedType = 3;
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
                    houseId: waterConsumption.houseId
                }, function (tenants) {
                    angular.forEach(tenants, function (tenant, i) {
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
                if (resident.email != null) {
                    resident.selected = true;
                } else {
                    resident.selected = false;
                }
            });
        }

        vm.selectTenant = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.type == 4 && resident.email != null) {
                    resident.selected = true;
                } else {
                    resident.selected = false;
                }
            });
        }

        vm.close = function () {
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
            var residentsToSendEmails = obtainEmailToList().slice(0, -1);
            Charge.sendChargeEmail({
                companyId: globalCompany.getId(),
                houseId: vm.waterConsumptionSelected.chargeId,
                emailTo: residentsToSendEmails
            }, function (result) {
                $mdDialog.hide();
                Modal.hideLoadingBar();
                Modal.toast("Se envió la cuota por correo correctamente.");
            });
        };

        function obtainEmailToList() {
            var residentsToSendEmails = "";
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.selected == true) {
                    if (residentsToSendEmails.indexOf(resident) === -1) {
                        residentsToSendEmails = residentsToSendEmails + resident.id + ",";
                    }
                }
            });
            return residentsToSendEmails;
        }


        loadAll();

        vm.toPay = function (wC) {
            if (vm.autoCalculated) {
                if (vm.calcType == 3) {
                    return (wC.consumptionInt * parseFloat(vm.adminConfig.waterPrice)) + vm.montoFijo;
                } else {
                    return vm.calculateToPayBaseInTable(wC.consumptionInt);
                }
            }
        };

        vm.calculateToPayBaseInTable = function (consumo) {
            var consumoRestante = consumo;
            var monto = 0;
            var bloqueAnterior = 0;
            var bloqueMaximo = 0;
            for (var i = 0; i < vm.tableCosts.bloques.length; i++) {
                var bloque = vm.tableCosts.bloques[i];
                    bloqueMaximo = bloque.maximo - bloqueAnterior;
                if (consumoRestante != 0) {
                    if (consumoRestante <= bloqueMaximo) {
                        monto = monto + (consumoRestante * bloque.tipos[vm.tableCosts.tipoSelected].monto)
                        consumoRestante = 0;
                    } else {
                        if(bloque.maximo!="∞") {
                            monto = monto + (bloqueMaximo * bloque.tipos[vm.tableCosts.tipoSelected].monto)
                            consumoRestante = consumoRestante - bloqueMaximo;
                            bloqueAnterior = bloque.maximo;
                        }else{
                            monto = monto + (consumoRestante * bloque.tipos[vm.tableCosts.tipoSelected].monto)
                        }
                    }
                }
            }
            return monto + vm.tableCosts.cargoFijo.tipos[vm.tableCosts.tipoSelected].monto;
        }

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
            wC.medicionActual = wC.medicionActualInt + "";
            wC.medicionAnterior = wC.medicionAnteriorInt + "";
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
