(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OtherPaymentController', OtherPaymentController);

    OtherPaymentController.$inject = ['ExchangeRateBccr','$scope', '$localStorage', '$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'House', 'Charge', 'Banco', 'Payment', 'AdministrationConfiguration', 'Resident', 'globalCompany', 'Modal'];

    function OtherPaymentController(ExchangeRateBccr,$scope, $localStorage, $state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, House, Charge, Banco, Payment, AdministrationConfiguration, Resident, globalCompany, Modal) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.today = new Date();
        vm.isReady = false;
        $rootScope.mainTitle = "Otro ingreso";
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $rootScope.active = "otroIngreso";
        vm.printReceipt = false;
        vm.selectedAll = true;
        vm.datePickerOpenStatus = false;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        vm.payment = {};
        vm.payment.ammountToShow = 0;
        vm.payment.ammount = 0;
        vm.bccrUse = true;
        vm.Today = new Date();
        ExchangeRateBccr.get({
            fechaInicio: moment(new Date()).format(),
            fechaFinal: moment(new Date()).format(),
        },function(result){
            vm.tipoCambio = result;
        })
        vm.showDate = function () {
            if(vm.payment.date!=null){
                ExchangeRateBccr.get({
                    fechaInicio: moment(vm.payment.date).format(),
                    fechaFinal: moment(vm.payment.date).format(),
                },function(result){
                    vm.tipoCambio = result;
                    vm.Today = vm.payment.date;
                })
            }
        }
        vm.save = createPayment;
        Modal.enteringForm(createPayment);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        angular.element(document).ready(function () {
            $('.infoCharge').popover('show')
        });

        loadAll();
        $('.dating').keypress(function (e) {
            return false
        });
        vm.formatCurrencyToPay = function () {
            var venta = vm.bccrUse?vm.tipoCambio.venta:vm.account.saleExchangeRate;
            if (vm.admingConfig.chargesCollectCurrency != vm.account.currency) {
                if (vm.admingConfig.chargesCollectCurrency == "₡" && vm.account.currency == "$") {
                    vm.payment.ammount = vm.payment.ammountToShow * venta;
                }
                if (vm.admingConfig.chargesCollectCurrency == "$" && vm.account.currency == "₡") {
                    vm.payment.ammount = vm.payment.ammountToShow / venta;
                }
            }
        }
        vm.calculatePayments = function (payment) {
            setTimeout(function () {
                $scope.$apply(function () {
                        vm.formatCurrencyToPay()
                        console.log(vm.payment)
                })
            }, 1)
        }
        vm.validate = function (cuota) {
            var s = cuota.ammount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase() || s == undefined) {
                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                cuota.valida = true;
            } else {
                cuota.valida = false
            }
        }

        function loadAll() {
            loadBancos()
            loadAdminConfig()

            vm.payment = {
                paymentMethod: "DEPOSITO BANCO",
                transaction: "3",
                companyId: globalCompany.getId(),
                concept: undefined,
                ammount: 0
            };
        }

        function loadAdminConfig() {
            AdministrationConfiguration.get({
                companyId: globalCompany.getId()
            }).$promise.then(function (result) {
                vm.admingConfig = result;
                if (result.incomeFolio == true) {
                    vm.folioSerie = result.folioSerie;
                    vm.folioNumber = result.folioNumber;
                    vm.payment.receiptNumber = result.folioSerie + "-" + result.folioNumber;
                }
            })
        }

        vm.formatearNumero = function (nStr) {
            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }


        function loadBancos() {
            Banco.query({
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.bancos = data;
                vm.page = pagingParams.page;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function createPayment() {
            adelantoCondomino();
        }


        function increaseFolioNumber(success) {
            vm.admingConfig.folioNumber = vm.folioNumber + 1;
            vm.admingConfig.folioSerie = vm.folioSerie;
            AdministrationConfiguration.update(vm.admingConfig, success);
        }

        function adelantoCondomino() {
            Modal.confirmDialog("¿Está seguro que desea capturar este ingreso?", "",
                function () {
                    registrarOtroIngreso()
                });

        }

        function clear() {
            vm.payment = {
                paymentMethod: "DEPOSITO BANCO",
                transaction: "1",
                companyId: globalCompany.getId(),
                concept: 'Abono a cuotas'
            };
        }

        function registrarOtroIngreso() {
            Modal.showLoadingBar();
            vm.payment.transaction = "3";
            vm.payment.account = vm.account.beneficiario + ";" + vm.account.id;
            vm.payment.houseId = undefined;
            vm.payment.charges = [];
            vm.increasedAmmount = vm.payment.ammount;
            vm.payment.receiptNumber = vm.admingConfig.folioSerie + "-" + vm.admingConfig.folioNumber;
            vm.payment.emailTo = [];
            vm.payment.doubleMoney = 0;
            if (vm.account.currency != vm.admingConfig.chargesCollectCurrency) {
                vm.payment.doubleMoney = 1;
                vm.payment.ammountDollar = vm.payment.ammountToShow;
                vm.payment.exchangeRate = vm.account.saleExchangeRate;
            }
            Payment.save(vm.payment, onSuccess, onError)

            function onSuccess(result) {
                increaseMaintBalance(result.ammount);
                if (vm.printReceipt == true) {
                    printJS({
                        printable: '/api/payments/file/' + result.id,
                        type: 'pdf',
                        modalMessage: "Obteniendo comprobante de pago"
                    })
                    setTimeout(function () {
                        Modal.hideLoadingBar();
                        clear();
                        Modal.toast("Se ha capturado el otro ingreso correctamente.")
                        vm.printReceipt = false;
                        increaseFolioNumber(function () {
                        });
                        $state.go("houseAdministration.paymentsPerHouse")
                    }, 7000)
                } else {
                    Modal.hideLoadingBar();
                    clear();
                    Modal.toast("Se ha capturado el otro ingreso correctamente.")
                    increaseFolioNumber(function () {
                    });
                    $state.go("houseAdministration.paymentsPerHouse")
                }


            }

            function onError() {
                Modal.hideLoadingBar();
                clear()
                Modal.toast("Ups. No fue posible capturar el ingreso.")

            }
        }

        function increaseMaintBalance(ammount) {

            House.get({
                id: vm.house
            }, function (result) {
                var house = result;
                house.balance.maintenance = parseInt(house.balance.maintenance) + parseInt(ammount);
                Balance.update(house.balance, function () {
                    Modal.hideLoadingBar();
                    loadAll();
                })
            })
        }

        vm.isAnyChargeSelected = function () {
            var count = 0;
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.isIncluded == true) {
                    count++
                }
            })
            return (count > 0)
        }

        vm.back = function () {
            window.history.back();
        }
    }
})();
