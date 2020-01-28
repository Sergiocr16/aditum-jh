(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigController', RevisionConfigController);

    RevisionConfigController.$inject = ['Modal', 'globalCompany', '$state', 'CommonMethods', '$rootScope', 'RevisionConfig', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function RevisionConfigController(Modal, globalCompany, $state, CommonMethods, $rootScope, RevisionConfig, ParseLinks, AlertService, paginationConstants) {

        var vm = this;
        $rootScope.active = "revisionsConfig";
        $rootScope.mainTitle = "Configuración revisiones";
        vm.isReady = false;
        vm.revisionConfigs = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();
        vm.detailRevisionConfig = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('revision-config-detail', {
                id: encryptedId
            })
        }
        vm.editRevisionConfig = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('revision-config.edit', {
                id: encryptedId
            })
        }

        vm.delete = function (item) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la confirguración de revisión?", "", function () {
                Modal.showLoadingBar();
                RevisionConfig.delete({id: item.id},
                    function () {
                        CommonMethods.deleteFromArray(item, vm.revisionConfigs);
                        Modal.hideLoadingBar();
                    });
            })
        }


        function loadAll() {
            RevisionConfig.findByCompany({
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
                    vm.revisionConfigs.push(data[i]);
                }
                vm.isReady = true;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.revisionConfigs = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
