(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['$rootScope','Modal','$state','$localStorage','$timeout', '$scope', '$stateParams', 'entity', 'Article', 'Chapter', 'KeyWords', 'ArticleCategory'];

    function ArticleDialogController ($rootScope,Modal,$state,$localStorage,$timeout, $scope, $stateParams, entity, Article, Chapter, KeyWords, ArticleCategory) {
        var vm = this;
        vm.isReady = false;
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
            vm.article.articleCategories = [];
        }

        ArticleCategory.query({}, function (categories) {
            vm.categories = [];
            vm.keyWords = [];
            angular.forEach(categories, function (item, key) {
                vm.categories.push(item)
            });
            angular.forEach(vm.article.articleCategories, function (item, key) {
                angular.forEach(vm.categories, function (category, key) {
                    if(item.id==category.id){
                        var index = vm.categories.indexOf(category);
                        vm.categories.splice(index, 1);
                    }
                });
            });
         getKeyWords();

        }, onSaveError);


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function getKeyWords() {
            KeyWords.query({}, function (keyWords) {

                angular.forEach(keyWords, function (item) {
                    vm.keyWords.push(item)
                });
                angular.forEach(vm.article.keyWords, function (item, key) {
                    angular.forEach(vm.keyWords, function (keyWord, key) {
                        if(item.id==keyWord.id){
                            var index = vm.keyWords.indexOf(keyWord);
                            vm.keyWords.splice(index, 1);
                        }
                    });
                });
                vm.isReady = true;

            }, onSaveError);
        }

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
        vm.addToSelected = function (item,type) {
            var index;
            if(type===1){
                vm.article.articleCategories.push(item);
                index = vm.categories.indexOf(item);
                vm.categories.splice(index, 1);
            }else{
                vm.article.keyWords.push(item);
                index = vm.keyWords.indexOf(item);
                vm.keyWords.splice(index, 1);

            }

        };
        vm.deleteFromSelected = function (item,type) {
            var index;
            if(type===1){
                vm.categories.push(item);
                index = vm.article.articleCategories.indexOf(item);
                vm.article.articleCategories.splice(index, 1);

            }else{
                vm.keyWords.push(item);
                index = vm.article.keyWords.indexOf(item);
                vm.article.keyWords.splice(index, 1);

            }

        }

    }
})();
