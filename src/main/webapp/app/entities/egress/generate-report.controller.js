(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressGenerateReportController', EgressGenerateReportController);

    EgressGenerateReportController.$inject = ['Company','$scope', '$state', 'Banco', 'Egress', 'ParseLinks', 'AlertService', 'paginationConstants', 'CommonMethods', 'Proveedor', '$rootScope', 'globalCompany'];

    function EgressGenerateReportController(Company,$scope, $state, Banco, Egress, ParseLinks, AlertService, paginationConstants, CommonMethods, Proveedor, $rootScope, globalCompany) {
        $rootScope.active = "reporteGastos";
        var vm = this;

        vm.propertyName = 'id';
        $rootScope.mainTitle = "Reporte de egresos";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.reverse = true;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

        vm.gastosQuantity = 0;
        vm.showNoResults = false;
        vm.hideReportForm = false;
        vm.loadingReport = false;
        vm.selectedProveedores = [];
        vm.selectedCampos = [];
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        };
        var date = new Date(),
            y = date.getFullYear(),
            m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };
        vm.translationCampos = {
            checkAll: "Selecciona todos",
            buttonDefaultText: "Selecciona los campos",
            uncheckAll: "Deseleccionar todos",
            selectionCount: "elementos seleccionados",
            dynamicButtonTextSuffix: "elementos seleccionados"
        }
        vm.translation = {
            checkAll: "Selecciona todos",
            buttonDefaultText: "Selecciona los proveedores",
            uncheckAll: "Deseleccionar todos",
            selectionCount: "elementos seleccionados",
            dynamicButtonTextSuffix: "elementos seleccionados"
        };

        loadProveedors();

        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        };

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


        function loadProveedors() {

            Proveedor.getAllForReport({companyId: globalCompany.getId()}).$promise.then(onSuccessProveedores);

            function onSuccessProveedores(data, headers) {
                vm.proveedores = data;
                vm.proveedoresMultiSelect = []
                angular.forEach(vm.proveedores, function (proveedor, key) {
                    var proveedorToshow = {}
                    proveedorToshow.label = proveedor.empresa;
                    proveedorToshow.id = proveedor.id;
                    vm.proveedoresMultiSelect.push(proveedorToshow)
                });
                var proveedorToshow = {};
                proveedorToshow.label = "Devoluciones de dinero";
                proveedorToshow.id = "devolucion";
                vm.proveedoresMultiSelect.push(proveedorToshow);
                loadCampos();
            }

        }

        function loadCampos() {
            vm.camposMultiSelect = [{id: 0, label: 'Concepto', attr: 'concept'}, {
                id: 1,
                label: 'Folio',
                attr: 'folio'
            }, {id: 2, label: 'Fecha de cobro', attr: 'date'}, {
                id: 3,
                label: 'Fecha de vencimiento',
                attr: 'expirationDate'
            }, {id: 4, label: 'Fecha pago realizado', attr: 'paymentDate'}, {
                id: 5,
                label: '# Factura',
                attr: 'billNumber'
            }, {id: 6, label: 'Referencia', attr: 'reference'}, {id: 7, label: 'Cuenta', attr: 'account'}, {id: 8, label: 'Monto', attr: 'total'}]
            loadAccounts();
        }

        function loadAccounts() {
            Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);
        }

        function onSuccessBancos(data, headers) {
            vm.bancos = data;
            Company.get({id:  globalCompany.getId()}).$promise.then(function (result) {
                vm.isReady = true;
                vm.companyName = result.name;
            });


        }

        vm.formatearNumero = function (nStr)  {

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
            AlertService.error(error.data.message);
        }

        vm.generateReport = function () {

            var selectedCampos = "";
            var selectedProveedores = "";
            angular.forEach(vm.selectedCampos, function (selectedCampo, key) {
                selectedCampos = selectedCampos + vm.camposMultiSelect[selectedCampo.id].attr + ",";
            });
            angular.forEach(vm.selectedProveedores, function (selectedProveedor, keyProveedor) {
                selectedProveedores = selectedProveedores + selectedProveedor.id + ",";

            });
            Egress.generateReport({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                empresas: selectedProveedores,
                selectedCampos: selectedCampos
            }).$promise.then(onSuccess);

            vm.gastosQuantity = 0;
            vm.isReady2 = false;
            vm.loadingReport = true;

            function onSuccess(data) {

                vm.egresses = data;
                console.log(vm.egresses)
                vm.superObject = moment(vm.dates.initial_time).format() +'}'+moment(vm.dates.final_time).format()+'}'+globalCompany.getId()+'}'+selectedProveedores+'}'+selectedCampos;
                vm.path = '/api/egresses/file/' + vm.superObject;
                vm.isReady2 = true;
                vm.hideReportForm = true;
                vm.loadingReport = false;
                if(data.egressByProveedor.length>0){

                    vm.showNoResults = false

                }else{
                    vm.showNoResults = true
                }

            }

        };

    }
})();
