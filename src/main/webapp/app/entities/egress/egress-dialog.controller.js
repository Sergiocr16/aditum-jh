(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDialogController', EgressDialogController);

    EgressDialogController.$inject = ['CommonMethods', '$timeout', '$state', '$scope', '$stateParams', 'previousState', 'entity', 'Egress', 'Company', 'Principal', 'Proveedor', '$rootScope', 'Banco', 'EgressCategory'];

    function EgressDialogController(CommonMethods, $timeout, $state, $scope, $stateParams, previousState, entity, Egress, Company, Principal, Proveedor, $rootScope, Banco, EgressCategory) {
        var vm = this;
        $rootScope.active = "newEgress";


        vm.isAuthenticated = Principal.isAuthenticated;
        vm.egress = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.gastosVariables = [];
        vm.gastosFijos = [];
        vm.gastosOtros = [];
        CommonMethods.validateNumbers();
        CommonMethods.formatCurrencyInputs();
        $(function () {

        });


        setTimeout(function () {
            Proveedor.query({companyId: $rootScope.companyId}).$promise.then(onSuccessProveedores);

            function onSuccessProveedores(data, headers) {
                vm.proveedores = data;

                Banco.query({companyId: $rootScope.companyId}).$promise.then(onSuccessBancos);

                function onSuccessBancos(data, headers) {
                    vm.bancos = data;

                    EgressCategory.query({companyId: $rootScope.companyId}).$promise.then(onSuccessEgressCategories);
                }


            }
        }, 700)

        vm.hola = function(egress){
            console.log(egress)
        }
        function onSuccessEgressCategories(data, headers) {
            angular.forEach(data, function (value, key) {
                if (value.group == 'Gastos fijos') {
                    vm.gastosFijos.push(value)
                    console.log(vm.gastosFijos);
                }
                if (value.group == 'Gastos variables') {
                    vm.gastosVariables.push(value)
                }

                if (value.group == 'Otros gastos') {
                    vm.gastosOtros.push(value)
                }

            })
            vm.egressCategories = data;
            setTimeout(function () {
                $("#loadingIcon").fadeOut(300);
            }, 400)
            setTimeout(function () {
                $("#new_egress_form").fadeIn('slow');
            }, 900)
        }


        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            vm.egress.total = x1 + x2;
        }


        if (vm.egress.id != null) {
//          vm.egress.account = null;
//          vm.egress.paymentMethod = null;
//          vm.egress.paymentDate = null;
            vm.title = 'Reportar pago';
            vm.button = "Reportar";
            vm.picker3 = {
                datepickerOptions: {
                    minDate: vm.egress.date,
                    enableTime: false,
                    showWeeks: false,
                }
            }
            Proveedor.get({id: vm.egress.proveedor}, onSuccessProovedor)

            if (vm.egress.folio == null || vm.egress.folio == 'undefined') {
                vm.egress.folio = 'Sin Registrar'
            }
            if (vm.egress.billNumber == null || vm.egress.billNumber == 'undefined' || vm.egress.billNumber == '') {
                vm.egress.billNumber = 'Sin Registrar'
            }
        } else {
            vm.title = "Capturar gasto";
            vm.button = "Registrar";
        }
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function onSuccessProovedor(proovedor, headers) {formatearNumero
            vm.egress.empresa = proovedor.empresa;

        }
        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.confirmMessage = function () {
            if (vm.egress.id != null) {
                confirmReportPayment();

            } else {
                confirmCreateEgress();
            }
        }


        function confirmCreateEgress() {
            bootbox.confirm({
                message: '<div class="text-center gray-font font-15"><h3 style="margin-bottom:30px;">¿Está seguro que desea registrar este egreso?</h3><h5 class="bold">Una vez registrada esta información no se podrá editar</h5></div>',
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {

                    if (result) {
                        save()

                    } else {
                        vm.isSaving = false;

                    }
                }
            });
        }

        function confirmReportPayment() {
            bootbox.confirm({
                message: '<div class="text-center gray-font font-15"><h3 style="margin-bottom:30px;">¿Está seguro que desea reportar el pago de este egreso?</h3><h5 class="bold">Una vez registrada esta información no se podrá editar</h5></div>',
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {

                    if (result) {
                        save()

                    } else {
                        vm.isSaving = false;

                    }
                }
            });
        }

        function save() {
            CommonMethods.waitingMessage();
            var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
            var expirationTime = new Date(vm.egress.expirationDate).getTime();
            if (currentTime <= expirationTime) {
                vm.egress.state = 1;
            } else {
                vm.egress.state = 3;
            }
            if (vm.egress.paymentDate != null || vm.egress.paymentDate == 'undefined') {
                vm.egress.state = 2;
            }
            vm.isSaving = true;
            if (vm.egress.id != null) {
                Egress.update(vm.egress, onSaveReport, onSaveError);

            } else {
                vm.egress.companyId = $rootScope.companyId;
                vm.egress.paymentMethod = 0;
                vm.egress.account = 0;
                vm.egress.account = 0;

                Egress.save(vm.egress, onSaveSuccess, onSaveError);
            }
        }
        vm.hola(vm.egress.total)
        function onSaveReport(result) {
            bootbox.hideAll();
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress');
            toastr["success"]("Se reportó el pago correctamente");
            vm.isSaving = false;
        }

        function onSaveSuccess(result) {
            bootbox.hideAll();
            $scope.$emit('aditumApp:egressUpdate', result);
            $state.go('egress');
            toastr["success"]("Se registró el gasto correctamente");
            vm.isSaving = false;
        }

        function onSaveError() {
            bootbox.hideAll()
            toastr["error"]("Un error inesperado ocurrió");
            vm.isSaving = false;
        }

        vm.updatePicker = function () {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.egress.expirationDate == undefined ? new Date() : vm.egress.expirationDate,
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.egress.date,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }
        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.paymentDate = false;
        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
