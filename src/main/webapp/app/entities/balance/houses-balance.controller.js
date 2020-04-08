(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HousesBalanceController', HousesBalanceController);

    HousesBalanceController.$inject = ['$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'globalCompany'];

    function HousesBalanceController($state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, globalCompany) {
        $rootScope.active = "balance";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        $rootScope.mainTitle = "Saldo de filiales";
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.estado = "";
        vm.deudas = "2";
        vm.status = "2";
        vm.ocultarACondos = false;
        loadAll();
        if (globalCompany.getId() > 2) {
            vm.ocultarACondos = true;
        }
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
            var workSheetName = "Saldo de filiales - " + moment(new Date()).format("L");
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {worksheet: workSheetName || 'Worksheet', table: table.innerHTML}
            var a = document.createElement('a');
            a.href = uri + base64(format(template, ctx))
            a.download = workSheetName + '.xls';
            //triggering the function
            a.click();
        }
        vm.defineBalanceClass = function (balance) {
            var b = parseInt(balance);
            if (b != 0) {
                if (b > 0) {
                    return "greenBalance";
                } else {
                    return "redBalance";
                }
            }
        }

        vm.editHouse = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('house.edit', {
                id: encryptedId
            })
        }
        vm.filterByState = function () {
            if(vm.status=="2"){
                vm.estado = "";
            }else{
                vm.estado = vm.status;
            }
        };
        vm.filterByDeuda = function () {
            if(vm.deudas=="2"){
                vm.deudaFilter = "";
            }else{
                vm.deudaFilter = vm.deudas ;
            }
        };

        function loadAll() {
            Balance.queryBalances({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {

                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function (value, key) {
                    if(value.balance.total<0){
                        value.debit = 1;
                    }else{
                        value.debit = 0;
                    }
                });
                vm.houses = data;
                vm.page = pagingParams.page;
                vm.isReady = true;
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


        vm.showKeys = function (house_number, securityKey, emergencyKey) {
            if (securityKey == null || emergencyKey == null || securityKey == "" || emergencyKey == "") {
                Modal.toast("Esta casa aún no tiene claves de seguridad asignadas.");
            } else {
                bootbox.dialog({
                    message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div> <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de seguridad: <span class="font-20 bold" id="security_key">1134314</span></h1></div> <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de emergencia: <span class="font-20 bold" id="emergency_key">1134314</span></h1></div>',
                    closeButton: false,
                    buttons: {
                        confirm: {
                            label: 'Ocultar',
                            className: 'btn-success'
                        }
                    },
                })
                document.getElementById("key_id_house").innerHTML = "" + house_number;
                document.getElementById("security_key").innerHTML = "" + securityKey;
                document.getElementById("emergency_key").innerHTML = "" + emergencyKey;
            }
        }
        vm.showLoginCode = function (house_number, codeStatus, loginCode) {
            var estado = "";
            if (loginCode == null) {
                Modal.toast("Esta casa aún no tiene un código de iniciación asignado.");
            } else {

                if (codeStatus == false || codeStatus == 0) {
                    estado = 'No activada'
                } else {
                    estado = "Activada"
                }
                ;
                bootbox.dialog({
                    message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div> <div class="text-center gray-font font-15"> <h1 class="font-20">Código de iniciación: <span class="font-15 bold" id="login_code">1134314</span></h1></div> <div class="text-center gray-font font-15"> <h1 class="font-20">Estado: <span class="font-15 bold" id="code_status">1134314</span></h1></div>',
                    closeButton: false,
                    buttons: {
                        confirm: {
                            label: 'Ocultar',
                            className: 'btn-success'
                        }
                    },
                })

                document.getElementById("key_id_house").innerHTML = "" + house_number;
                document.getElementById("login_code").innerHTML = "" + loginCode;
                document.getElementById("code_status").innerHTML = "" + estado;
            }
        }

    }
})();
