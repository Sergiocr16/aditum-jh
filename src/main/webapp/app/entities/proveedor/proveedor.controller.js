(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProveedorController', ProveedorController);

    ProveedorController.$inject = ['$state', 'Proveedor', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$rootScope'];

    function ProveedorController($state, Proveedor, ParseLinks, AlertService, paginationConstants, pagingParams,$rootScope) {

        var vm = this;
      $rootScope.active = "proovedores";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.proveedorsQuantity = 0;
        setTimeout(function() {
                       loadAll();
         },900 )


        function loadAll () {
        vm.proveedorsQuantity = 0;
            Proveedor.query({
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
                setTimeout(function() {
                     $("#loadingIcon").fadeOut(300);
                 }, 400)
                 setTimeout(function() {
                     $("#tableData").fadeIn('slow');
                 },900 )

                 angular.forEach(data,function(value,key){
                     if(value.email==null || value.email==""){
                        value.email = 'No registrado'
                     }
                      if(value.comentarios==null || value.comentarios==""){
                         value.comentarios = 'No hay'
                      }
                      if(value.deleted==0){
                         vm.proveedorsQuantity = vm.proveedorsQuantity+1;
                      }
                 })
                console.log(vm.proveedorsQuantity)
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.proveedors = data;
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

      vm.confirmDeleteProveedor = function(proveedor) {
             bootbox.confirm({
                       message: "¿Está seguro que desea eliminar este proveedor " + "?",
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
                    vm.deleteProveedor(proveedor)
                    }
                }
            });

        };

        vm.deleteProveedor = function(proveedor){
             proveedor.deleted = 1;
             Proveedor.update(proveedor, onSuccessDeleted, onError);

        }
        function onSuccessDeleted() {
              loadAll()
              toastr["success"]("Se eliminó el proveedor correctamente");
        }
          function onError(error) {
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
