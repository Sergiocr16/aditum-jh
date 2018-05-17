(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoController', BancoController);

    BancoController.$inject = ['CommonMethods','$state', 'Banco', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$rootScope'];

    function BancoController(CommonMethods,$state, Banco, ParseLinks, AlertService, paginationConstants, pagingParams,$rootScope) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.accountsQuantity = 0;

        setTimeout(function(){ loadAll();},900)
        function loadAll () {
            Banco.query({
                companyId: $rootScope.companyId,
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
          vm.sortBy = function(propertyName) {
            vm.reverse = (vm.propertyName === propertyName) ? !vm.reverse : false;
            vm.propertyName = propertyName;
          };
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
     vm.deleteBanco = function(banco) {

            bootbox.confirm({

                message: "¿Está seguro que desea eliminar la cuenta " + banco.beneficiario + "?",

                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                          CommonMethods.waitingMessage();
                          banco.deleted = 0;
                          Banco.update(banco, onDeleteSuccess, onSaveError);

                    }
                }
            });
        };

       function onDeleteSuccess (result) {
            bootbox.hideAll()
            loadAll();
             toastr["success"]("Se eliminó la cuenta correctamente");
            $state.go('banco-configuration');
            vm.isSaving = false;
        }
         function onSaveError(error) {
              bootbox.hideAll()
           toastr["error"]("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
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
