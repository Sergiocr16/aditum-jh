(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsController', CommonAreaReservationsController);

    CommonAreaReservationsController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea'];

    function CommonAreaReservationsController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadCommonAreas();
        function loadCommonAreas(){
            CommonArea.query({},onSuccessCommonAreas, onError);

        }
        function onSuccessCommonAreas(data, headers) {
            vm.commonAreas = data;
            if(data.length>0){
                loadAll();
            }
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        function loadAll () {
            CommonAreaReservations.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
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
                vm.commonAreaReservations = data;
                vm.page = pagingParams.page;
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
    }
})();
