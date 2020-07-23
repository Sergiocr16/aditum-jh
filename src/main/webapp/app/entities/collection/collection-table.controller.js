(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CollectionTableController', CollectionTableController);

    CollectionTableController.$inject = ['$timeout', 'ExcelExport', '$scope', '$state', 'Collection', 'ParseLinks', 'AlertService', '$rootScope', 'globalCompany', 'Modal', 'CommonMethods'];

    function CollectionTableController($timeout, ExcelExport, $scope, $state, Collection, ParseLinks, AlertService, $rootScope, globalCompany, Modal, CommonMethods) {
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


        // vm.tableToExcel = function (table, name) {
        //     console.log("a")
        //     var table = TableExport(document.getElementById("tableToExport"),
        //     {
        //         headers: true,                      // (Boolean), display table headers (th or td elements) in the <thead>, (default: true)
        //          footers: true,                      // (Boolean), display table footers (th or td elements) in the <tfoot>, (default: false)
        //         formats: ["xlsx", "csv", "txt"],    // (String[]), filetype(s) for the export, (default: ['xlsx', 'csv', 'txt'])
        //         filename: name,                     // (id, String), filename for the downloaded file, (default: 'id')
        //         bootstrap: true,                   // (Boolean), style buttons using bootstrap, (default: true)
        //         exportButtons: true,                // (Boolean), automatically generate the built-in export buttons for each of the specified formats (default: true)
        //         position: "bottom",                 // (top, bottom), position of the caption element relative to table, (default: 'bottom')
        //         ignoreRows: null,                   // (Number, Number[]), row indices to exclude from the exported file(s) (default: null)
        //         ignoreCols: null,                   // (Number, Number[]), column indices to exclude from the exported file(s) (default: null)
        //         trimWhitespace: true,               // (Boolean), remove all leading/trailing newlines, spaces, and tabs from cell text in the exported file(s) (default: false)
        //         RTL: false,                         // (Boolean), set direction of the worksheet to right-to-left (default: false)
        //         sheetname: name                   // (id, String), sheet name for the exported spreadsheet, (default: 'id')
        //     });
        //     /* convert export data to a file for download */
        //     var exportData = table.getExportData();
        //     var xlsxData = exportData.table.xlsx; // Replace with the kind of file you want from the exportData
        //     table.export2file(xlsxData.data, xlsxData.mimeType, xlsxData.filename, xlsxData.fileExtension, xlsxData.merges, xlsxData.RTL, xlsxData.sheetname)
        // }


        vm.tableToExcel = function (table) {
            vm.exportingExcel = true;
            setTimeout(function () {
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
            var workSheetName = "TABLA DE COBRANZA - " +vm.year;
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {worksheet: workSheetName || 'Worksheet', table: table.innerHTML}
            var a = document.createElement('a');
            a.href = uri + base64(format(template, ctx))
            a.download = workSheetName + '.xls';
            //triggering the function
            a.click();
                vm.exportingExcel = false;
            }, 1)
        }


        vm.exportTableToExcel = function (tableID, filename) {
            var downloadLink;
            var dataType = 'application/vnd.ms-excel';
            var tableSelect = document.getElementById(tableID);
            var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');

            // Specify file name
            filename = filename ? filename + '.xls' : 'excel_data.xls';

            // Create download link element
            downloadLink = document.createElement("a");

            document.body.appendChild(downloadLink);

            if (navigator.msSaveOrOpenBlob) {
                var blob = new Blob(['\ufeff', tableHTML], {
                    type: dataType
                });
                navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                // Create a link to the file
                downloadLink.href = 'data:' + dataType + ', ' + tableHTML;

                // Setting the file name
                downloadLink.download = filename;

                //triggering the function
                downloadLink.click();
            }
        }
    }
})();
