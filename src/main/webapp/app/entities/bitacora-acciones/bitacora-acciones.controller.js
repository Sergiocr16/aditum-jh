(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BitacoraAccionesController', BitacoraAccionesController);

    BitacoraAccionesController.$inject = ['Principal','globalCompany','$rootScope','Company','$localStorage','CommonMethods','$state', 'BitacoraAcciones', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function BitacoraAccionesController(Principal,globalCompany,$rootScope,Company,$localStorage,CommonMethods,$state, BitacoraAcciones, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "bitacoraAcciones";

            vm.hasContability = true;

        Principal.identity().then(function (account) {
            vm.adminInfo = account;
            switch (account.authorities[0]) {
                case "ROLE_ADMIN":
                    vm.userType = 1;
                    loadAllCompanies();

                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    vm.companyId = globalCompany.getId();
                    vm.loadAll();
                    break;
            }


        });
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 50000;
        vm.isReady = false;
        vm.type = 0;


        function loadAllCompanies () {
            Company.query({
                page: pagingParams.page - 1,
                size: 20
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.companies = data;
                vm.companySelected = vm.companies[0];
                vm.companyId = vm.companySelected.id;
                vm.loadAll();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.searchByType = function (type) {
            vm.type = type;
            vm.loadAll();
        };
        vm.changeCompany = function () {
            vm.companyId = vm.companySelected.id;


            vm.loadAll();
        };



        vm.loadAll = function() {
            vm.isReady = false;
            BitacoraAcciones.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                companyId: vm.companyId,
                type: vm.type,
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
                vm.isReady = true;
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.bitacoraAcciones = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }



        vm.detail = function (bitacoraAcciones) {
            if(bitacoraAcciones.type!==3){
                if(bitacoraAcciones.type===4){
                    $localStorage.budgetAction = 1;
                }
                var encryptedId = CommonMethods.encryptIdUrl(bitacoraAcciones.idReference)
                $state.go(bitacoraAcciones.urlState, {
                    id: encryptedId
                });

            }else{
                $state.go('administration-configuration-detail')
            }

        };

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
    }
})();
