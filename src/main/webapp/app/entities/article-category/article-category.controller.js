(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleCategoryController', ArticleCategoryController);

    ArticleCategoryController.$inject = ['Modal','$rootScope', '$localStorage','$state', 'ArticleCategory', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ArticleCategoryController(Modal,$rootScope, $localStorage, $state, ArticleCategory, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "article-category";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        loadAll();

        function loadAll () {
            ArticleCategory.query({
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
                vm.articleCategories = data;
                vm.isReady = true;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.delete = function (category) {

            Modal.confirmDialog("¿Está seguro que desea eliminar: " + category.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    category.deleted = 1;
                    ArticleCategory.update(category, onSaveSuccess, onSaveError);
                });


        };

        function onSaveSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se ha eliminado la categoría correctamente.");
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
