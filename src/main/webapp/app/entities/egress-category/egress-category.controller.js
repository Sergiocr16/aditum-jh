(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressCategoryController', EgressCategoryController);

    EgressCategoryController.$inject = ['EgressCategory','$rootScope'];

    function EgressCategoryController(EgressCategory,$rootScope) {

        var vm = this;
            vm.groups = [{id:1,name:'Gastos fijos'},{id:2,name:'Gastos variables'},{id:3,name:'Otros gastos'}]
            vm.egressCategories = [];

            vm.validate = function(egressCategory,type){
                if(type==1){
                     if(egressCategory.group==""|| egressCategory.group==null || egressCategory.group==undefined){

                         egressCategory.groupIsEmpty=true;

                    }else{
                         egressCategory.groupIsEmpty=false;
                    }
                }else{
                    if(egressCategory.category==""|| egressCategory.category==null || egressCategory.category==undefined){

                         egressCategory.categoryIsEmpty=true;

                    }else{
                         egressCategory.categoryIsEmpty=false;
                    }

                }


            }

           vm.addEgressCategory = function(){
               var egressCategory = {id:null,group:null,category:null,companyId:$rootScope.companyId}
               vm.egressCategories.push(egressCategory);
           }
           vm.saveCategory = function(egressCategory,type){

               if(egressCategory.groupIsEmpty==false && egressCategory.categoryIsEmpty == false){

                   if(egressCategory.id!==null){
                       if(egressCategory.categoryIsEmpty==false){
                              EgressCategory.update(egressCategory,function(result){
                              toastr["success"]("Guardado.")
                              })
                       }

                   }else{
                     EgressCategory.save(egressCategory,function(result){
                         toastr["success"]("Guardado.")
                     })

                   }
               }else{
                toastr["error"]("No puede dejar el campo vacio")
               }

           }
              vm.confirmDeleteCategory = function(index,egressCategory) {
                     bootbox.confirm({
                               message: "¿Está seguro que desea eliminar esta categoría de gastos " + "?",
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
                               toastr["success"]("Eliminado.")
                            vm.deleteCategory(index,egressCategory)
                            }
                        }
                    });

                };

                vm.deleteCategory = function(index,egressCategory){
                     if(egressCategory.id!==null){
                       EgressCategory.delete({id: egressCategory.id},
                         function () {
                                 loadAll();
                         });
                     }else{
                     loadAll();
                     }
                }

            setTimeout(function(){loadAll()},1000)
            function loadAll() {

                EgressCategory.query({companyId: $rootScope.companyId}).$promise.then(onSuccessEgressCategories);
                function onSuccessEgressCategories(data, headers) {
                   vm.searchQuery = null;
                      angular.forEach(data, function(value, key) {
                           value.groupIsEmpty = false
                           value.categoryIsEmpty = false;
                       })
                     vm.egressCategories = data;
                   setTimeout(function() {
                       $("#loadingIcon").fadeOut(300);
                   }, 400)
                   setTimeout(function() {
                       $("#tableData").fadeIn('slow');
                   }, 700)
                }

            }
    }
})();
