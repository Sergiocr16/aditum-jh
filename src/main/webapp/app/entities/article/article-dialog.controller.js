(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['Subsection', 'Regulation', '$rootScope', 'Modal', '$state', '$localStorage', '$timeout', '$scope', '$stateParams', 'entity', 'Article', 'Chapter', 'KeyWords', 'ArticleCategory'];

    function ArticleDialogController(Subsection, Regulation, $rootScope, Modal, $state, $localStorage, $timeout, $scope, $stateParams, entity, Article, Chapter, KeyWords, ArticleCategory) {
        var vm = this;
        vm.isReady = false;
        vm.article = entity;

        vm.save = save;
        $rootScope.active = "regulation";
        vm.chapter = $localStorage.chapterSelected;

        vm.regulation = $localStorage.regulationSelected;
        if (vm.article.id !== null) {
            vm.button = "Editar";
            $rootScope.mainTitle = "Editar árticulo - " + vm.chapter.name + " - " + vm.regulation.name;

            loadAarticlesCategory()
        } else {
            vm.article.references = [];
            $rootScope.mainTitle = "Registrar árticulo - " + vm.chapter.name + " - " + vm.regulation.name;
            vm.button = "Registrar";
            vm.article.articleCategories = [];
            vm.article.keyWords = [];
            loadAarticlesCategory();
        }

        function loadAarticlesCategory() {

            ArticleCategory.query({}, function (categories) {
                vm.categories = [];
                vm.keyWords = [];
                angular.forEach(categories, function (item, key) {
                    vm.categories.push(item)
                });
                angular.forEach(vm.article.articleCategories, function (item, key) {
                    angular.forEach(vm.categories, function (category, key) {
                        if (item.id == category.id) {
                            var index = vm.categories.indexOf(category);
                            vm.categories.splice(index, 1);
                        }
                    });
                });
                getKeyWords();

            }, onSaveError);

        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });


        function getKeyWords() {
            KeyWords.query({}, function (keyWords) {

                angular.forEach(keyWords, function (item) {
                    vm.keyWords.push(item)
                });
                angular.forEach(vm.article.keyWords, function (item, key) {
                    angular.forEach(vm.keyWords, function (keyWord, key) {
                        if (item.id == keyWord.id) {
                            var index = vm.keyWords.indexOf(keyWord);
                            vm.keyWords.splice(index, 1);
                        }
                    });
                });

                loadAll();

            }, onSaveError);
        }

        function save() {
            Modal.showLoadingBar()
            vm.isSaving = true;
            vm.article.references = [];
            if(vm.articleSelectedToInsert!=undefined){
            vm.article.references.push(vm.articleSelectedToInsert);
            }
            if (vm.article.id !== null) {
                Article.update(vm.article, onSaveSuccess, onSaveError);
            } else {
                vm.article.chapterId = vm.chapter.id;
                vm.article.deleted = 0;

                Article.save(vm.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            $state.go('article');
            Modal.toast("Se ha gestionado el artículo correctamente.");
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.addToSelected = function (item, type) {
            var index;
            if (type === 1) {
                vm.article.articleCategories.push(item);
                index = vm.categories.indexOf(item);
                vm.categories.splice(index, 1);
            } else {
                vm.article.keyWords.push(item);
                index = vm.keyWords.indexOf(item);
                vm.keyWords.splice(index, 1);

            }

        };
        vm.deleteFromSelected = function (item, type) {
            var index;
            if (type === 1) {
                vm.categories.push(item);
                index = vm.article.articleCategories.indexOf(item);
                vm.article.articleCategories.splice(index, 1);

            } else {
                vm.keyWords.push(item);
                index = vm.article.keyWords.indexOf(item);
                vm.article.keyWords.splice(index, 1);

            }

        }


        function loadAll() {

            Regulation.query({}, onSuccess, onSaveError);

            function onSuccess(data, headers) {
                vm.regulations = data;
                vm.isReady = true;
                if (vm.article.id != null) {
                    if(vm.article.references.length>0){
                        angular.forEach(vm.article.references, function (reference, key) {
                            angular.forEach(vm.regulations, function (regulation, key) {
                                if(regulation.id==reference.regulationIdReference){
                                        Chapter.get({id: parseInt(reference.chapterId)}, function (chapter) {
                                            vm.chapterSelectedEdit = chapter;
                                            vm.selectRegulation(regulation);
                                            vm.referenceSelectedEdit = reference;
                                        });


                                }
                            });


                        });
                    }


                } else {
                    vm.selectRegulation(vm.regulations[0]);

                }

            }
        }


        vm.selectRegulation = function (regulation) {
            angular.forEach(vm.regulations, function (regulation, key) {
                regulation.selected = false;
            });
            vm.regulationSelected = regulation;
            regulation.selected = true;
            Chapter.query({
                regulationId: regulation.id
            }, function (data) {
                vm.regulationSelected.chapters = data;
                vm.isReady = true;
                if (vm.article.id != null) {
                    angular.forEach(vm.regulationSelected.chapters, function (chapter, key) {
                        if(chapter.id==vm.chapterSelectedEdit.id){
                            vm.selectChapters(chapter);
                        }
                    });

                }
            }, onSaveError);

        };

        vm.selectChapters = function (chapter) {
            angular.forEach(vm.regulationSelected.chapters, function (chapter, key) {
                chapter.selected = false;
                chapter.articles = [];
            });

            chapter.selected = true;


            console.log(chapter)
            Article.query({
                chapterId: chapter.id
            }, function (data) {
                angular.forEach(data, function (article, key) {
                    article.selected = false;
                    Subsection.query({
                        articleId: article.id
                    }, function (subsection) {
                        article.subsections = subsection;
                    }, onSaveError);
                });
                chapter.articles = data;
                angular.forEach(chapter.articles, function (article, key) {
                   if(article.id==vm.referenceSelectedEdit.id){
                       vm.selectArticles(chapter,article)
                   }
                });
            }, onSaveError);

        };
        vm.selectArticles = function (chapter,article) {
            angular.forEach(chapter.articles, function (article, key) {
                article.selected = false;
            });
            article.selected = !article.selected;
            vm.articleSelectedToInsert = article;
            // if (article.selected) {
            //     vm.articleSelectedToInsert = article
            // } else {
            //     var index = data.indexOf(article);
            //     vm.article.references.splice(index, 1);
            // }

        };
    }
})();
