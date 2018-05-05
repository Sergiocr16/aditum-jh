(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoController', BancoController);

    BancoController.$inject = ['$state', 'Banco', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function BancoController($state, Banco, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.accountsQuantity = 0;
        loadAll();

        function loadAll () {
            Banco.query({
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
                vm.bancos = data;
                vm.page = pagingParams.page;
                 setTimeout(function() {
                     $("#loadingIcon").fadeOut(300);
                 }, 400)
                 setTimeout(function() {
                     $("#tableData").fadeIn('slow');
                 },900 )
                angular.forEach(data,function(value,key){

//                   if(value.deleted==0){
                     vm.accountsQuantity = vm.accountsQuantity+1;
//                   }
                })

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
  vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
             var rgx = /(\d+)(\d{3})/;
             while (rgx.test(x1)) {
                     x1 = x1.replace(rgx, '$1' + ',' + '$2');
             }
             return x1 + x2;
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
