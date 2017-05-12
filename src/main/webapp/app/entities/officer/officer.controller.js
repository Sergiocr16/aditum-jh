(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerController', OfficerController);

    OfficerController.$inject = ['$state','CommonMethods','DataUtils', 'Officer', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','$rootScope'];

    function OfficerController($state,CommonMethods,DataUtils, Officer, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,$rootScope) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        loadAll();

        vm.editOfficer = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('officer.edit', {
                id: encryptedId
          })
        }

        function loadAll () {
            Officer.query({
               companyId: $rootScope.companyId
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
               vm.officers = data;
                $("#loadingIcon").fadeOut(0);
                setTimeout(function() {
                    $("#tableData").fadeIn(300);
                }, 200)
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;

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
