(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResultStatesMensualReportController', ResultStatesMensualReportController);

    ResultStatesMensualReportController.$inject = ['CommonMethods','Company','AlertService', '$rootScope', 'Principal', 'MensualAndAnualReport', '$scope', 'Presupuesto', 'globalCompany','Modal'];

    function ResultStatesMensualReportController(CommonMethods,Company,AlertService, $rootScope, Principal, MensualAndAnualReport, $scope, Presupuesto, globalCompany,Modal) {

        var vm = this;

        $rootScope.mainTitle = "Reporte mensual";
        vm.isReady = false;
        var dateMonthDay = new Date(), y1 = dateMonthDay.getFullYear(), m1 = dateMonthDay.getMonth();
        var firstMonthDay = new Date(y1, m1, 1);
        vm.isShowingMaintenanceDetail = false;
        vm.isShowingExtrardinaryDetail = false;
        vm.isShowingCommonAreasDetail = false;
        vm.isShowingOtherIngressDetail = false;
        vm.isShowingInitialBalanceAccounts = false;
        vm.showPresupuestoFields = false;
        vm.expanding = false;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };
        vm.fechaInicio = vm.dates.initial_time;
        vm.fechaFin = vm.dates.final_time;
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false
        };
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        };

        vm.tableToExcel = function (table) {
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
            var workSheetName = vm.companyName +" - REPORTE ESTADO RESULTADOS - del " +moment(vm.fechaInicio).format("L")+" al "+moment(vm.fechaFin).format("L");
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {worksheet: workSheetName || 'Worksheet', table: table.innerHTML}
            var a = document.createElement('a');
            a.href = uri + base64(format(template, ctx))
            a.download = workSheetName + '.xls';
            //triggering the function
            a.click();
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

        vm.loadAll = function () {
            vm.withPresupuesto = 0;
            var final_balance_time = new Date();
            if (firstMonthDay.getDate() == vm.dates.initial_time.getDate() && firstMonthDay.getMonth() == vm.dates.initial_time.getMonth() && firstMonthDay.getFullYear() == vm.dates.initial_time.getFullYear()) {
                final_balance_time.setDate(vm.dates.initial_time.getDate());
            } else {



                final_balance_time.setDate(vm.dates.initial_time.getDate() - 1);
                final_balance_time.setDate(vm.dates.initial_time.getDate());
                final_balance_time.setMonth(vm.dates.initial_time.getMonth());
                final_balance_time.setFullYear(vm.dates.initial_time.getFullYear());
                final_balance_time.setDate(final_balance_time.getDate()-1);
                final_balance_time.setMinutes(0);
                final_balance_time.setSeconds(0);
                final_balance_time.setHours(0);

            }
            var date = vm.dates.initial_time, y = date.getFullYear(), m = date.getMonth();
            vm.firstMonthDay = new Date(y, m, 1);

            vm.firstMonthDayFormatted = moment(vm.firstMonthDay).format()+"";
            vm.finalBalanceTimeFormatted = moment(final_balance_time).format()+"";
            vm.initialTimeFormatted = moment(vm.dates.initial_time).format()+"";
            vm.FinalTimeFormatted = moment(vm.dates.final_time).format()+"";
            vm.companyId =  globalCompany.getId();
            MensualAndAnualReport.query({
                first_month_day: moment(vm.firstMonthDay).format(),
                final_balance_time: moment(final_balance_time).format(),
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                withPresupuesto: vm.withPresupuesto,


            }, onSuccess, onError);

        };

        vm.isFinite = function (percentage) {
            return isFinite(percentage);
        }

        function onSuccess(data, headers) {
            vm.report = data;
            console.log(data)
            vm.superObject = vm.firstMonthDayFormatted +'}'+vm.finalBalanceTimeFormatted+'}'+vm.initialTimeFormatted+'}'+vm.FinalTimeFormatted+'}'+vm.companyId+'}'+vm.withPresupuesto;
            vm.path = '/api/mensualReport/file/' + vm.superObject;
            vm.initialDateBalance = vm.dates.initial_time;
            vm.fechaInicio = vm.dates.initial_time;
            vm.fechaFin = vm.dates.final_time;

            vm.showPresupuestoFields = false;

            vm.allEgressPercentageQuantity = data.mensualEgressReport.fixedCostsPercentage + data.mensualEgressReport.variableCostsPercentage + data.mensualEgressReport.otherCostsPercentage
            vm.saldoNeto = vm.report.totalInitialBalance + vm.report.mensualIngressReport.allIngressCategoriesTotal - vm.report.mensualEgressReport.allEgressCategoriesTotal;
            vm.superHabitPercentage = 100 - vm.allEgressPercentageQuantity;

            vm.reportString = JSON.stringify(vm.report);
            Company.get({id: globalCompany.getId()}).$promise.then(function (result) {
                vm.isReady = true;
                vm.companyName = result.name;
            });
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

                case 5:
                    vm.isShowingMultaDetail = !vm.isShowingMultaDetail;
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
        };

        vm.loadAll();


    }
})();
