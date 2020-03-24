(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ContractController', ContractController);

    ContractController.$inject = ['Contract', 'ParseLinks', 'AlertService', 'paginationConstants', 'CommonMethods', '$state', 'Modal', 'globalCompany','$rootScope'];

    function ContractController(Contract, ParseLinks, AlertService, paginationConstants, CommonMethods, $state, Modal, globalCompany,$rootScope) {

        var vm = this;
        vm.isReady = false;
        vm.contracts = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        $rootScope.active = "contract";
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        $rootScope.mainTitle = "Contratos";

        loadAll();

        function loadAll() {
            Contract.allByCompany({
                page: vm.page,
                size: vm.itemsPerPage,
                companyId: globalCompany.getId(),
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
                for (var i = 0; i < data.length; i++) {
                    vm.contracts.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.editContract = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id);
            $state.go('contract.edit', {
                id: encryptedId
            })
        };
        vm.detail = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id);
            $state.go('contract-detail', {
                id: encryptedId
            })
        };

        vm.delete = function (id) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el contrato?", "", function () {
                Contract.delete({id: id}, function () {
                    var object = {id: id};
                    CommonMethods.deleteFromArrayWithId(object, vm.contracts);
                    Modal.toast("Se ha eliminado el contrato correctamente.")
                })
            })
        }

        function reset() {
            vm.page = 0;
            vm.contracts = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
