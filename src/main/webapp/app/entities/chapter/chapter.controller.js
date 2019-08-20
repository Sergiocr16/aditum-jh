(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChapterController', ChapterController);

    ChapterController.$inject = ['Modal','$rootScope','$localStorage','$state', 'Chapter', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ChapterController(Modal,$rootScope,$localStorage,$state, Chapter, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "regulation";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        vm.regulation = $localStorage.regulationSelected;
        loadAll();

        function loadAll() {
            Chapter.query({
                regulationId: vm.regulation.id,
                page: pagingParams.page - 1,
                size: 500,
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
                vm.chapters = data;
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

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.watchArticules = function (chapter) {
            $localStorage.chapterSelected = chapter;
            $state.go('article')
        }

        vm.delete = function (chapter) {

            Modal.confirmDialog("¿Está seguro que desea eliminar: " + chapter.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar()
                    chapter.deleted = 1;
                    Chapter.update(chapter, onSaveSuccess, onSaveError);
                });


        };

        function onSaveSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se ha eliminado el capítulo correctamente.");
            loadAll();
        }
        function onSaveError () {
            Modal.toast("Un error inesperado sucedió.");
        }

        vm.back = function () {
            window.history.back();
        }
    }
})();
