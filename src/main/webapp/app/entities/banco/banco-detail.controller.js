(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDetailController', BancoDetailController);

    BancoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Banco', 'Company', 'Egress', 'pagingParams', 'ParseLinks', 'Charge', 'Payment', 'BalanceByAccount', 'Transferencia', 'globalCompany'];

    function BancoDetailController($scope, $rootScope, $stateParams, previousState, entity, Banco, Company, Egress, pagingParams, ParseLinks, Charge, Payment, BalanceByAccount, Transferencia, globalCompany) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        $rootScope.active = "bancos";
        vm.banco = entity;
        vm.previousState = previousState.name;
        vm.isReady = false;
        vm.isConsulting = false;
        var unsubscribe = $rootScope.$on('aditumApp:bancoUpdate', function (event, result) {
            vm.banco = result;
        });
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        };
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.first_month_day = firstDay;
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };

        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        };
        vm.exportingExcel = false;
        vm.tableToExcel = function (table) {
            vm.notExportingExcel = false;
            vm.exportingExcel = true;
            setTimeout(function () {
                setTimeout(function(){
                    $scope.$apply(function(){
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
                        var workSheetName = "ESTADO DE CUENTA - "+vm.banco.beneficiario +" - del " +moment(vm.dates.initial_time).format("L") +" al "+moment(vm.dates.final_time).format("L");
                        if (!table.nodeType) table = document.getElementById(table)
                        var ctx = {worksheet: workSheetName || 'Worksheet', table: table.innerHTML}
                        var a = document.createElement('a');
                        a.href = uri + base64(format(template, ctx))
                        a.download = workSheetName + '.xls';
                        //triggering the function
                        a.click();
                        vm.exportingExcel = false;
                    }, 1)
                    vm.notExportingExcel = true;
                },500)
            })
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
                modalMessage: "Obteniendo reporte de egresos"
            })
        };

        getAccountStatus();

        function getAccountStatus() {
            var dateInitialCapital = new Date();
            dateInitialCapital.setDate(vm.dates.initial_time.getDate());
            dateInitialCapital.setMonth(vm.dates.initial_time.getMonth());
            dateInitialCapital.setFullYear(vm.dates.initial_time.getFullYear());
            dateInitialCapital.setDate(dateInitialCapital.getDate()-1);
            dateInitialCapital.setMinutes(0);
            dateInitialCapital.setSeconds(0);
            dateInitialCapital.setHours(0);
            vm.path = '/api/bancos/accountStatus/file/' + moment(vm.first_month_day).format() + "/" + moment(dateInitialCapital).format() + "/" + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + vm.banco.id;
            vm.first_month_day.setMonth(vm.dates.initial_time.getMonth());

            Banco.getAccountStatus({
                first_month_day: moment(vm.first_month_day).format(),
                final_capital_date: moment(dateInitialCapital).format(),
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                accountId: vm.banco.id
            }, function (data) {
                vm.banco = data;
                vm.movementsList =  vm.banco.movimientos;

                console.log(vm.banco)
                if(vm.isConsulting===false){
                    var banco;
                    vm.banco.saldo = vm.banco.totalBalance;
                    banco = vm.banco;
                    banco.movimientos = null;

                    Banco.update(banco, function () {
                        vm.isReady = true;
                    }, onError);
                }else{
                    vm.isReady = true;
                }
            });

        }
        vm.consult = function(){
            vm.isConsulting = true;
            getAccountStatus();
        }

        vm.stopConsulting = function () {
            var date = new Date(), y = date.getFullYear(), m = date.getMonth();
            var firstDay = new Date(y, m, 1);
            var lastDay = new Date(y, m + 1, 0);
            vm.dates = {
                initial_time: firstDay,
                final_time: lastDay
            };
            vm.isConsulting = false;
            vm.isReady = false;
            getAccountStatus();
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

        function onError(error) {

        }

        $scope.$on('$destroy', unsubscribe);

    }
})();
