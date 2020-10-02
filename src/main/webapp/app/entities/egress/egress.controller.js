(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressController', EgressController);

    EgressController.$inject = ['AdministrationConfiguration', 'Modal', '$scope', '$state', 'Egress', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonMethods', 'Proveedor', '$rootScope', 'globalCompany'];

    function EgressController(AdministrationConfiguration, Modal, $scope, $state, Egress, ParseLinks, AlertService, paginationConstants, pagingParams, CommonMethods, Proveedor, $rootScope, globalCompany) {
        $rootScope.active = "egress";
        var vm = this;
        $rootScope.mainTitle = "Egresos";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
//        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.propertyName = 'id';
        vm.reverse = true;
        vm.consult = consult;
        vm.showFilterDiv = false;
        vm.dates = {
            initial_time: undefined,
            final_time: undefined
        };

        function loadAdminConfig() {

        }

        vm.isDisableButton = function () {
            if (vm.dates.initial_time == undefined || vm.dates.final_time == undefined) return true;
            return false;
        }

        loadProveedors();

        function loadProveedors() {
            Proveedor.query({companyId: globalCompany.getId()}).$promise.then(onSuccessProveedores);
            loadAdminConfig();

            function onSuccessProveedores(data, headers) {
                vm.proveedores = data;
                loadAll();
            }
        }

        function loadAll() {
            vm.title = 'Egresos';
            if (pagingParams.search == null) {
                Egress.query({
                    companyId: globalCompany.getId(),
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                }, onSuccess, onError);
            } else {
                var dates = pagingParams.search.split(" ");
                vm.dates = {
                    initial_time: moment(dates[0], 'DD/MM/YYYY').toDate(),
                    final_time: moment(dates[1], 'DD/MM/YYYY').toDate(),
                };
                vm.consult();
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.egresses = data;
                vm.page = pagingParams.page;
                vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                formatEgresos(vm.egresses);
            }
        }

        function onError(error) {
            Modal.toast("Un error inesperado sucedió");
            AlertService.error(error.data.message);
        }

        vm.sortBy = function (propertyName) {
            vm.reverse = (vm.propertyName === propertyName) ? !vm.reverse : false;
            vm.propertyName = propertyName;
        };

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
                if (value.currency == vm.companyConfig.currency) {
                    value.showOriginalCurrency = true;
                } else {
                    if (value.ammountDoubleMoney == null) {
                        value.showOriginalCurrency = true;
                    } else {
                        value.showOriginalCurrency = false;
                    }
                }
                angular.forEach(vm.proveedores, function (proveedor, key) {
                    if (proveedor.id == value.proveedor) {

                        value.proveedor = proveedor.empresa
                    }

                })
            });
            vm.isReady = true;
            vm.isReady2 = true;
        }


        vm.clickPopover = function (id) {

            $('[data-toggle="' + id + '"]').popover({
                placement: 'bottom',
                html: true,
                animation: true,
                trigger: "focus",
                content: '<div><a href="#"></a><div ><h4 >Reportar el pago de este egreso</h4> <h1 class="text-center"><button type=button" class="btn btn-primary" onclick="formats()" >Reportar pago</button></h1></div>'
            });
            $(document).on("click", ".popoversd", function () {

            })

        };

        vm.deleteEgress = function (egress) {
            Modal.confirmDialog("¿Está seguro que desea eliminar este egreso?", "",
                function () {
                    Modal.showLoadingBar();
                    egress.deleted = 1;

                    if (egress.paymentDate == "No pagado") {
                        egress.paymentDate = null;
                    }
                    if (egress.folio == "Sin Registrar") {
                        egress.folio = null;
                    }
                    if (egress.reference == "Sin Registrar") {
                        egress.reference = null;
                    }

                    Egress.update(egress, onDeleteSuccess, onError);

                });

        };


        function onDeleteSuccess(result) {
            Modal.hideLoadingBar();
            loadAll();
            Modal.toast("Se eliminó el egreso correctamente");
            vm.isSaving = false;
        }

        vm.detailEgress = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('egress-detail', {
                id: encryptedId
            })
        }

        vm.editEgress = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('egress.edit', {
                id: encryptedId
            })
        };

        function consult() {
            vm.isReady2 = false;
            Egress.findBetweenDatesByCompany({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                moment.locale('es');
                vm.egresses = data;
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.page = pagingParams.page;
                vm.title = 'Egresos entre:';
                vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");
                vm.isConsulting = true;
                formatEgresos(vm.egresses);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.payEgress = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('egress.edit', {
                id: encryptedId
            })
        }


        vm.stopConsulting = function () {
            vm.isReady2 = true;
            vm.dates = {
                initial_time: undefined,
                final_time: undefined
            };
            pagingParams.page = 1;
            pagingParams.search = null;
            vm.isConsulting = false;
            loadAll();
            vm.titleConsult = "";
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
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

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.isConsulting == true ? moment(vm.dates.initial_time).format('l') + " " + moment(vm.dates.final_time).format('l') : null,
            });
        }

    }
})();
