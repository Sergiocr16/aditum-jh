(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BitacoraAccionesController', BitacoraAccionesController);

    BitacoraAccionesController.$inject = ['$rootScope','Company','$localStorage','CommonMethods','$state', 'BitacoraAcciones', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function BitacoraAccionesController($rootScope,Company,$localStorage,CommonMethods,$state, BitacoraAcciones, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        $rootScope.active = "bitacoraAcciones";

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 50000;
        vm.isReady = false;
        vm.type = 0;


        loadAllCompanies();

        function loadAllCompanies () {
            Company.query({
                page: pagingParams.page - 1,
                size: 20
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.companies = data;
                vm.companySelected = vm.companies[0];
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
        vm.loadAll = function() {
            vm.isReady = false;
            BitacoraAcciones.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                companyId: vm.companySelected.id,
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
                console.log(data)
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
