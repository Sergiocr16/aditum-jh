(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DashboardController', DashboardController).value('googleChartApiConfig', {
        version: '1.1',
        optionalSettings: {
            packages: ['bar'],
            language: 'es'
        }
    });

    DashboardController.$inject = ['$scope', '$rootScope', 'Principal', 'LoginService', '$state', 'Dashboard', 'globalCompany', 'Modal', '$timeout', 'CommonAreaReservations'];

    function DashboardController($scope, $rootScope, Principal, LoginService, $state, Dashboard, globalCompany, Modal, $timeout, CommonAreaReservations) {
        var vm = this;
        $rootScope.active = "dashboard";
        $rootScope.mainTitle = "Dashboard";

        vm.ready = false;
        vm.year = moment(new Date()).format("YYYY");
        vm.visitorTitle = "De la semana";
        vm.charTypes = [{name: "Gráfico de barras", type: "ColumnChart"}, {name: "Gráfico de area", type: "AreaChart"}]
        vm.chartTypeEIB = vm.charTypes[0];
        vm.chartTypeDefaulters = vm.charTypes[1];
        vm.visitantState = 1;

        var ieDone = false;
        var porCobrarDone = false;

        var monthsText = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"]
        vm.loadAll = function () {
            Dashboard.query({companyId: globalCompany.getId()}, function (result) {
                vm.dashboard = result;
                vm.visitorsPerMonth = vm.dashboard.visitorsPerMonth;
                vm.housesPercentage = ((vm.dashboard.houseQuantity * 100) / vm.dashboard.totalHouses).toFixed(2)
                loadGraphFlujoIngresosYEgresos(vm.year, vm.chartTypeEIB.type);
                vm.loadDefaulters(vm.year)
                visitantsGraphInit();
                residentsEnabledGraphInit();
                vehiculesEnabledGraphInit();
                createYearsArrays();
                initCalendar();

            });
        };

        vm.changeMonthDefaulter = function (month) {
            $timeout(function () {
                defaulterMonthGraphInit(month)
            })
        };

        function createMonthsArray(year) {
            vm.monthsDefaultersOptions = [];
            var d = new Date();
            var n = d.getMonth();
            if (year != vm.year) {
                n = 12
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

        vm.changeYear = function () {
            Dashboard.query({companyId: globalCompany.getId()}, function (result) {
                vm.dashboard = result;
                loadGraphFlujoIngresosYEgresos(vm.year, vm.chartTypeEIB.type);
            });
        }

        vm.loadDefaulters = function (year) {
            Dashboard.defaulters({companyId: globalCompany.getId(), year: year}, function (result) {
                vm.defaulters = result;
                createMonthsArray(year);
                definePorCobrarDelMes()
                $timeout(function () {
                    defaultersGraphInit(vm.chartTypeDefaulters.type)
                    $timeout(function(){
                        vm.isReady = true;
                    },500)
                }, 110)
            });
        }

        function definePorCobrarDelMes() {
            if (porCobrarDone === false) {
                var m = new Date().getMonth();
                var monthData = vm.defaulters[m];
                vm.porCobrar = monthData.debtFormat;
                porCobrarDone = true;
            }
        }

        vm.updateWeekData = function () {
            vm.visitorTitle = "De la semana";
            vm.visitantState = 1;

            Dashboard.updateWeek({companyId: globalCompany.getId()}, function (result) {
                vm.visitorsPerMonth = result;
                $timeout(function () {
                    visitantsGraphInit()
                })
            });
        }
        vm.updateMonthData = function () {
            vm.visitorTitle = "Del mes";
            vm.visitantState = 2;

            Dashboard.updateMonth({companyId: globalCompany.getId()}, function (result) {
                vm.visitorsPerMonth = result;
                $timeout(function () {
                    visitantsGraphInit()
                })
            });
        }
        vm.updateYearData = function () {
            vm.visitorTitle = "Del año";
            vm.visitantState = 3;
            Dashboard.updateYear({companyId: globalCompany.getId()}, function (result) {
                vm.visitorsPerMonth = result;
                $timeout(function () {
                    visitantsGraphInit()
                })
            });
        }


        vm.showYearIEB = function () {
            loadGraphFlujoIngresosYEgresos(vm.yearIEB);
        }


        vm.showYearDefaulter = function () {
            vm.loadDefaulters(vm.yearDefaulter)
        }


        function loadGraphFlujoIngresosYEgresos(year, type) {
            Dashboard.flujoIngresosEgresos({companyId: globalCompany.getId(), year: year}, function (result) {
                vm.anualReportDashboardDTO = result;
                defineEICurrentMonth();
                ingresosEgresosGraphInit(type)
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

        vm.loadAll();

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
        }
        vm.changeChartTypeDefaulters = function (type) {
            $timeout(function () {
                defaultersGraphInit(type)
            })
        }

        function vehiculesEnabledGraphInit() {
            var rows = [];
            var colums = [];
            colums.push({"v": "Habilitados"});
            colums.push({"v": vm.dashboard.enableVehicleuQantity});
            rows.push({"c": colums})
            var colums = [];
            colums.push({"v": "Deshabilitados"});
            colums.push({"v": vm.dashboard.disableVehicleQuantity});
            rows.push({"c": colums})
            vm.vehiculeGraph = {
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
                            "label": "Habilitados",
                            "type": "number"
                        },
                        {
                            "id": "disable-id",
                            "label": "Deshabilitado",
                            "type": "number"
                        }
                    ],
                    "rows": rows
                },
                "options": {
                    // "title": "Vehículos",
                    "legend": {"position": "bottom"},
                    "isStacked": "false",
                    'chartArea': {'width': '90%', 'height': '78%'},
                    "fill": 200000,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    "displayExactValues": true,
                    colors: ['#ae52d4', '#7b1fa2', '#ec8f6e', '#f3b49f', '#f6c7b6']
                }
            }
        }

        function residentsEnabledGraphInit() {
            var rows = [];
            var colums = [];
            colums.push({"v": "Habilitados"});
            colums.push({"v": vm.dashboard.enableResidentQuantity});
            rows.push({"c": colums})
            var colums = [];
            colums.push({"v": "Deshabilitados"});
            colums.push({"v": vm.dashboard.disableResidentQuantity});
            rows.push({"c": colums})
            vm.residentGraph = {
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
                            "label": "Habilitados",
                            "type": "number"
                        },
                        {
                            "id": "disable-id",
                            "label": "Deshabilitado",
                            "type": "number"
                        }
                    ],
                    "rows": rows
                },
                "options": {
                    // "title": "Residentes",
                    "legend": {"position": "bottom"},
                    "isStacked": "false",
                    'chartArea': {'width': '90%', 'height': '78%'},

                    "fill": 200000,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    "displayExactValues": true,
                    colors: ['#ae52d4', '#7b1fa2', '#ec8f6e', '#f3b49f', '#f6c7b6']
                }
            }
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
                            "label": "Morosos",
                            "type": "number"
                        },
                        {
                            "id": "Vigentes-id",
                            "label": "Vigentes",
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
                    "fill": 10,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    'chartArea': {'width': '90%'},
                    "displayExactValues": true,
                    series: {
                        0: {color: '#0097a7'},
                        1: {color: '#e6693e'},
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

            defaulterMonthGraphInit(vm.monthDefaulter)
        }


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
                    // "title": "Flujo de Ingresos y Egresos",
                    // "curveType": "function",
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

        function visitantsGraphInit() {
            var visitorsPerMonth = vm.visitorsPerMonth;
            var rows = [];
            for (var i = 0; i < visitorsPerMonth.length; i++) {
                rows.push(colsPerMonthVisitors(visitorsPerMonth[i]))
            }
            vm.dataVisitors = {
                "type": "AreaChart",
                "displayed": false,
                "data": {
                    "cols": [
                        {
                            "id": "month",
                            "label": "Mes",
                            "type": "string",
                        },
                        {
                            "id": "quantity-id",
                            "label": "Cantidad",
                            "type": "number"
                        },
                    ],
                    "rows": rows
                },
                "options": {
                    "title": vm.visitorTitle,
                    "curveType": "function",
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    'chartArea': {'width': '95%'},
                    // pointSize: 2,
                    "legend": {"position": "bottom"},
                    "isStacked": "false",
                    "fill": 200000,
                    "displayExactValues": true,
                    series: {
                        0: {color: '#009688'}
                    }
                }
            }
        }

        function initCalendar() {
            /* config object */
            vm.uiConfig = {
                calendar: {
                    events: function (start, end, timezone, callback) {
                        var events = [];
                        CommonAreaReservations.getLastAcceptedReservations({
                            companyId: globalCompany.getId()
                        }, function (data) {
                            angular.forEach(data, function (value) {
                                var color;
                                if (value.status == 1) {
                                    color = '#ef5350'
                                } else if (value.status == 2) {
                                    color = '#42a5f5'
                                }
                                events.push({
                                    id: value.id,
                                    commonAreaId: value.commonAreaId,
                                    title: value.commonArea.name + " - " + value.resident.name + " " + value.resident.lastname + " - Filial " + value.house.housenumber,
                                    start: new Date(value.initalDate),
                                    end: new Date(value.finalDate),
                                    description: 'This is a cool event',
                                    color: color,
                                    status: value.status
                                })
                            });
                            callback(events);
                        });
                    },
                    columnHeader: false,
                    dayClick: vm.onDayClick,
                    editable: false,
                    header: false,
                    height: 257,
                    eventClick: vm.alertOnEventClick,
                    eventDrop: vm.alertOnDrop,
                    eventResize: vm.alertOnResize,
                    eventRender: vm.eventRender,
                    defaultView: 'listYear',
                    default: 'bootstrap3',
                }
            };
        }


    }
})
();
