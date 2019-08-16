(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleCategoryDialogController', ArticleCategoryDialogController);

    ArticleCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArticleCategory'];

    function ArticleCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ArticleCategory) {
        var vm = this;

        vm.articleCategory = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.articleCategory.id !== null) {
                ArticleCategory.update(vm.articleCategory, onSaveSuccess, onSaveError);
            } else {
                ArticleCategory.save(vm.articleCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:articleCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
