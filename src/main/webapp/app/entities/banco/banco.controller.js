(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoController', BancoController);

    BancoController.$inject = ['CommonMethods', '$state', '$location', 'Banco', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', 'globalCompany','Modal'];

    function BancoController(CommonMethods, $state, $location, Banco, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, globalCompany,Modal) {

        var vm = this;
        vm.location = $location.path();

        if (vm.location == "/banco") {
            $rootScope.active = "bancos";
        } else {
            $rootScope.active = "bancoConfiguration";
        }
        vm.isReady = false;
        $rootScope.mainTitle = "Cuentas";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.accountsQuantity = 0;

            loadAll();

        function loadAll() {
            Banco.query({
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
                vm.bancos = data;
                vm.page = pagingParams.page;
                vm.isReady = true;
                angular.forEach(data, function (value, key) {

//                   if(value.deleted==0){
                    vm.accountsQuantity = vm.accountsQuantity + 1;
//                   }
                })

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.sortBy = function (propertyName) {
            vm.reverse = (vm.propertyName === propertyName) ? !vm.reverse : false;
            vm.propertyName = propertyName;
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
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        vm.deleteBanco = function (banco) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la cuenta " + banco.beneficiario + "?","",
                function(){
                    Modal.showLoadingBar();
                    banco.deleted = 0;
                    Banco.update(banco, onDeleteSuccess, onSaveError);

                });

        };

        function onDeleteSuccess(result) {
            Modal.hideLoadingBar();
            loadAll();
            Modal.toast("Se eliminó la cuenta correctamente");
            $state.go('banco-configuration');
            vm.isSaving = false;
        }

        function onSaveError(error) {
            Modal.hideLoadingBar();
            Modal.toast("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
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
