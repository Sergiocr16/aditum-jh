(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResultStatesAnualReportController', ResultStatesAnualReportController);

    ResultStatesAnualReportController.$inject = ['Company', 'AlertService', '$rootScope', 'Principal', 'MensualAndAnualReport', '$scope', 'Presupuesto', 'globalCompany', '$state'];

    function ResultStatesAnualReportController(Company, AlertService, $rootScope, Principal, MensualAndAnualReport, $scope, Presupuesto, globalCompany, $state) {
        var vm = this;
        vm.isReady = false;
        $rootScope.mainTitle = "Reporte anual";
        vm.datePickerOpenStatus = {};
        vm.isShowingMaintenanceDetail = false;
        vm.isShowingExtrardinaryDetail = false;
        vm.isShowingCommonAreasDetail = false;
        vm.isShowingOtherIngressDetail = false;
        vm.openCalendar = openCalendar;
        var dateMonthDay = new Date(), y1 = dateMonthDay.getFullYear(), m1 = dateMonthDay.getMonth();
        vm.actualMonth = new Date(y1, m1, 1);
        vm.month = m1 + 1;
        vm.consulting = false;
        vm.rowsQuantity = vm.month + 4;
        vm.showPresupuestoFields = false;
        var withPresupuestos = 3;
        vm.expanding = false;
        vm.resultsReady = false;
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false
        };

        vm.showGraphic = function () {
            $state.go("resultStates.anualReport.graphic", {year: vm.yearIEB})
        }
        createYearsArrays()
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        }

        function createYearsArrays() {
            var d = new Date();
            var year = d.getFullYear();
            var yearsIEB = [];
            yearsIEB.push(year)
            for (var i = 1; i <= 3; i++) {
                yearsIEB.push(year - i)
            }
            vm.yearsIEB = yearsIEB;
            vm.yearsDefaulters = yearsIEB;
            vm.yearIEB = yearsIEB[0];
            vm.yearDefaulter = yearsIEB[0];
        }

        vm.tableToExcel = function (table) {
            vm.exportingExcel = true;
            setTimeout(function () {
            var uri = 'data:application/vnd.ms-excel;base64,'
                ,
                template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'
                , base64 = function (s) {
                    return window.btoa(unescape(encodeURIComponent(s)))
                }
                , format = function (s, c) {
                    return s.replace(/{(\w+)}/g, function (m, p) {
                        return c[p];
                    })
                }
           var workSheetName = vm.companyName +" - ESTADO DE RESULTADOS - EJERCICIO " +moment(vm.actualMonth).format("YYYY");
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {worksheet: workSheetName || 'Worksheet', table: table.innerHTML}
            var a = document.createElement('a');
            a.href = uri + base64(format(template, ctx))
            a.download = workSheetName + '.xls';
            //triggering the function
            a.click();
            vm.exportingExcel = false;
        }, 1)
        }
        vm.print = function () {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 7000);
            printJS({
                printable: vm.path,
                type: 'pdf',
                modalMessage: "Obteniendo estado de resultados"
            })
        };

        vm.changeYear = function () {
            vm.consulting = true;
            vm.resultsReady = false;
            if (vm.yearIEB === new Date().getFullYear()) {
                var dateMonthDay = new Date(), y1 = dateMonthDay.getFullYear(), m1 = dateMonthDay.getMonth();
                vm.actualMonth = new Date(y1, m1, 1);
                vm.month = m1 + 1;
                vm.rowsQuantity = vm.month + 4;
                vm.loadAll();
            } else {
                var dateMonthDay = new Date();
                dateMonthDay.setFullYear(vm.yearIEB);
                dateMonthDay.setMonth(11);
                vm.actualMonth = dateMonthDay;
                vm.month = vm.actualMonth.getMonth() + 1;
                vm.rowsQuantity = vm.month + 4;
                vm.loadAll();

            }

        };


        vm.loadAll = function () {
            vm.companyId = globalCompany.getId();
            vm.actualMonthFormatted = moment(vm.actualMonth).format();
            MensualAndAnualReport.getAnualReport({
                actual_month: moment(vm.actualMonth).format(),
                companyId: globalCompany.getId(),
                withPresupuesto: withPresupuestos,
            }, onSuccess, onError);

        }
        vm.loadAll();

        function onSuccess(data, headers) {
            vm.consulting = false;
            vm.resultsReady = true;
            vm.report = data;
            vm.superObject = vm.actualMonthFormatted + '}' + vm.companyId + '}' + withPresupuestos;

            vm.path = '/api/anualReport/file/' + vm.superObject;
            Company.get({id: globalCompany.getId()}).$promise.then(function (result) {
                vm.isReady = true;
                vm.companyName = result.name;
            });
            vm.totalFlujo = vm.report.allIngressAcumulado - vm.report.allEgressAcumulado;

        }


        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.showBodyTableIngress = function (type) {

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
                case 5:
                    vm.isShowingTotalIngressDetail = !vm.isShowingTotalIngressDetail;
                    break;
            }

        };

        vm.showBodyTotalEgress = function (type) {

            switch (type) {
                case 1:
                    vm.isShowingFixedCostsDetail = !vm.isShowingFixedCostsDetail;
                    break;
                case 2:
                    vm.isShowingVariableCostsDetail = !vm.isShowingVariableCostsDetail;
                    break;
                case 3:
                    vm.isShowingOtherCostsDetail = !vm.isShowingOtherCostsDetail;
                    break;
                case 4:
                    vm.isShowingAllIngressDetail = !vm.isShowingAllIngressDetail;
                    break;
                case 5:
                    vm.isShowingAllEgressDetail = !vm.isShowingAllEgressDetail;
                    break;
            }

        };
        vm.showBodyTableEgress = function (cost) {

            cost.showDetail = !cost.showDetail;

        }


        vm.expand = function () {

            setTimeout(function () {
                $scope.$apply(function () {
                    vm.expanding = !vm.expanding;
                });
            }, 200);

        }

        vm.expandDetail = function () {
            if (vm.isShowingMaintenanceDetail == true) {
                vm.isShowingMaintenanceDetail = false;
                vm.isShowingExtrardinaryDetail = false;
                vm.isShowingCommonAreasDetail = false;
                vm.isShowingOtherIngressDetail = false;
                vm.isShowingTotalIngressDetail = false;
                vm.isShowingFixedCostsDetail = false;
                vm.isShowingVariableCostsDetail = false;
                vm.isShowingOtherCostsDetail = false;
                vm.isShowingAllIngressDetail = false;
                vm.isShowingAllEgressDetail = false;
                angular.forEach(vm.report.fixedCostEgress, function (value, key) {
                    value.showDetail = false;
                })
                angular.forEach(vm.report.variableCostEgress, function (value, key) {
                    value.showDetail = false;
                })
                angular.forEach(vm.report.otherCostEgress, function (value, key) {
                    value.showDetail = false;
                })

            } else {
                vm.isShowingMaintenanceDetail = true;
                vm.isShowingExtrardinaryDetail = true;
                vm.isShowingCommonAreasDetail = true;
                vm.isShowingOtherIngressDetail = true;
                vm.isShowingTotalIngressDetail = true;
                vm.isShowingFixedCostsDetail = true;
                vm.isShowingVariableCostsDetail = true;
                vm.isShowingOtherCostsDetail = true;
                vm.isShowingAllIngressDetail = true;
                vm.isShowingAllEgressDetail = true;
                angular.forEach(vm.report.fixedCostEgress, function (value, key) {
                    value.showDetail = true;
                })
                angular.forEach(vm.report.variableCostEgress, function (value, key) {
                    value.showDetail = true;
                })
                angular.forEach(vm.report.otherCostEgress, function (value, key) {
                    value.showDetail = true;
                })
            }

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

        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

    }
})();
