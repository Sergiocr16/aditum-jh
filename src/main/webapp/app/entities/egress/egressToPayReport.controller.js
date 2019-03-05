(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('EgressToPayReportController', EgressToPayReportController);

        EgressToPayReportController.$inject = ['Company', '$scope', '$state', 'Egress', 'AlertService', 'CommonMethods', 'Proveedor', '$rootScope', 'globalCompany'];

        function EgressToPayReportController(Company, $scope, $state, Egress, AlertService, CommonMethods, Proveedor, $rootScope, globalCompany) {
            $rootScope.active = "cuentasPorPagar";
            var vm = this;
            $rootScope.mainTitle = "Cuentas por pagar";
            vm.isReady = false;
            vm.isReady2 = false;
            vm.total = 0;
            vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

            vm.exportActions = {
                downloading: false,
                printing: false,
                sendingEmail: false,
            };
            var date = new Date(),
                y = date.getFullYear(),
                m = date.getMonth();
            var lastDay = new Date(y, m + 1, 0);
            vm.final_time = lastDay;
            loadProveedors();

            function loadProveedors() {
                Proveedor.query({companyId: globalCompany.getId()}).$promise.then(onSuccessProveedores);

                function onSuccessProveedores(data, headers) {
                    vm.proveedores = data;
                    vm.loadAll();
                }
            }

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


            vm.loadAll = function() {
                vm.total = 0;
                vm.isReady2 = false;
                vm.title = 'Egresos';
                Egress.getEgressToPay({
                    companyId: globalCompany.getId(),
                    final_time: moment(vm.final_time).format(),
                }, onSuccess, onError);
            }


            function onSuccess(data) {
                vm.path = '/api/egresses/reportEgressToPay/file/' + moment(vm.final_time).format() +"/byCompany/" + globalCompany.getId();
                vm.egresses = data;
                Company.get({id: globalCompany.getId()}).$promise.then(function (result) {
                    vm.companyName = result.name;
                    formatEgresos(vm.egresses);
                });

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

            function formatEgresos(egresses) {

                angular.forEach(egresses, function (value, key) {

                    if (value.paymentDate == null || value.paymentDate == 'undefined') {
                        value.paymentDate = "No pagado";
                    }
                    if (value.folio == null || value.folio == 'undefined') {
                        value.folio = 'Sin Registrar'
                    }
                    if (value.reference == null || value.reference == 'undefined') {
                        value.reference = 'Sin Registrar'
                    }
                    angular.forEach(vm.proveedores, function (proveedor, key) {


                        if (proveedor.id == value.proveedor) {

                            value.proveedor = proveedor.empresa
                        }

                    });
                    vm.total = vm.total + parseInt(value.total);
                });
                vm.isReady = true;
                vm.isReady2 = true;
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

        }
    }

)();
