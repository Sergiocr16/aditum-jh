(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationController', RegulationController);

    RegulationController.$inject = ['Principal','Modal','$rootScope', '$localStorage', 'Company', '$state', 'Regulation', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','globalCompany'];

    function RegulationController(Principal,Modal,$rootScope, $localStorage, Company, $state, Regulation, ParseLinks, AlertService, paginationConstants, pagingParams,globalCompany) {

        var vm = this;
        $rootScope.active = "regulation";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;

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
            Regulation.query({
                page: pagingParams.page - 1,
                size: 500,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;



                vm.page = pagingParams.page;
                vm.isReady = true;
                angular.forEach(data, function (regulation, key) {
                    if (regulation.companyId != null && vm.userType==1) {
                        Company.get({id: parseInt(regulation.companyId)}, function (company) {
                            regulation.company = company;
                        })
                    }else if(regulation.companyId != null && vm.userType==2){
                        if(regulation.companyId==globalCompany.getId()){
                            Company.get({id: parseInt(regulation.companyId)}, function (company) {
                                regulation.company = company;
                            })
                        }else{
                            var index = data.indexOf(regulation);
                            data.splice(index, 1);
                        }


                    }
                });
            vm.regulations = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.delete = function (regulation) {

            Modal.confirmDialog("¿Está seguro que desea eliminar: " + regulation.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar()
                    regulation.deleted = 1;
                    Regulation.update(regulation, onSaveSuccess, onSaveError);
                });


        };

        function onSaveSuccess() {
            Modal.hideLoadingBar();
            Modal.toast("Se ha eliminado el reglamento correctamente.");
            loadAll();
        }
        vm.watchChapters = function (regulation) {
            $localStorage.regulationSelected = regulation;
            $state.go('chapter')
        };

        vm.completeRegulation = function (regulation) {
            $localStorage.regulationSelected = regulation;

            $state.go('regulation-detail', {
                id: regulation.id
            })
        };


        function onSaveError(error) {
            AlertService.error(error.data.message);
        }

    }
})();
