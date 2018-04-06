(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ConfigureChargesController', ConfigureChargesController);

    ConfigureChargesController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge','CommonMethods'];

    function ConfigureChargesController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.radiostatus = true;
        vm.cuotaFija = true;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.verificando = false;
        moment.locale("es");
         vm.validate = function(house,s,t){
             var caracteres = ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","ñ","o","p","q","r","s","t","u","v","w","x","y","z",",",".","?","/","-","+","@","#","$","%","^","&","*","(",")","-","_","=","|"]
              var invalido = 0;
              angular.forEach(caracteres,function(val,index){
              if (s!=undefined){
               for(var i=0;i<s.length;i++){
               if(s.charAt(i).toUpperCase()==val.toUpperCase()){

               invalido++;
               }
               }
               }
              })
              if(invalido==0){
               if (t==1){
               house.validDue = true;
                house.dirtyDue = true;

               }else{
               house.validSquare=true;
                house.dirtySquare = true;
               }
              }else{
                if (t==1){
                  house.validDue = false;
                      house.dirtyDue = false;
                  }else{
                  house.validSquare=false ;

                                      house.dirtySquare = false;
                  }
              }
             }
        setTimeout(function() {
            loadAll();
        }, 1500)

       vm.saveHouse = function(house,t){
       if(house.validDue==true && house.validSquare == true){
       if(t==1){
        if(house.dirtyDue==true){
               House.update(house,function(result){
               toastr["success"]("Guardado.")
               house.dirtyDue = false;
               })
        }
       }else{
       if(house.dirtySquare==true){
                      House.update(house,function(result){
                      toastr["success"]("Guardado.")
                        house.dirtySquare = false;
                      })
               }
       }

       }else{
        toastr["error"]("Debe de ingresar solo números.")
       }
       }
        function loadAll() {
            House.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
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
                AdministrationConfiguration.get({
                    companyId: $rootScope.companyId
                }).$promise.then(function(result) {
                    vm.adminConfig = result;

                })
                vm.globalConceptNumber = 0;
                vm.globalConcept = [{
                    date: "",
                    concept: "",
                    id: vm.globalConceptNumber,
                    datePickerOpenStatus: false
                }];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    value.validDue = true;
                    value.validSquare = true;
                    value.dirtyDue = false;
                    value.dirtySquare = false;
                })
                vm.houses = data;

                vm.page = pagingParams.page;
                setTimeout(function() {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#tableData").fadeIn('slow');
                }, 700)
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

        function openCalendar(index) {
            vm.globalConcept[index].datePickerOpenStatus = true;
        }
    }
})();
