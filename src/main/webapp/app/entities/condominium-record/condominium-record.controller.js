(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CondominiumRecordController', CondominiumRecordController);

    CondominiumRecordController.$inject = ['$scope', '$state', '$rootScope', 'CommonMethods', 'globalCompany', 'Modal', 'CondominiumRecord', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function CondominiumRecordController($scope, $state, $rootScope, CommonMethods, globalCompany, Modal, CondominiumRecord, ParseLinks, AlertService, paginationConstants) {

        var vm = this;
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.condominiumRecords = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        $rootScope.active = "records";
        $rootScope.mainTitle = "Actas condominales";
        vm.links = {
            last: 0
        };
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
                type: 1
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
            $state.go('condominium-record-detail', {
                id: encryptedId
            })
        }

        vm.recordEdit = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id);
            $state.go('condominium-record.edit', {
                id: encryptedId
            })
        };

        vm.delete = function (entity) {
            Modal.confirmDialog("¿Está seguro que decia eliminar el acta?", "Una vez eliminada no podrá recuperar el archivo", function () {
                CondominiumRecord.delete({id: entity.id}, function () {
                    CommonMethods.deleteFromArray(entity, vm.condominiumRecords);
                    Modal.toast("Se ha eliminado el acta correctamente")
                })
            })
        };
        vm.print = function (path) {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 7000);
            printJS({
                printable: path,
                type: 'pdf',
                modalMessage: "Obteniendo Estado de cuenta"
            })
        };
        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
