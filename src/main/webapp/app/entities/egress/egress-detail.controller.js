(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDetailController', EgressDetailController);

    EgressDetailController.$inject = ['AdministrationConfiguration', 'ExchangeRateBccr', 'AditumStorageService', 'CommonMethods', '$scope', '$state', '$rootScope', '$stateParams', 'previousState', 'entity', 'Egress', 'Company', 'Proveedor', 'Banco', 'Principal', 'Modal', 'globalCompany'];

    function EgressDetailController(AdministrationConfiguration, ExchangeRateBccr, AditumStorageService, CommonMethods, $scope, $state, $rootScope, $stateParams, previousState, entity, Egress, Company, Proveedor, Banco, Principal, Modal, globalCompany) {
        var vm = this;
        $rootScope.active = "newEgress";
        $rootScope.mainTitle = "Detalle de gasto";
        vm.isReady = false;
        vm.save = save;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.companies = Company.query();
        vm.showOriginalCurrency = true;
        vm.comission = false;

        vm.showCurrencyColones = function(){
            if(vm.egress.currency=="₡"){
                return true;
            }else{
                return false;
            }
        }
        function loadAdminConfig() {
            vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
            if (vm.egress.currency == vm.companyConfig.currency) {
                vm.showOriginalCurrency = true;
            } else {
                vm.showOriginalCurrency = false;
            }
        }

        vm.egress = entity;
        loadAdminConfig();
        if (vm.egress.state == 3) {
            vm.egress.ammountDoubleMoney = 0;
            vm.egress.ivaDoubleMoney = 0;
            vm.egress.subtotalDoubleMoney = 0;
            vm.egress.account = {currency: ""}
        }
        vm.previousState = previousState.name;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        vm.fileNameStart = vm.egress.fileName;
        vm.Today = moment(new Date()).format();
        vm.bccrUse = true;

        vm.formatCurrencyToPay = function () {
            var venta = vm.bccrUse ? vm.tipoCambio.venta : vm.egress.account.saleExchangeRate;
            vm.venta = venta;
            if (vm.egress.account.currency != vm.egress.currency) {
                vm.egress.exchangeRate = venta;
                if (vm.egress.account.currency == "₡" && vm.egress.currency == "$") {
                    vm.egress.ammountDoubleMoney = vm.egress.total * venta;
                    vm.egress.ivaDoubleMoney = vm.egress.iva * venta;
                    vm.egress.subtotalDoubleMoney = vm.egress.subtotal * venta;
                }
                if (vm.egress.account.currency == "$" && vm.egress.currency == "₡") {
                    if (vm.egress.subtotal == 0 && vm.egress.total == 0) {
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


        vm.calculateTotal = function () {
            vm.formatCurrencyToPay()
        }

        ExchangeRateBccr.get({
            fechaInicio: moment(new Date()).format(),
            fechaFinal: moment(new Date()).format(),
        }, function (result) {
            vm.tipoCambio = result;
            vm.calculateTotal()
        })
        var file = null;
        if (vm.egress.subtotal == 0) {
            vm.hasIva = false;
        } else {
            vm.hasIva = true;
        }
        vm.datePickerOpenStatus = {};
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        vm.openCalendar = openCalendar;
        var unsubscribe = $rootScope.$on('aditumApp:egressUpdate', function (event, result) {
            vm.egress = result;
        });
        $scope.$on('$destroy', unsubscribe);
        if (vm.egress.folio == null || vm.egress.folio == 'undefined') {
            vm.egress.folio = 'Sin Registrar'
            vm.comission = true;
        }

        if (vm.egress.paymentDate == null || vm.egress.paymentDate == undefined || vm.egress.paymentDate == 'No pagado') {
            vm.egress.paymentDate = "No pagado";
        } else {
            Banco.get({id: vm.egress.account}, onSuccessAccount)
        }

        function save() {
            Modal.showLoadingBar();
            var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
            var expirationTime = new Date(vm.egress.expirationDate).getTime();

            if (vm.egress.account.currency != vm.egress.currency) {
                vm.egress.doubleMoney = 1;
            }else{
                vm.egress.doubleMoney = 0;
                if (vm.egress.currency == "$") {
                    vm.egress.ammountDoubleMoney = vm.egress.total * vm.venta;
                    vm.egress.ivaDoubleMoney = vm.egress.iva * vm.venta;
                    vm.egress.subtotalDoubleMoney = vm.egress.subtotal * vm.venta;
                }
                if (vm.egress.currency == "₡") {
                    vm.egress.ammountDoubleMoney = vm.egress.total / vm.venta;
                    vm.egress.ivaDoubleMoney = vm.egress.iva / vm.venta;
                    vm.egress.subtotalDoubleMoney = vm.egress.subtotal / vm.venta;
                }
            }
            if (currentTime <= expirationTime) {
                vm.egress.state = 1;
            } else {
                vm.egress.state = 3;
            }
            if (vm.egress.paymentDate !== null || vm.egress.paymentDate == 'undefined') {
                vm.egress.state = 2;
            }
            if (vm.egress.comission == null || vm.egress.comission == 0) {
                vm.egress.hasComission = 0;
            } else {
                vm.egress.hasComission = 1;
            }
            vm.egress.account = vm.egress.account.id;
            Egress.update(vm.egress, onSaveSuccess, onSaveError);
        }

        vm.calculateWithIVA = function (subtotal) {
            vm.formatCurrencyToPay();
            vm.egress.iva = subtotal * 0.13;
            vm.egress.total = vm.egress.iva + subtotal + "";
        }
        vm.saveExchangeRate = function () {
            vm.egress.account.exchangeRateDate = moment(new Date()).format()
            console.log(vm.egress.account)
            Banco.update(vm.egress.account, function () {
                Modal.toast("Monto de tipo de cambio actualizado.")
            }, function () {
            });
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.confirmReportPayment = function () {

            Modal.confirmDialog("¿Está seguro que desea reportar el pago de este egreso?", "Una vez registrada esta información no se podrá editar",
                function () {
                    if (vm.fileName) {
                        vm.isSaving = true;
                        upload(vm.egress.proveedor);
                    }else{
                        save();
                    }
                });

        }

        function upload(proveedorId) {
            var today = new Date();
            moment.locale("es");
            vm.direction = globalCompany.getId() + '/expenses/' + moment(today).format("YYYY") + '/' + moment(today).format("MMMM") + '/' + proveedorId + '/';
            var uploadTask = AditumStorageService.ref().child(vm.direction + file.name).put(file);
            uploadTask.on('state_changed', function (snapshot) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    })
                }, 1)
                switch (snapshot.state) {
                    case firebase.storage.TaskState.PAUSED: // or 'paused'
                        console.log('Upload is paused');
                        break;
                    case firebase.storage.TaskState.RUNNING: // or 'running'
                        console.log('Upload is running');
                        break;
                }
            }, function (error) {
                // Handle unsuccessful uploads
                console.log(error);
            }, function () {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    vm.egress.urlFile = downloadURL;
                    vm.egress.fileName = vm.fileName;
                    save();
                });
            });
        }

        vm.setFile = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                vm.file = $file;
                vm.fileName = vm.file.name;
                file = $file;
            }
        };

        function onSaveSuccess(result) {
            angular.forEach(vm.bancos, function (banco, key) {
                if (banco.id == vm.egress.account) {
                    banco.saldo = banco.saldo - vm.egress.total;
                    Banco.update(banco, onAccountBalanceSuccess, onSaveError);
                }
            });
        }

        function onAccountBalanceSuccess(result) {
            Modal.toast("Se reportó el pago correctamente");
            Banco.get({id: vm.egress.account}, onSuccessAccount)
            Proveedor.get({id: vm.egress.proveedor}, onSuccessProovedor)
            Modal.hideLoadingBar();
            vm.isSaving = false;
        }


        setTimeout(function () {
            Proveedor.get({id: vm.egress.proveedor}, onSuccessProovedor)
        }, 700)

        function onSuccessProovedor(proovedor, headers) {
            vm.egress.empresa = proovedor.empresa;
            Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);
        }

        function onSuccessBancos(data, headers) {

            vm.isReady = true;
            vm.bancos = data;
        }

        function onSuccessAccount(account, headers) {
            vm.egress.banco = account.beneficiario;
            vm.banco = account;
        }

        vm.datePickerOpenStatus.paymentDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }


})();
