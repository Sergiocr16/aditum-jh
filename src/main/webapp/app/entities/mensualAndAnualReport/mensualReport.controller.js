(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualReportController', MensualReportController);

    MensualReportController.$inject = ['AlertService', '$rootScope', 'Principal', 'MensualAndAnualReport', '$scope', 'Presupuesto', 'globalCompany'];

    function MensualReportController(AlertService, $rootScope, Principal, MensualAndAnualReport, $scope, Presupuesto, globalCompany) {

        var vm = this;
        vm.datePickerOpenStatus = {};

        vm.openCalendar = openCalendar;
        var dateMonthDay = new Date(), y1 = dateMonthDay.getFullYear(), m1 = dateMonthDay.getMonth();
        var firstMonthDay = new Date(y1, m1, 1);
        vm.isShowingMaintenanceDetail = false;
        vm.isShowingExtrardinaryDetail = false;
        vm.isShowingCommonAreasDetail = false;
        vm.isShowingOtherIngressDetail = false;
        vm.isShowingInitialBalanceAccounts = false;
        vm.showPresupuestoFields = false;
        vm.expanding = false;
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };
        vm.fechaInicio = vm.dates.initial_time;
        vm.fechaFin = vm.dates.final_time;

        vm.loadAll = function () {
            if (vm.mensualReportPresupuesto) {
                vm.withPresupuesto = 1;
            } else {
                vm.withPresupuesto = 0;
            }
            var final_balance_time = new Date();
            if (firstMonthDay.getDate() == vm.dates.initial_time.getDate() && firstMonthDay.getMonth() == vm.dates.initial_time.getMonth() && firstMonthDay.getFullYear() == vm.dates.initial_time.getFullYear()) {
                final_balance_time.setDate(vm.dates.initial_time.getDate());
            } else {
                final_balance_time.setDate(vm.dates.initial_time.getDate() - 1);

            }
            final_balance_time.setMonth(vm.dates.initial_time.getMonth());
            final_balance_time.setYear(vm.dates.initial_time.getFullYear());
            var date = vm.dates.initial_time, y = date.getFullYear(), m = date.getMonth();
            vm.firstMonthDay = new Date(y, m, 1);
            MensualAndAnualReport.query({
                first_month_day: moment(vm.firstMonthDay).format(),
                final_balance_time: moment(final_balance_time).format(),
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                withPresupuesto: vm.withPresupuesto,


            }, onSuccess, onError);

        }

        vm.isFinite = function (percentage) {
            return isFinite(percentage);
        }

        function onSuccess(data, headers) {
            vm.report = data;
            vm.initialDateBalance = vm.dates.initial_time;
            vm.fechaInicio = vm.dates.initial_time;
            vm.fechaFin = vm.dates.final_time;
            if (vm.mensualReportPresupuesto) {
                Presupuesto.query({companyId: globalCompany.getId()}, function (result) {
                    if (result.length > 0) {
                        vm.showPresupuestoFields = true;
                        vm.totalEgressBudget = vm.report.mensualEgressReport.fixedCostsBudgetTotal + vm.report.mensualEgressReport.variableCostsBudgetTotal + vm.report.mensualEgressReport.otherCostsBudgetTotal;
                        vm.egressBudgetDiference = vm.report.mensualEgressReport.allEgressCategoriesTotal - vm.totalEgressBudget;
                        vm.ingressBudgetDiference = vm.report.mensualIngressReport.allIngressCategoriesTotal - vm.report.mensualIngressReport.totalBudget;
                        vm.superHabit = (vm.egressBudgetDiference * -1) - (vm.ingressBudgetDiference * -1);

                    } else {
                        toastr["error"]("No existen un presupuesto del 2018 registrado.");
                    }

                });


            } else {
                vm.showPresupuestoFields = false;
            }

            vm.allEgressPercentageQuantity = data.mensualEgressReport.fixedCostsPercentage + data.mensualEgressReport.variableCostsPercentage + data.mensualEgressReport.otherCostsPercentage
            vm.saldoNeto = vm.report.totalInitialBalance + vm.report.mensualIngressReport.allIngressCategoriesTotal - vm.report.mensualEgressReport.allEgressCategoriesTotal;
            vm.superHabitPercentage = 100 - vm.allEgressPercentageQuantity;
            $("#loadingIcon2").fadeOut(0);
            setTimeout(function () {
                $("#reportResults").fadeIn(500);
            }, 200)
        }

        vm.expand = function () {

            setTimeout(function () {
                $scope.$apply(function () {
                    vm.expanding = !vm.expanding;
                });
            }, 200);

        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.showBodyTableEgress = function (cost) {

            cost.showDetail = !cost.showDetail;

        }
        vm.showInitialBalanceAccount = function () {
            vm.isShowingInitialBalanceAccounts = !vm.isShowingInitialBalanceAccounts

        }

        vm.showBodyTableIngress = function (type, variable) {

            switch (type) {
                case 1:
                    vm.isShowingMaintenanceDetail = !vm.isShowingMaintenanceDetail;
                    break;
                case 2:
                    vm.isShowingExtrardinaryDetail = !vm.isShowingExtrardinaryDetail;
                    break;
                case 3:
                    vm.isShowingCommonAreasDetail = !vm.isShowingCommonAreasDetail;
                    break;
                case 4:
                    vm.isShowingOtherIngressDetail = !vm.isShowingOtherIngressDetail;
                    break;
            }

        };
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
        vm.updatePicker = function () {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.dates.final_time,
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
        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
        vm.loadAll();


    }
})();
