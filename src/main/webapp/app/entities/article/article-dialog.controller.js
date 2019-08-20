(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['$rootScope','Modal','$state','$localStorage','$timeout', '$scope', '$stateParams', 'entity', 'Article', 'Chapter', 'KeyWords', 'ArticleCategory'];

    function ArticleDialogController ($rootScope,Modal,$state,$localStorage,$timeout, $scope, $stateParams, entity, Article, Chapter, KeyWords, ArticleCategory) {
        var vm = this;
        vm.isReady = true;
        vm.article = entity;
        vm.save = save;
        $rootScope.active = "regulation";
        vm.chapter = $localStorage.chapterSelected;
        vm.regulation = $localStorage.regulationSelected;
        if (vm.article.id !== null) {
            vm.button = "Editar";
            $rootScope.mainTitle = "Editar árticulo - " + vm.chapter.name+" - " + vm.regulation.name;
        } else {
            $rootScope.mainTitle = "Registrar árticulo - " + vm.chapter.name +" - " + vm.regulation.name;
            vm.button = "Registrar";
        }


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save () {
            Modal.showLoadingBar()
            vm.isSaving = true;
            if (vm.article.id !== null) {
                Article.update(vm.article, onSaveSuccess, onSaveError);
            } else {
                vm.article.chapterId = vm.chapter.id;
                vm.article.deleted = 0;
                Article.save(vm.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            $state.go('article');
            Modal.toast("Se ha gestionado el artículo correctamente.");
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
