(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentReportController', PaymentReportController);

    PaymentReportController.$inject = ['Banco', 'House', '$timeout', '$scope', '$state', 'Payment', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonMethods', 'Proveedor', '$rootScope'];

    function PaymentReportController(Banco, House, $timeout, $scope, $state, Payment, ParseLinks, AlertService, paginationConstants, pagingParams, CommonMethods, Proveedor, $rootScope) {
        $rootScope.active = "egress";
        var vm = this;
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.propertyName = 'id';
        vm.reverse = true;
 vm.consulting=false;
        vm.banco = "";
        vm.paymentMethod = "empty",
            vm.houseId = "";
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
        vm.consultAgain = function(){
        vm.banco = "";
                vm.paymentMethod = "empty",
                    vm.houseId = "";
                vm.category = "empty";
                vm.consulting=false;
        }
        vm.updatePicker = function() {
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

        vm.loadHouses = function() {
            House.query({
                companyId: $rootScope.companyId
            }, function(data, headers) {
                angular.forEach(data, function(value, key) {
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
                companyId: $rootScope.companyId
            }, function(data, headers) {
                vm.bancos = data;
                $("#loadingIconAll").fadeOut(300);
                setTimeout(function() {
                    $("#data").fadeIn('slow');
                },900 )
            });
        }

        vm.print = function() {
            vm.exportActions.printing = true;
            $timeout(function() {
                $scope.$apply(function() {
                    vm.exportActions.printing = false;
                })
            }, 6000)
            printJS({
                printable: vm.urlToDownload(),
                type: 'pdf',
                modalMessage: "Obteniendo comprobante de pago"
            })
        }
vm.urlToDownload = function(){
               return '/api/payments/incomeReport/file/' + moment(vm.dates.initial_time).format()+"/"+moment(vm.dates.final_time).format()+"/"+$rootScope.companyId+"/"+vm.banco+"/"+vm.paymentMethod+"/"+vm.houseId+"/"+vm.category;

}
        vm.download = function() {
            vm.exportActions.downloading = true;
            $timeout(function() {
                $scope.$apply(function() {
                    vm.exportActions.downloading = false;
                })
            }, 6000)
        }
        $timeout(function() {
            vm.loadHouses();
        }, 2000)
        vm.sendEmail = function(payment) {
            bootbox.confirm({
                message: "¿Está seguro que desea enviarle el comprobante del pago " + payment.receiptNumber + " al contacto principal de la filial " + $localStorage.houseSelected.housenumber + "?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        vm.exportActions.sendingEmail = true;
                        Resident.findResidentesEnabledByHouseId({
                            houseId: parseInt($localStorage.houseSelected.id),
                        }).$promise.then(onSuccessResident, onError);

                        function onSuccessResident(data, headers) {
                            var thereIs = false;
                            angular.forEach(data, function(resident, i) {
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
                                $timeout(function() {
                                    $scope.$apply(function() {
                                        vm.exportActions.sendingEmail = false;
                                    })
                                    toastr["success"]("Se ha enviado el comprobante por correo al contacto principal.")

                                }, 6000)
                            } else {

                                vm.exportActions.sendingEmail = false;

                                toastr["error"]("Esta filial no tiene un contacto principal para enviarle el correo.")

                            }
                        }

                        function onError() {
                            toastr["error"]("Esta filial no tiene un contacto principal para enviarle el correo.")

                        }
                    }
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

        vm.generateReport = function() {
            $("#reportResults").fadeOut(0);
            $("#loading").fadeIn(100);
            vm.consulting=true;
            if (vm.banco == "" || vm.banco == null) {
                vm.banco = "empty"
            }
            if (vm.houseId == "" || vm.houseId == null) {
                vm.houseId = "empty"
            }
            Payment.findIncomeReportBetweenDatesByCompany({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: $rootScope.companyId,
                account: vm.banco,
                paymentMethod: vm.paymentMethod,
                houseId: vm.houseId,
                category: vm.category,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.incomeReport = data;
                vm.payments = vm.incomeReport.payments;
                vm.companyName = $rootScope.companyName;
                angular.forEach(vm.payments, function(payment, i) {
                    payment.isShowingCharges = false;
                })
                $("#loading").fadeOut(297);
                $timeout(function() {
                    $("#reportResults").fadeIn("slow");
                }, 300)
            }

            function onError(error) {
                $("#loading").fadeOut(300);
                $timeout(function() {
                    $("#reportResults").fadeIn("slow");
                }, 900)
                toastr["error"]("Ha ocurrido un error al generar el reporte de ingresos.")
                AlertService.error(error.data.message);
            }
        }

        vm.showCharges = function(payment) {
            payment.isShowingCharges = !payment.isShowingCharges;
            angular.forEach(vm.payments, function(paymentIn, i) {
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
