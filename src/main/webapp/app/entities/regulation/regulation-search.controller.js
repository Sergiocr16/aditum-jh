(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationSearchController', RegulationSearchController);

    RegulationSearchController.$inject = ['Article', 'Chapter', 'Principal', 'Modal', '$rootScope', '$localStorage', 'Company', '$state', 'Regulation', 'AlertService', 'globalCompany','Subsection'];

    function RegulationSearchController(Article, Chapter, Principal, Modal, $rootScope, $localStorage, Company, $state, Regulation, AlertService, globalCompany,Subsection) {

        var vm = this;
        $rootScope.active = "regulation-search";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.loadingReport = false;
        $rootScope.mainTitle = "Búsqueda ADITUM rules";
        Principal.identity().then(function (account) {
            vm.adminInfo = account;
            switch (account.authorities[0]) {
                case "ROLE_ADMIN":
                    vm.userType = 1;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    break;
            }
        });
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
                vm.selectRegulation(vm.regulations[0]);
                vm.isReady = true;
            }


        }

        vm.selectRegulation = function (regulation) {
            console.log(regulation)
            angular.forEach(vm.regulations, function (regulation, key) {
                regulation.selected = false;
            });
            vm.regulationSelected = regulation;
            regulation.selected = true;
            Chapter.query({
                regulationId: regulation.id
            }, function (data) {
                regulation.chapters = data;
            }, onError);
        };

        vm.selectChapters = function (chapter) {
            chapter.selected = !chapter.selected;

            if (chapter.selected) {
                Article.query({
                    chapterId: chapter.id
                }, function (data) {
                    angular.forEach(data, function (article, key) {
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

        vm.searchRegulation = function () {
            vm.isReady2 = true;
        };
    }
})();
