(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnualReportController', AnualReportController);

    AnualReportController.$inject = ['Company','AlertService', '$rootScope', 'Principal', 'MensualAndAnualReport', '$scope', 'Presupuesto', 'globalCompany','Modal'];

    function AnualReportController(Company,AlertService, $rootScope, Principal, MensualAndAnualReport, $scope, Presupuesto, globalCompany,Modal) {
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
        vm.rowsQuantity = vm.month + 4;
        vm.showPresupuestoFields = false;
        var withPresupuestos = 3;
        vm.expanding = false;
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
        }



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
            console.log(data)
            vm.report = data;
            vm.superObject =  vm.actualMonthFormatted + '}' + vm.companyId +'}'+ withPresupuestos;

            vm.path = '/api/anualReport/file/' + vm.superObject;
            Company.get({id: globalCompany.getId()}).$promise.then(function (result) {
                vm.isReady = true;
                vm.companyName = result.name;
            });
            vm.totalFlujo = vm.report.allIngressAcumulado - vm.report.allEgressAcumulado;

        }

        vm.showWithBudget = function () {
            Presupuesto.query({companyId: globalCompany.getId()}, function (result) {
                if (result.length > 0) {
                    var yearExist = 0;
                    angular.forEach(result, function (value, key) {
                        if (value.anno == dateMonthDay.getFullYear()) {
                            yearExist++;
                            $("#reportResults").fadeOut(0);
                            $("#loadingIcon2").fadeIn(0);
                            withPresupuestos = 2;
                            vm.loadAll();
                            vm.showPresupuestoFields = !vm.showPresupuestoFields;
                        }

                    })
                    if (yearExist == 0) {
                        Modal.toast("No existe un presupuesto del " +vm.actualMonth.getFullYear() + " registrado.");
                    }
                } else {
                    Modal.toast("No existe un presupuesto del " +vm.actualMonth.getFullYear() + " registrado.");
                }

            });


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
