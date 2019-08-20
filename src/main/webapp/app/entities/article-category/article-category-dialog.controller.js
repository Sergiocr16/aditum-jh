(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleCategoryDialogController', ArticleCategoryDialogController);

    ArticleCategoryDialogController.$inject = ['Modal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArticleCategory'];

    function ArticleCategoryDialogController (Modal,$timeout, $scope, $stateParams, $uibModalInstance, entity, ArticleCategory) {
        var vm = this;

        vm.articleCategory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.isReady = true;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        if (vm.articleCategory.id !== null) {
            vm.title = "Editar categoría de artículos";
            vm.button = "Editar";

        } else {

            vm.title = "Registrar categoría de artículos ";
            vm.button = "Registrar";

        }

        function save () {
            vm.isSaving = true;
            Modal.showLoadingBar()
            if (vm.articleCategory.id !== null) {
                ArticleCategory.update(vm.articleCategory, onSaveSuccess, onSaveError);
            } else {
                vm.articleCategory.deleted = 0;
                ArticleCategory.save(vm.articleCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:articleCategoryUpdate', result);
            $uibModalInstance.close(result);
            Modal.hideLoadingBar();
            Modal.toast("Se ha gestionado la categoría correctamente.");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
