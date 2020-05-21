(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HistoricalReportDefaulters', HistoricalReportDefaulters)

    HistoricalReportDefaulters.$inject = ['$rootScope', '$state', 'Charge', 'globalCompany', 'Company', 'CommonMethods', 'AlertService', '$scope'];

    function HistoricalReportDefaulters($rootScope, $state, Charge, globalCompany, Company, CommonMethods, AlertService, $scope) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.transition = transition;
        $rootScope.active = "historical-defaulters";
        vm.loadAll = loadAll;
        vm.month = new Date();
        vm.currentDate = new Date();
        vm.chargeType = -1;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        vm.simplify = false;
        vm.format = 'L';
        vm.exportingExcel = false;
        moment.locale("es");
        vm.locale = {
            formatDate: function(date) {
                var m = moment(date);
                return m.isValid() ? m.format(vm.format) : '';
            }
        };
        vm.changeFormat = function() {
            vm.format = 'MMMM';
            vm.hideDate = true;
            // $timeout(function() {
                vm.hideDate = false;
            // });
        };
        vm.createMonth = function () {
            vm.initial_time = new Date(vm.month.getFullYear(), vm.month.getMonth(), 1);
            vm.final_time = new Date(vm.month.getFullYear(), vm.month.getMonth() + 1, 0);
            vm.changeFormat()
            vm.loadAll()
        }
        vm.createMonth()

        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('owner-detail', {
                id: encryptedId
            })
        }
        vm.exportActions = {
            downloading: false,
            printing: false,
        };


        vm.tableToExcel = function (table) {
            vm.exportingExcel = true;
            setTimeout(function(){
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
                var workSheetName = vm.companyName + "- REPORTE DE MOROSIDAD HISTÓRICA - Previas al " + moment(vm.final_time).format("L");
                if (!table.nodeType) table = document.getElementById(table)
                var ctx = {worksheet: workSheetName || 'Worksheet', table: table.innerHTML}
                var a = document.createElement('a');
                a.href = uri + base64(format(template, ctx))
                a.download = workSheetName + '.xls';
                //triggering the function
                a.click();
                vm.exportingExcel = false;
            },1)
        }
        vm.loadAll();
        vm.showYearDefaulter = function () {
            vm.loadDefaulters(vm.yearDefaulter)
        }
        vm.print = function () {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 7000)
            printJS({
                printable: 'api/charges/chargesToPay/file/' + moment(vm.final_time).format() + '/' + vm.chargeType + '/byCompany/' + globalCompany.getId(),
                type: 'pdf',
                modalMessage: "Obteniendo reporte de cuotas por cobrar"
            })
        }
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        };
        vm.getCategory = function (type) {
            switch (type) {
                case -1:
                    return "TODAS"
                    break;
                case 1:
                    return "MANTENIMIENTO"
                    break;
                case 2:
                    return "EXTRAORDINARIA"
                    break;
                case 3:
                    return "ÁREAS COMUNES"
                    break;
                case 5:
                    return "MULTA";
                    break;
                case 6:
                    return "CUOTA AGUA";
                    break;
            }
        }

        function loadAll() {
            vm.isReady = false;
            vm.companyId = globalCompany.getId();
            if (vm.chargeType == 10) {
                vm.filtering = false;
            } else {
                vm.filtering = true;
            }
            vm.chargeTypeSetted = vm.chargeType;
            Charge.findHistoricalDefaultersReport({
                initial_time: moment(vm.initial_time).format(),
                final_time: moment(vm.final_time).format(),
                companyId: vm.companyId,
                charge_type: vm.chargeType
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data) {
                vm.report = data;
                Company.get({id: globalCompany.getId()}).$promise.then(function (result) {
                    vm.isReady = true;
                    vm.companyName = result.name;
                });
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
