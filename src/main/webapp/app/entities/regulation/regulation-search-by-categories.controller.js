(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationSearchByCategoriesController', RegulationSearchByCategoriesController);

    RegulationSearchByCategoriesController.$inject = ['pagingParams', '$scope', 'ArticleCategory', 'Article', 'Chapter', 'Principal', 'Modal', '$rootScope', '$localStorage', 'Company', '$state', 'Regulation', 'AlertService', 'globalCompany', 'KeyWords'];

    function RegulationSearchByCategoriesController(pagingParams, $scope, ArticleCategory, Article, Chapter, Principal, Modal, $rootScope, $localStorage, Company, $state, Regulation, AlertService, globalCompany, KeyWords) {
        var vm = this;
        $rootScope.active = "regulation-search";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.loadingReport = false;
        vm.waiting = false;
        vm.showReference = false;
        vm.noJustOne = true;
        vm.isReady3 = false;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.categoriesKeyWordsQueryDTO = {};
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $rootScope.mainTitle = "Búsqueda reglamento";
        Principal.identity().then(function (account) {
            vm.adminInfo = account;
            switch (account.authorities[0]) {
                case "ROLE_ADMIN":
                    vm.userType = 1;
                    vm.byCompany = false;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    vm.byCompany = true;
                    break;
                case "ROLE_USER":
                    vm.userType = 3;
                    vm.byCompany = true;
                    break;
                case "ROLE_OWNER":
                    vm.userType = 3;
                    vm.byCompany = true;
                    break;
                case "ROLE_JD":
                    vm.userType = 3;
                    vm.byCompany = true;
                    break;
            }
            loadAll();
        });
        vm.userType = 1;
        vm.categoriesKeyWordsQueryDTO.categories = [];
        vm.categoriesKeyWordsQueryDTO.keyWords = [];
        vm.keyWordsSelected = [];
        vm.categoriesSelected = [];

        function onSaveError() {
            vm.isSaving = false;
        }


        function loadAll() {

            if (!vm.byCompany) {
                Regulation.query({
                    page: pagingParams.page - 1,
                    size: 500,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Regulation.queryByCompany({
                    page: pagingParams.page - 1,
                    size: 500,
                    sort: sort(),
                    companyId: globalCompany.getId()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                angular.forEach(data, function (regulation, key) {
                    if (regulation.companyId != null && vm.userType == 1) {
                        Company.get({id: parseInt(regulation.companyId)}, function (company) {
                            regulation.company = company;
                        })
                    } else if (regulation.companyId != null && vm.userType == 2) {
                        if (regulation.companyId == globalCompany.getId()) {
                            Company.get({id: parseInt(regulation.companyId)}, function (company) {
                                regulation.company = company;
                            })
                        } else {
                            var index = data.indexOf(regulation);
                            data.splice(index, 1);
                        }


                    }
                });
                vm.regulations = data;
                getCategories();
                vm.isReady = true;
            }


        }

        function getCategories() {

            ArticleCategory.query({}, function (categories) {
                vm.categories = [];
                vm.keyWords = [];
                angular.forEach(categories, function (item, key) {
                    item.selected = false;
                    vm.categories.push(item)
                });

                getKeyWords();

            }, onSaveError);
        }


        function getKeyWords() {
            KeyWords.query({}, function (keyWords) {

                angular.forEach(keyWords, function (item) {
                    vm.keyWords.push(item)
                });

                vm.isReady = true;

            }, onSaveError);
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.searchRegulation = function () {
            vm.noJustOne = true;
            if (vm.categoriesKeyWordsQueryDTO.regulationDTO != null) {
                vm.waiting = true;
                Regulation.searchInfoByCategoriesAndKeyWords(vm.categoriesKeyWordsQueryDTO, function (data) {
                    vm.regulation = data;
                    vm.isReady2 = true;
                    vm.waiting = false;
                }, onSaveError);
            } else {
                Modal.toast("Debe seleccionar un reglamento.");
            }


        };

        vm.selectCategory = function (category) {
            var index = vm.categoriesKeyWordsQueryDTO.categories.indexOf(category.id);
            if (index >= 0) {
                vm.categoriesSelected.splice(category, 1);
                vm.categoriesKeyWordsQueryDTO.categories.splice(index, 1);
                category.selected = false;
            } else {
                vm.categoriesKeyWordsQueryDTO.categories.push(category.id);
                vm.categoriesSelected.push(category);
                category.selected = true;
            }

        }
        vm.selectKeyWords = function (keyWord) {
            var index = vm.categoriesKeyWordsQueryDTO.keyWords.indexOf(keyWord.id);
            if (index >= 0) {
                vm.keyWordsSelected.splice(keyWord, 1);
                vm.categoriesKeyWordsQueryDTO.keyWords.splice(keyWord, 1);
                keyWord.selected = false;
            } else {
                vm.keyWordsSelected.push(keyWord);
                vm.categoriesKeyWordsQueryDTO.keyWords.push(keyWord.id);
                keyWord.selected = true;
            }

        }
        vm.selectRegulation = function (regulation) {
            vm.categoriesKeyWordsQueryDTO.regulationDTO = regulation;
            angular.forEach(vm.regulations, function (regulation, key) {
                regulation.selected = false;
            });
            regulation.selected = true;
        };
        vm.consult = function (item, type) {
            vm.waiting = true;
            vm.justCategory = false;
            vm.justKeyWord = false;
            vm.searchCategoriesDTO = {};
            vm.searchCategoriesDTO.regulationDTO = vm.categoriesKeyWordsQueryDTO.regulationDTO;
            vm.searchCategoriesDTO.categories = [];
            vm.searchCategoriesDTO.keyWords = [];
            if (type == 1) {
                vm.categorySelect = item;
                vm.searchCategoriesDTO.categories.push(item.id);
                vm.justCategory = true;
            } else {
                vm.keyWordSelect = item;
                vm.searchCategoriesDTO.keyWords.push(item.id);
                vm.justKeyWord = true;
            }


            Regulation.searchInfoByCategoriesAndKeyWords(vm.searchCategoriesDTO, function (data) {
                vm.regulation = data;
                vm.waiting = false;
                vm.noJustOne = false;

            }, onSaveError);
        }

        vm.consultReference = function (article, chapter, reference) {
            vm.waiting = true;
            vm.regulationReference = {};
            vm.regulationReference.name = vm.regulation.name;
            vm.regulationReference.chapter = chapter;
            vm.regulationReference.article = article;
            vm.regulationReference.reference = reference;
            vm.waiting = false;
            vm.showReference = true;
        }


    }
})();
