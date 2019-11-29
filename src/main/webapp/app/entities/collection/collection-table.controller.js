(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CollectionTableController', CollectionTableController);

    CollectionTableController.$inject = ['$timeout', 'ExcelExport', '$scope', '$state', 'Collection', 'ParseLinks', 'AlertService', '$rootScope', 'globalCompany','Modal'];

    function CollectionTableController($timeout, ExcelExport, $scope, $state, Collection, ParseLinks, AlertService, $rootScope, globalCompany,Modal) {
        var vm = this;
//        vm.exportToExcel=function(tableId){ // ex: '#my-table'
//                    var exportHref=ExcelExport.tableToExcel(tableId,'sheet name');
//                    $timeout(function(){location.href=exportHref;},100); // trigger download
//                }
        vm.isReady = false;
        $rootScope.mainTitle = "Tabla de cobranza";
        $rootScope.active = "collectionTable";
        vm.companyId = globalCompany.getId();
        vm.year = moment(new Date()).format("YYYY")
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        }

        vm.print = function (paymentId) {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 7000)
            printJS({
                printable: '/api/collections/file/' + globalCompany.getId() + '/' + vm.year,
                type: 'pdf',
                modalMessage: "Obteniendo tabla de cobranza"
            })
        }
        vm.nextYear = function () {
            return parseInt(vm.year) + 1;
        }
        vm.backYear = function () {
            return parseInt(vm.year) - 1;
        }

        vm.showNextYear = function () {

            vm.isReady = false;

            vm.year = parseInt(vm.year) + 1;
            loadAll(vm.year);
        }
        vm.showBackYear = function () {

            vm.isReady = false;

            vm.year = parseInt(vm.year) - 1;
            loadAll(vm.year);
        }
        loadAll(vm.year);
        vm.formatearNumero = function (nStr) {
            nStr = nStr + "";
            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }

        function loadAll() {
            Collection.getCollectionByYear({
                year: vm.year,
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.collections = data;
                vm.isReady = true;
            }

            function onError(error) {
                Modal.toast("Hubo un problema obteniendo la tabla de cobranza")
                vm.isReady = true;
            }
        }

    }
})();
