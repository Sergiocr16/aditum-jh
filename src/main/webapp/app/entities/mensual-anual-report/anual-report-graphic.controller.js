(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnualReportGraphicController', AnualReportGraphicController).value('googleChartApiConfig', {
        version: '1.1',
        optionalSettings: {
            packages: ['bar'],
            language: 'es'
        }
    });

    AnualReportGraphicController.$inject = ['$scope', '$uibModalInstance', '$state', 'globalCompany', '$stateParams', 'Dashboard','$timeout'];

    function AnualReportGraphicController($scope, $uibModalInstance, $state, globalCompany, $stateParams, Dashboard,$timeout) {
        var vm = this;
        vm.isReady = false;
        vm.year = $stateParams.year;
        vm.charTypes = [{name: "Gráfico de barras", type: "ColumnChart"}, {name: "Gráfico de area", type: "AreaChart"}]
        vm.chartTypeEIB = vm.charTypes[0];
        var ieDone = false;
        var monthsText = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"]
        loadGraphFlujoIngresosYEgresos(vm.year, vm.chartTypeEIB.type);
        vm.showYearIEB = function () {
            loadGraphFlujoIngresosYEgresos(vm.yearIEB);
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        }

        function loadGraphFlujoIngresosYEgresos(year, type) {
            Dashboard.flujoIngresosEgresos({companyId: globalCompany.getId(), year: year}, function (result) {
                vm.anualReportDashboardDTO = result;
                defineEICurrentMonth();
                ingresosEgresosGraphInit(type)
                vm.isReady = true;
            });
        }

        function defineEICurrentMonth() {
            if (ieDone === false) {
                var m = new Date().getMonth()
                var currentMonth = vm.anualReportDashboardDTO.incomeEgressBudgetList[m];
                vm.ingressMonth = currentMonth.incomeTotalformated;
                vm.egressMonth = currentMonth.egressTotalformated;
                ieDone = true;
            }
        }


        function getColumnChart(title, val) {
            return {"v": val, "f": title}
        }


        function colsPerMonthIEB(monthData) {
            var colums = [];
            colums.push({"f": monthsText[monthData.monthValue - 1]});
            if ($state.current.name === "resultStates.anualReport.graphic") {
                colums.push(getColumnChart(monthData.incomeTotalformated, monthData.incomeTotal));
                colums.push(getColumnChart(monthData.egressTotalformated, monthData.egressTotal));
                colums.push(getColumnChart(monthData.initialBalanceformated, monthData.initialBalance));
                colums.push(getColumnChart(monthData.realBalanceformated, monthData.realBalance));
            }
            if ($state.current.name === "budgetExecution.anualReport.graphic") {
                colums.push(getColumnChart(monthData.incomeTotalformated, monthData.incomeTotal));
                colums.push(getColumnChart(monthData.budgetIncomeTotalformated, monthData.budgetIncomeTotal));
                colums.push(getColumnChart(monthData.egressTotalformated, monthData.egressTotal));
                colums.push(getColumnChart(monthData.budgetEgressTotalformated, monthData.budgetEgressTotal));
            }

            return {"c": colums}
        }


        function defineLabelsToShow() {
            var columns = []
            var monthCol = {
                "id": "month",
                "label": "Mes",
                "type": "string"
            };
            var ingressColumn = {
                "id": "ingress-id",
                "label": "Ingresos",
                "type": "number"
            };
            var budgetIngressColumn = {
                "id": "budget-ingress-id",
                "label": "Presupuesto Ingresos",
                "type": "number"
            };
            var egressColumn = {
                "id": "egress-id",
                "label": "Egresos",
                "type": "number"
            };
            var budgetEgressColumn = {
                "id": "budget-egress-id",
                "label": "Presupuesto Egresos",
                "type": "number"
            };
            var initialBalanceColumn = {
                "id": "initial-balance-id",
                "label": "Saldo Inicial",
                "type": "number"
            };
            var realBalanceColumn = {
                "id": "real-balance-id",
                "label": "Saldo Real",
                "type": "number"
            };
            columns.push(monthCol)

            if ($state.current.name === "resultStates.anualReport.graphic") {
                vm.title = "Estado de resultados";
                columns.push(ingressColumn)
                columns.push(egressColumn)
                columns.push(initialBalanceColumn)
                columns.push(realBalanceColumn)
            }
            if ($state.current.name === "budgetExecution.anualReport.graphic") {
                vm.title = "EJECUCIÓN PRESUPUESTARIA";
                columns.push(ingressColumn)
                columns.push(budgetIngressColumn)
                columns.push(egressColumn)
                columns.push(budgetEgressColumn)
            }
                return columns;
        }

        function defineColorsBars() {
            if ($state.current.name === "resultStates.anualReport.graphic") {
                return {
                    0: {color: '#43a047'},
                    1: {color: '#d50000'},
                    2: {color: '#b1bfca'},
                    3: {color: '#0d47a1'},
                }
            }
            if ($state.current.name === "budgetExecution.anualReport.graphic") {
                return {
                    0: {color: '#43a047'},
                    1: {color: '00701a'},
                    2: {color: '#d50000'},
                    3: {color: '#9b0000'},
                }
            }
        }

        vm.changeChartTypeIEB = function (type) {
            $timeout(function () {
                ingresosEgresosGraphInit(type)
            })
        };

        function ingresosEgresosGraphInit(type) {
            var reportPerMonth = vm.anualReportDashboardDTO.incomeEgressBudgetList;
            var rows = [];
            for (var i = 0; i < reportPerMonth.length; i++) {
                rows.push(colsPerMonthIEB(reportPerMonth[i]))
            }
            vm.dataIEB = {
                "type": type,
                "displayed": false,
                "data": {
                    "cols":defineLabelsToShow() ,
                    "rows": rows
                },
                "options": {
                    // "title": "Flujo de Ingresos y Egresos",
                    // "curveType": "function",
                    "sliceVisibilityThreshold": 0,
                    "legend": {"position": "bottom"},
                    'chartArea': {'width': '75%'},
                    "isStacked": "false",
                    "fill": 200000,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    "displayExactValues": true,
                    series: defineColorsBars(),
                    // "vAxis": {
                    //     "title": "Salesunit",
                    //     "gridlines": {
                    //         "count": 10
                    //
                    //     }
                    // },
                    // "hAxis": {
                    //     "title": "Date"
                    // }
                }
            }
        }

    }
})
();
