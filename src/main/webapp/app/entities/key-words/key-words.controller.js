(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyWordsController', KeyWordsController);

    KeyWordsController.$inject = ['Modal','$rootScope','$state', 'KeyWords', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function KeyWordsController(Modal,$rootScope,$state, KeyWords, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "key-words";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        loadAll();


        function loadAll () {
            KeyWords.query({
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
                vm.keyWords = data;
                vm.isReady = true;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        vm.delete = function (keyWods) {

            Modal.confirmDialog("¿Está seguro que desea eliminar: " + keyWods.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    keyWods.deleted = 1;
                    KeyWords.update(keyWods, onSaveSuccess, onSaveError);
                });


        };

        function onSaveSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se ha eliminado la palabra clave correctamente.");
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        function onSaveError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
