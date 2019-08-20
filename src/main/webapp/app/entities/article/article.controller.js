(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleController', ArticleController);

    ArticleController.$inject = ['Modal','$rootScope','$localStorage','$state', 'Article', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ArticleController(Modal,$rootScope,$localStorage,$state, Article, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        $rootScope.active = "regulation";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.chapter = $localStorage.chapterSelected;
        vm.regulation = $localStorage.regulationSelected;
        vm.isReady = false;
        loadAll();

        function loadAll () {
            Article.query({
                chapterId: vm.chapter.id,
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
                vm.articles = data;
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

        vm.delete = function (article) {

            Modal.confirmDialog("¿Está seguro que desea eliminar: " + article.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar()
                    article.deleted = 1;
                    Article.update(article, onSaveSuccess, onSaveError);
                });


        };

        function onSaveSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se ha eliminado el artículo correctamente.");
            loadAll();
        }
        function onSaveError () {
            Modal.toast("Un error inesperado sucedió.");
        }


        vm.watchSubsections = function (article) {
            $localStorage.articleSelected = article;
            $state.go('subsection')
        }
        vm.back = function () {
            window.history.back();
        }
    }
})();
