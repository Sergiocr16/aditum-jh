(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationSearchByCategoriesController', RegulationSearchByCategoriesController);

    RegulationSearchByCategoriesController.$inject = ['ArticleCategory', 'Article', 'Chapter', 'Principal', 'Modal', '$rootScope', '$localStorage', 'Company', '$state', 'Regulation', 'AlertService', 'globalCompany', 'KeyWords'];

    function RegulationSearchByCategoriesController(ArticleCategory, Article, Chapter, Principal, Modal, $rootScope, $localStorage, Company, $state, Regulation, AlertService, globalCompany, KeyWords) {

        var vm = this;
        $rootScope.active = "regulation-search";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.loadingReport = false;
        vm.categoriesKeyWordsQueryDTO = {};
        $rootScope.mainTitle = "Búsqueda ADITUM rules";
        // Principal.identity().then(function (account) {
        //     vm.adminInfo = account;
        //     switch (account.authorities[0]) {
        //         case "ROLE_ADMIN":
        //             vm.userType = 1;
        //             break;
        //         case "ROLE_MANAGER":
        //             vm.userType = 2;
        //             break;
        //     }
        // });
        vm.userType = 1;
        vm.categoriesKeyWordsQueryDTO.categories = [];
        vm.categoriesKeyWordsQueryDTO.keyWords = [];
        vm.keyWordsSelected = [];
        vm.categoriesSelected = [];
        function onSaveError() {
            vm.isSaving = false;
        }


        loadAll();

        function loadAll() {
            Regulation.query({}, onSuccess, onError);

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
            if(vm.categoriesKeyWordsQueryDTO.regulationDTO!=null){
                Regulation.searchInfoByCategoriesAndKeyWords(vm.categoriesKeyWordsQueryDTO, function (data) {
                    vm.regulation = data;
                    vm.isReady2 = true;
                }, onSaveError);
            }else{
                Modal.toast("Debe seleccionar un reglamento.");
            }


        };

        vm.selectCategory = function (category) {
            var index = vm.categoriesKeyWordsQueryDTO.categories.indexOf(category.id);
            if(index>=0){
                vm.categoriesSelected.splice(category, 1);
                vm.categoriesKeyWordsQueryDTO.categories.splice(index, 1);
                category.selected = false;
            }else{
                vm.categoriesKeyWordsQueryDTO.categories.push(category.id);
                vm.categoriesSelected.push(category);
                category.selected = true;
            }

        }
        vm.selectKeyWords = function (keyWord) {
            var index = vm.categoriesKeyWordsQueryDTO.keyWords.indexOf(keyWord.id);
            if(index>=0){
                vm.keyWordsSelected.splice(keyWord, 1);
                vm.categoriesKeyWordsQueryDTO.keyWords.splice(keyWord, 1);
                keyWord.selected = false;
            }else{
                vm.keyWordsSelected.push(keyWord);
                vm.categoriesKeyWordsQueryDTO.keyWords.push(keyWord.id);
                keyWord.selected = true;
            }

        }
        vm.selectRegulation= function (regulation) {
            vm.categoriesKeyWordsQueryDTO.regulationDTO = regulation;
            angular.forEach(vm.regulations, function (regulation, key) {
                regulation.selected = false;
            });
            regulation.selected = true;
        }
    }
})();
