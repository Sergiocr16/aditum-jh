(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsectionController', SubsectionController);

    SubsectionController.$inject = ['$rootScope','$localStorage','$state', 'Subsection', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function SubsectionController($rootScope,$localStorage,$state, Subsection, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "regulation";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        vm.article = $localStorage.articleSelected;
        vm.chapter = $localStorage.chapterSelected;
        vm.regulation = $localStorage.regulationSelected;
        loadAll();

        function loadAll () {
            Subsection.query({
                articleId: vm.article.id,
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
                vm.subsections = data;
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

        vm.delete = function (subsection) {

            Modal.confirmDialog("¿Está seguro que desea eliminar: " + subsection.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar()
                    subsection.deleted = 1;
                    Subsection.update(subsection, onSaveSuccess, onSaveError);
                });


        };

        function onSaveSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se ha eliminado el inciso correctamente.");
            loadAll();
        }
        function onSaveError () {
            Modal.toast("Un error inesperado sucedió.");
        }

    }
})();
