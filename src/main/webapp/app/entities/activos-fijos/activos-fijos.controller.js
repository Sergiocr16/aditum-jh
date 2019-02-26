(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivosFijosController', ActivosFijosController);

    ActivosFijosController.$inject = ['$state', 'ActivosFijos', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$rootScope','Modal','globalCompany'];

    function ActivosFijosController($state, ActivosFijos, ParseLinks, AlertService, paginationConstants, pagingParams,$rootScope,Modal,globalCompany) {

        var vm = this;
        $rootScope.active = "activosFijos";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        loadAll();

        function loadAll () {
            console.log(globalCompany)
            ActivosFijos.query({
                companyId: globalCompany.getId(),
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
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
                vm.activosFijos = data;
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
        vm.delete = function (activoFijo) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el activo fijo " + activoFijo.nombre + "?","",
                function(){
                    Modal.showLoadingBar();
                    activoFijo.deleted = 0;
                    ActivosFijos.update(activoFijo, onDeleteSuccess, onSaveError);

                });

        };

        function onSaveError(error) {
            Modal.hideLoadingBar();
            Modal.toast("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
        }

        function onDeleteSuccess(result) {
            Modal.hideLoadingBar();
            loadAll();
            Modal.toast("Se eliminó el activo fijo correctamente");
            vm.isSaving = false;
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
