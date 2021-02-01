(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MinutesController', MinutesController);

    MinutesController.$inject = ['$state', '$rootScope', 'CommonMethods', 'globalCompany', 'Modal', 'CondominiumRecord', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function MinutesController($state, $rootScope, CommonMethods, globalCompany, Modal, CondominiumRecord, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.condominiumRecords = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        $rootScope.active = "minutes";

        vm.links = {
            last: 0
        };

        var textDocumentName = "el documento";
        vm.textDocumentName2 = "documento";
        vm.textDocumentName3 = "documentos registrados";
        $rootScope.mainTitle =  "Documentos";

        vm.isReady = false;
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll() {
            CondominiumRecord.findByCompany({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId(),
                type: 2
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
                    vm.condominiumRecords.push(data[i]);
                }
                vm.isReady = true;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.condominiumRecords = [];
            loadAll();
        }

        vm.recordDetail = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id);
            $state.go('minutes-detail', {
                id: encryptedId
            })
        }

        vm.recordEdit = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id);
            $state.go('minutes.edit', {
                id: encryptedId
            })
        };

        vm.delete = function (entity) {
            Modal.confirmDialog("¿Está seguro que decia eliminar "+textDocumentName+"?", "Una vez eliminada no podrá recuperar el archivo", function () {
                CondominiumRecord.delete({id: entity.id}, function () {
                    CommonMethods.deleteFromArray(entity, vm.condominiumRecords);
                    Modal.toast("Se ha eliminado "+textDocumentName+" correctamente")
                })
            })
        };

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
