(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleCategoryDeleteController',ArticleCategoryDeleteController);

    ArticleCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ArticleCategory'];

    function ArticleCategoryDeleteController($uibModalInstance, entity, ArticleCategory) {
        var vm = this;

        vm.articleCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ArticleCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
