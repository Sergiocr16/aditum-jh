(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentReportController', PaymentReportController);

    PaymentReportController.$inject = ['Company','Resident', 'Banco', 'House', '$timeout', '$scope', '$state', 'Payment', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonMethods', 'Proveedor', '$rootScope', 'globalCompany', 'Modal'];

    function PaymentReportController(Company,Resident, Banco, House, $timeout, $scope, $state, Payment, ParseLinks, AlertService, paginationConstants, pagingParams, CommonMethods, Proveedor, $rootScope, globalCompany, Modal) {
        $rootScope.active = "reporteIngresos";
        var vm = this;
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        };
        $rootScope.mainTitle = "Reporte de ingresos";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.propertyName = 'id';
        vm.reverse = true;
        vm.consulting = false;
        vm.banco = "empty";
        vm.paymentMethod = "empty";
        vm.houseId = "empty";
        vm.category = "empty";
        var date = new Date(),
            y = date.getFullYear(),
            m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.consultAgain = function () {
            vm.banco = "empty";
            vm.paymentMethod = "empty";
            vm.houseId = "empty";
            vm.category = "empty";
            vm.consulting = false;
            vm.isReady2 = false;
        }
        vm.updatePicker = function () {
            vm.picker1 = {
                datepickerOptions: {
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {

                    minDate: vm.dates.initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }
        vm.updatePicker();

        vm.loadHouses = function () {
            House.query({
                companyId: globalCompany.getId()
            }, function (data, headers) {
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                vm.houses = data;
                loadAccounts();
            })

        }

        function loadAccounts() {
            Banco.query({
                companyId: globalCompany.getId()
            }, function (data, headers) {
                vm.bancos = data;
                Company.get({id:  globalCompany.getId()}).$promise.then(function (result) {
                    vm.isReady = true;
                    vm.companyName = result.name;
                });


            });
        }

        vm.printPayment = function (paymentId) {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 8000)
            printJS({
                printable: '/api/payments/file/' + paymentId,
                type: 'pdf',
                modalMessage: "Obteniendo comprobante de pago"
            })
        }


        vm.print = function () {
            vm.exportActions.printing = true;
            $timeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 6000)
            printJS({
                printable: vm.urlToDownload(),
                type: 'pdf',
                modalMessage: "Obteniendo comprobante de pago"
            })
        }
        vm.urlToDownload = function () {
            return '/api/payments/incomeReport/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + "/" + vm.banco + "/" + vm.paymentMethod + "/" + vm.houseId + "/" + vm.category;

        }
        vm.download = function () {
            vm.exportActions.downloading = true;
            $timeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 6000)
        }
        vm.loadHouses();
        vm.sendEmail = function (payment) {
            Modal.confirmDialog("¿Está seguro que desea enviarle el comprobante del pago " + payment.receiptNumber + " al contacto principal de la filial " + payment.houseNumber + "?", "",
                function () {
                    vm.exportActions.sendingEmail = true;
                    Resident.findResidentesEnabledByHouseId({
                        houseId: parseInt(payment.houseId),
                    }).$promise.then(onSuccessResident, onError);

                    function onSuccessResident(data, headers) {
                        var thereIs = false;
                        angular.forEach(data, function (resident, i) {
                            if (resident.email != undefined && resident.email != "" && resident.email != null) {
                                resident.selected = false;
                                if (resident.principalContact == 1) {
                                    thereIs = true;
                                }
                            }
                        });
                        if (thereIs == true) {
                            Payment.sendPaymentEmail({
                                paymentId: payment.id
                            })
                            $timeout(function () {
                                $scope.$apply(function () {
                                    vm.exportActions.sendingEmail = false;
                                })
                                Modal.toast("Se ha enviado el comprobante por correo al contacto principal.")

                            }, 6000)
                        } else {

                            vm.exportActions.sendingEmail = false;

                            Modal.toast("Esta filial no tiene un contacto principal para enviarle el correo.")

                        }
                    }

                    function onError() {
                        Modal.toast("Esta filial no tiene un contacto principal para enviarle el correo.")

                    }

                });


        }

        function formatearNumero(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }


        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.generateReport = function () {
            vm.isReady2 = false;
            vm.consulting = true;
            vm.showLoading = true;
            if (vm.banco == "" || vm.banco == null) {
                vm.banco = "empty"
            }
            if (vm.houseId == "" || vm.houseId == null) {
                vm.houseId = "empty"
            }
            Payment.findIncomeReportBetweenDatesByCompany({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                account: vm.banco,
                paymentMethod: vm.paymentMethod,
                houseId: vm.houseId,
                category: vm.category,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.incomeReport = data;
                console.log(data)
                vm.payments = vm.incomeReport.payments;
                angular.forEach(vm.payments, function (payment, i) {
                    payment.isShowingCharges = false;
                })
                vm.isReady2 = true;
                vm.showLoading = false;
            }

            function onError(error) {
                vm.isReady2 = true;
                Modal.toast("Ha ocurrido un error al generar el reporte de ingresos.")
                AlertService.error(error.data.message);
            }
        }

        vm.showCharges = function (payment) {
            payment.isShowingCharges = !payment.isShowingCharges;
            angular.forEach(vm.payments, function (paymentIn, i) {
                if (paymentIn.id != payment.id) {
                    paymentIn.isShowingCharges = false;
                }
            })
        }

        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
