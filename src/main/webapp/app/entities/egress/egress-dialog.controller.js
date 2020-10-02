(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDialogController', EgressDialogController);

    EgressDialogController.$inject = ['CommonMethods', '$timeout', '$state', '$scope', '$stateParams', 'previousState', 'entity', 'Egress', 'Company', 'Principal', 'Proveedor', '$rootScope', 'Banco', 'EgressCategory', 'globalCompany', 'AdministrationConfiguration', 'Modal'];

    function EgressDialogController(CommonMethods, $timeout, $state, $scope, $stateParams, previousState, entity, Egress, Company, Principal, Proveedor, $rootScope, Banco, EgressCategory, globalCompany, AdministrationConfiguration, Modal) {
        var vm = this;
        $rootScope.active = "newEgress";
        vm.isReady = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.egress = entity;

        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        Modal.enteringForm(confirmCreateEgress);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.gastosVariables = [];
        vm.gastosFijos = [];
        vm.gastosOtros = [];
        vm.hasIva = true;
        CommonMethods.validateNumbers();
        CommonMethods.formatCurrencyInputs();
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        if (vm.egress.id == null) {
            vm.egress.currency = vm.companyConfig.currency;
        }

        vm.clearSearchTerm = function () {
            vm.searchTerm = '';
        };
        vm.searchTerm;
        vm.typingSearchTerm = function (ev) {
            ev.stopPropagation();
        }
        vm.showCurrencyColones = function () {
            if (vm.egress.currency == "₡") {
                return true;
            } else {
                return false;
            }
        }

        function loadAdminConfig() {
            AdministrationConfiguration.get({
                companyId: globalCompany.getId()
            }).$promise.then(function (result) {
                vm.admingConfig = result;
                if (result.egressFolio == true) {
                    vm.egressFolioSerie = result.egressFolioSerie;
                    vm.egressFolioNumber = result.egressFolioNumber;
                    vm.egress.folio = vm.egressFolioSerie + "-" + vm.egressFolioNumber;
                }
            })
        }

        loadAdminConfig();

        function increaseFolioNumber(success) {
            vm.admingConfig.egressFolioNumber = parseInt(vm.egressFolioNumber) + 1;
            vm.admingConfig.egressFolioSerie = vm.egressFolioSerie;
            AdministrationConfiguration.update(vm.admingConfig, success);
        }

        // setTimeout(function () {

        Proveedor.query({companyId: globalCompany.getId(), size: 500}).$promise.then(onSuccessProveedores);

        function onSuccessProveedores(data, headers) {
            vm.proveedores = data;

            Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);

            function onSuccessBancos(data, headers) {
                vm.bancos = data;

                EgressCategory.query({companyId: globalCompany.getId()}).$promise.then(onSuccessEgressCategories);
            }


        }

        vm.calculateWithIVA = function (subtotal) {
            vm.egress.iva = subtotal * 0.13;
            vm.egress.total = vm.egress.iva + subtotal + "";
        };


        function onSuccessEgressCategories(data, headers) {
            angular.forEach(data, function (value, key) {
                if (value.group == 'Gastos fijos') {
                    vm.gastosFijos.push(value)
                }
                if (value.group == 'Gastos variables') {
                    vm.gastosVariables.push(value)
                }

                if (value.group == 'Otros gastos') {
                    vm.gastosOtros.push(value)
                }

            })
            vm.egressCategories = data;
            vm.isReady = true;
        }


        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            vm.egress.total = x1 + x2;
        }


        if (vm.egress.id != null) {
//          vm.egress.account = null;
//          vm.egress.paymentMethod = null;
//          vm.egress.paymentDate = null;
            vm.title = 'Registrar egreso';
            vm.button = "Registrar";
            vm.picker3 = {
                datepickerOptions: {
                    minDate: vm.egress.date,
                    enableTime: false,
                    showWeeks: false,
                }
            }
            Proveedor.get({id: vm.egress.proveedor}, onSuccessProovedor)

            if (vm.egress.folio == null || vm.egress.folio == 'undefined') {
                vm.egress.folio = 'Sin Registrar'
            }
            if (vm.egress.billNumber == null || vm.egress.billNumber == 'undefined' || vm.egress.billNumber == '') {
                vm.egress.billNumber = 'Sin Registrar'
            }
        } else {
            vm.title = "Capturar cuenta por pagar";
            vm.button = "Registrar";
        }
        $rootScope.mainTitle = vm.title;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function onSuccessProovedor(proovedor, headers) {
            formatearNumero
            vm.egress.empresa = proovedor.empresa;

        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.confirmMessage = function () {
            if (vm.egress.id != null) {
                confirmReportPayment();

            } else {
                confirmCreateEgress();
            }
        }


        function confirmCreateEgress() {
            Modal.confirmDialog("¿Está seguro que desea registrar este egreso?", "Una vez registrada esta información no se podrá editar",
                function () {
                    save();
                });
        }

        function confirmReportPayment() {
            Modal.confirmDialog("¿Está seguro que desea reportar el pago de este egreso?", "Una vez registrada esta información no se podrá editar",
                function () {
                    save();
                });

        }

        vm.formatCurrencyToPay = function () {
            var venta = vm.bccrUse ? vm.tipoCambio.venta : vm.egress.account.saleExchangeRate;
            if (vm.egress.account.currency != vm.egress.currency) {
                vm.egress.exchangeRate = venta;
                if (vm.egress.account.currency == "₡" && vm.egress.currency == "$") {
                    vm.egress.ammountDoubleMoney = vm.egress.total * venta;
                    vm.egress.ivaDoubleMoney = vm.egress.iva * venta;
                    vm.egress.subtotalDoubleMoney = vm.egress.subtotal * venta;
                }
                if (vm.egress.account.currency == "$" && vm.egress.currency == "₡") {
                    if (vm.egress.subtotalDoubleMoney == 0 && vm.egress.ammountDoubleMoney == 0) {
                        vm.egress.ammountDoubleMoney = 0;
                        vm.egress.ivaDoubleMoney = 0;
                        vm.egress.subtotalDoubleMoney = 0;
                    } else {
                        vm.egress.ammountDoubleMoney = vm.egress.total / venta;
                        vm.egress.ivaDoubleMoney = vm.egress.iva / venta;
                        vm.egress.subtotalDoubleMoney = vm.egress.subtotal / venta;
                    }
                }
            }
        }

        function save() {
            Modal.showLoadingBar();
            var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
            var expirationTime = new Date(vm.egress.expirationDate).getTime();
            if (vm.egress.currency != vm.companyConfig.currency) {
                vm.egress.doubleMoney = 1;
            } else {
                vm.egress.doubleMoney = 0;
            }
            if (currentTime <= expirationTime) {
                vm.egress.state = 1;
            } else {
                vm.egress.state = 3;
            }
            if (vm.egress.paymentDate != null || vm.egress.paymentDate == 'undefined') {
                vm.egress.state = 2;
            }
            vm.isSaving = true;
            if (vm.egress.id != null) {
                Egress.update(vm.egress, onSaveReport, onSaveError);
            } else {
                vm.egress.companyId = globalCompany.getId();
                vm.egress.paymentMethod = 0;
                vm.egress.account = 0;
                vm.egress.deleted = 0;
                if (!vm.hasIva) {
                    vm.egress.subtotal = 0;
                    vm.egress.iva = 0;
                }
                Egress.save(vm.egress, onSaveSuccess, onSaveError);
            }
        }


        function onSaveReport(result) {
            Modal.hideLoadingBar();
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress-tabs.egress');
            Modal.toast("Se reportó el pago correctamente");
            vm.isSaving = false;

        }

        function onSaveSuccess(result) {
            if (vm.admingConfig.egressFolio == true) {
                increaseFolioNumber(function (admin) {
                    vm.admingConfig = admin;
                    vm.egressfolioSerie = admin.egressfolioSerie;
                    vm.egressfolioSerie = admin.egressfolioSerie;
                    Modal.hideLoadingBar();
                    $scope.$emit('aditumApp:egressUpdate', result);
                    $state.go('egress-tabs.egress');
                    Modal.toast("Se registró el gasto correctamente");
                    vm.isSaving = false;
                })
            } else {
                Modal.hideLoadingBar();
                $scope.$emit('aditumApp:egressUpdate', result);
                Modal.hideLoadingBar();
                Modal.toast("Se registró el gasto correctamente");
                $state.go('egress-tabs.egress');

                vm.isSaving = false;
            }

        }

        function onSaveError() {
            Modal.hideLoadingBar();
            Modal.toast("Un error inesperado ocurrió");
            vm.isSaving = false;
        }

        vm.updatePicker = function () {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.egress.expirationDate == undefined ? new Date() : vm.egress.expirationDate,
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.egress.date,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }
        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.paymentDate = false;
        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
