(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationSearchController', RegulationSearchController);

    RegulationSearchController.$inject = ['pagingParams','$scope','Article', 'Chapter', 'Principal', 'Modal', '$rootScope', '$localStorage', 'Company', '$state', 'Regulation', 'AlertService', 'globalCompany','Subsection'];

    function RegulationSearchController(pagingParams,$scope,Article, Chapter, Principal, Modal, $rootScope, $localStorage, Company, $state, Regulation, AlertService, globalCompany,Subsection) {

        var vm = this;
        $rootScope.active = "regulation-search";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.showReference=false;
        vm.waiting = false;
        vm.loadingReport = false;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        var completeRegulation;
        $rootScope.mainTitle = "Búsqueda reglamento";
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        Principal.identity().then(function (account) {
            vm.adminInfo = account;
            switch (account.authorities[0]) {
                case "ROLE_ADMIN":
                    vm.userType = 1;
                    vm.byCompany  = false;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    vm.byCompany  = true;
                    break;
                case "ROLE_USER":
                    vm.userType = 3;
                    vm.byCompany  = true;
                    break;
                case "ROLE_OWNER":
                    vm.userType = 3;
                    vm.byCompany  = true;
                    break;
                case "ROLE_JD":
                    vm.userType = 3;
                    vm.byCompany  = true;
                    break;
            }
            loadAll();

        });

        function loadAll() {
            if(!vm.byCompany){
                Regulation.query({
                    page: pagingParams.page - 1,
                    size: 500,
                    sort: sort()
                }, onSuccess, onError);
            }else{
                Regulation.queryByCompany({
                    page: pagingParams.page - 1,
                    size: 500,
                    sort: sort(),
                    companyId:globalCompany.getId()
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
                vm.selectRegulation(vm.regulations[0]);
                vm.isReady = true;
            }


        }

        vm.selectRegulation = function (regulation) {
            angular.forEach(vm.regulations, function (regulation, key) {
                regulation.selected = false;
            });
            vm.regulationSelected = regulation;
            completeRegulation = regulation;
            regulation.selected = true;
            Chapter.query({
                regulationId: regulation.id
            }, function (data) {
                regulation.chapters = data;
            }, onError);
        };

        vm.selectChapters = function (chapter) {
            chapter.selected = !chapter.selected;
            chapter.allArticles = false;
            if (chapter.selected) {
                Article.query({
                    chapterId: chapter.id
                }, function (data) {
                    angular.forEach(data, function (article, key) {
                        article.selected = false;
                        Subsection.query({
                            articleId: article.id
                        }, function (subsection) {
                            article.subsections = subsection;
                        }, onError);
                    });
                    chapter.articles = data;
                }, onError);
            } else {
                chapter.articles = [];
            }

        };
        vm.selectArticles = function (article) {
            article.selected = !article.selected;

        };

        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.consult = function (item,type) {
            vm.waiting = true;
            vm.justCategory = false;
            vm.justKeyWord = false;
            vm.searchCategoriesDTO = {};
            vm.searchCategoriesDTO.regulationDTO =  vm.regulationSelected;
            vm.searchCategoriesDTO.categories = [];
            vm.searchCategoriesDTO.keyWords = [];
            if(type==1){
                vm.categorySelect = item;
                vm.searchCategoriesDTO.categories.push(item.id);
                vm.justCategory = true;
            }else{
                vm.keyWordSelect = item;
                vm.searchCategoriesDTO.keyWords.push(item.id);
                vm.justKeyWord = true;
            }


            Regulation.searchInfoByCategoriesAndKeyWords(vm.searchCategoriesDTO, function (data) {
                vm.regulationSelected = data;
                angular.forEach(vm.regulationSelected.chapters, function (chapter, key) {
                 chapter.selected = true;
                    angular.forEach(chapter.articles, function (article, key) {
                        article.selected = true;
                    });
                });
                vm.waiting = false;

            }, onError);
        }

        vm.searchRegulation = function () {
            vm.isReady2 = true;
        };

        vm.selectAllArticles = function (chapter) {
            chapter.allArticles = !chapter.allArticles;
            if(chapter.allArticles){
                angular.forEach(chapter.articles, function (article, key) {
                    article.selected = true;
                });
            }else{
                angular.forEach(chapter.articles, function (article, key) {
                    article.selected = false;
                });

            }

        };

        vm.consultAll = function () {
            vm.waiting = true;
            vm.regulationSelected = completeRegulation;
            vm.waiting = false;
            vm.justKeyWord = false;
            vm.justCategory = false;
        };

        vm.consultReference = function (article,chapter,reference) {

            vm.regulationReference = {};
            vm.regulationReference.name = vm.regulationSelected.name;
            vm.regulationReference.chapter = chapter;
            vm.regulationReference.article = article;
            vm.regulationReference.reference = reference;

            vm.showReference=true;
        }
    }
})();
