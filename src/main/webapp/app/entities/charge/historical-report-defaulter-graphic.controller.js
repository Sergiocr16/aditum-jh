(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalDefaultersGraphicController', HistoricalDefaultersGraphicController).value('googleChartApiConfig', {
        version: '1.1',
        optionalSettings: {
            packages: ['bar'],
            language: 'es'
        }
    });

    HistoricalDefaultersGraphicController.$inject = ['$scope', '$uibModalInstance', '$state', 'globalCompany', '$stateParams', 'Dashboard', '$timeout'];

    function HistoricalDefaultersGraphicController($scope, $uibModalInstance, $state, globalCompany, $stateParams, Dashboard, $timeout) {
        var vm = this;
        vm.isReady = false;
        vm.year = moment(new Date()).format("YYYY");
        vm.charTypes = [{name: "Gráfico de barras", type: "ColumnChart"}, {name: "Gráfico de area", type: "AreaChart"}]
        vm.chartTypeDefaulters = vm.charTypes[0];
        var porCobrarDone = false;
        function getColumnChart(title, val) {
            return {"v": val, "f": title}
        }
        function colsPerMonthDefaulters(monthData, i) {
            var colums = [];
            colums.push({"f": monthsText[i]});
            colums.push(getColumnChart(monthData.totalHousesDefaulter + " unidades", monthData.totalHousesDefaulter));
            colums.push(getColumnChart(monthData.totalHousesOnTime + " unidades", monthData.totalHousesOnTime));
            return {"c": colums}
        }
        vm.showYearDefaulter = function () {
            vm.loadDefaulters(vm.yearDefaulter)
        }
        vm.changeChartTypeDefaulters = function (type) {
            $timeout(function () {
                defaultersGraphInit(type)
            })
        }
        var monthsText = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"]
        createYearsArrays()
        vm.changeMonthDefaulter = function (month) {
            $timeout(function () {
                defaulterMonthGraphInit(month)
            })
        };
        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        }

        function createYearsArrays() {
            var d = new Date();
            var year = d.getFullYear();
            var yearsDefaulters = [];
            yearsDefaulters.push(year)
            for (var i = 1; i <= 3; i++) {
                yearsDefaulters.push(year - i)
            }
            vm.yearsDefaulters = yearsDefaulters;
            vm.yearDefaulter = yearsDefaulters[0];
        }

        function createMonthsArray(year) {
            vm.monthsDefaultersOptions = [];
            var d = new Date();
            var n = d.getMonth();
            if (year != vm.year) {
                n = 11
            }
            var monthsDefaultersOptions = [];
            for (var i = 0; i <= n; i++) {
                monthsDefaultersOptions.push({id: i, text: monthsText[i] + " - " + year})
            }
            $timeout(function () {
                vm.monthsDefaultersOptions = monthsDefaultersOptions;
                if (year != vm.year) {
                    vm.monthDefaulter = vm.monthsDefaultersOptions[0].id;
                } else {
                    vm.monthDefaulter = vm.monthsDefaultersOptions[n].id;
                }
            }, 10)
        }

        function definePorCobrarDelMes() {
            if (porCobrarDone === false) {
                var m = new Date().getMonth();
                var monthData = vm.defaulters[m];
                porCobrarDone = true;
            }
        }

        vm.loadDefaulters = function (year) {
            Dashboard.defaultersHistoric({companyId: globalCompany.getId(), year: year}, function (result) {
                vm.defaulters = result;
                createMonthsArray(year);
                $timeout(function () {
                    defaultersGraphInit(vm.chartTypeDefaulters.type)
                    $timeout(function () {
                        vm.isReady = true;
                    }, 500)
                }, 110)
            });
        }

        function defaultersGraphInit(type) {
            var defaultersPerMonth = vm.defaulters;
            var rows = []
            for (var i = 0; i < defaultersPerMonth.length; i++) {
                rows.push(colsPerMonthDefaulters(defaultersPerMonth[i], i))
            }
            vm.dataDefaulters = {
                "type": type,
                "displayed": false,
                "cssStyle": "height:600px;width: 100%",
                "data": {
                    "cols": [
                        {
                            "id": "month",
                            "label": "Mes",
                            "type": "string",
                        },
                        {
                            "id": "defaulter-id",
                            "label": "Morosos históricos",
                            "type": "number"
                        },
                    ],
                    "rows": rows
                },
                "options": {
                    // titleTextStyle: {
                    //     color: "#4DB3A2",    // any HTML string color ('red', '#cc00cc')
                    //     fontName: "Open Sans", // i.e. 'Times New Roman'
                    //     fontSize: 15, // 12, 18 whatever you want (don't specify px)
                    //     bold: true,    // true or false
                    //     margin:20
                    // },
                    // "title": "MOROSIDAD POR UNIDADES PRIVATIVAS",
                    "curveType": "function",
                    "legend": {"position": "bottom"},
                    "isStacked": "true",
                    "sliceVisibilityThreshold": 0,
                    "fill": 10,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    'chartArea': {'width': '90%'},
                    "displayExactValues": true,
                    series: {
                        0: {color: '#e6693e'},
                    }
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
            };

        }

        function defaulterMonthGraphInit(month) {
            var monthData = vm.defaulters[month]
            var rows = [];
            var colums = [];
            colums.push({"v": "Liquidado"});
            colums.push({"v": monthData.total, "f": monthData.totalFormated});
            rows.push({"c": colums})
            var colums = [];
            colums.push({"v": "Por cobrar"});
            colums.push({"v": monthData.debt, "f": monthData.debtFormat});
            rows.push({"c": colums})
            vm.dataDefaulterPerMonth = {
                "type": "PieChart",
                "displayed": false,
                "cssStyle": "height:600px;width: 100%",
                "data": {
                    "cols": [
                        {
                            "id": "enable",
                            "label": "enable",
                            "type": "string"
                        },
                        {
                            "id": "enable-id",
                            "label": "Liquidado",
                            "type": "number"
                        },
                        {
                            "id": "disable-id",
                            "label": "Por cobrar",
                            "type": "number"
                        }
                    ],
                    "rows": rows
                },
                "options": {
                    "title": "",
                    "legend": {"position": "bottom"},
                    "isStacked": "false",
                    "fill": 200000,
                    "sliceVisibilityThreshold": 0,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    'chartArea': {'width': '90%', 'height': '78%'},
                    "displayExactValues": true,
                    colors: ['#0097a7', '#e6693e']
                }
            }
        }
        vm.loadDefaulters(vm.year)
    }
})
();
