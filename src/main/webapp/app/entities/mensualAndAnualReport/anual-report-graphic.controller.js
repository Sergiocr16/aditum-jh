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

    AnualReportGraphicController.$inject = ['$scope', '$rootScope', '$uibModalInstance', 'LoginService', '$state', 'Dashboard', 'globalCompany', 'Modal', '$timeout','$stateParams'];

    function AnualReportGraphicController($scope, $rootScope, $uibModalInstance, LoginService, $state, Dashboard, globalCompany, Modal, $timeout,$stateParams) {
        var vm = this;
        vm.isReady = false;
        vm.year = $stateParams.year;
        console.log(vm.year)
        vm.charTypes = [{name: "Gráfico de barras", type: "ColumnChart"}, {name: "Gráfico de area", type: "AreaChart"}]
        vm.chartTypeEIB = vm.charTypes[0];
        var ieDone = false;
        var monthsText = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"]
        loadGraphFlujoIngresosYEgresos(vm.year, vm.chartTypeEIB.type);

        vm.showYearIEB = function () {
            loadGraphFlujoIngresosYEgresos(vm.yearIEB);
        };

        vm.clear = function() {
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
            colums.push(getColumnChart(monthData.incomeTotalformated, monthData.incomeTotal));
            colums.push(getColumnChart(monthData.egressTotalformated, monthData.egressTotal));
            colums.push(getColumnChart(monthData.budgetTotalformated, monthData.budgetTotal));

            return {"c": colums}
        }

        function colsPerMonthDefaulters(monthData, i) {
            var colums = [];
            colums.push({"f": monthsText[i]});
            colums.push(getColumnChart(monthData.totalHousesDefaulter + " unidades", monthData.totalHousesDefaulter));
            colums.push(getColumnChart(monthData.totalHousesOnTime + " unidades", monthData.totalHousesOnTime));
            return {"c": colums}
        }

        function colsPerMonthVisitors(monthData) {
            var colums = [];
            colums.push({"f": monthData.name});
            colums.push({"v": monthData.value});
            return {"c": colums}
        }

        vm.changeChartTypeIEB = function (type) {
            $timeout(function () {
                ingresosEgresosGraphInit(type)
            })
        };

        function ingresosEgresosGraphInit(type) {
            var reportPerMonth = vm.anualReportDashboardDTO.incomeEgressBudgetList;
            var rows = []
            for (var i = 0; i < reportPerMonth.length; i++) {
                rows.push(colsPerMonthIEB(reportPerMonth[i]))
            }
            vm.dataIEB = {
                "type": type,
                "displayed": false,
                "data": {
                    "cols": [
                        {
                            "id": "month",
                            "label": "Mes",
                            "type": "string",
                        },
                        {
                            "id": "ingress-id",
                            "label": "Ingresos",
                            "type": "number"
                        },
                        {
                            "id": "egress-id",
                            "label": "Egresos",
                            "type": "number"
                        },
                        {
                            "id": "budget-id",
                            "label": "Presupuesto",
                            "type": "number"
                        },
                    ],
                    "rows": rows
                },
                "options": {
                    "sliceVisibilityThreshold":0,
                    "legend": {"position": "bottom"},
                    'chartArea': {'width': '97%'},
                    "isStacked": "false",
                    "fill": 200000,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    "displayExactValues": true,
                    series: {
                        0: {color: '#009688'},
                        1: {color: '#cb5a5e'},
                        2: {color: '#1c91c0'},
                    }
                }
            }
        }

    }
})
();
