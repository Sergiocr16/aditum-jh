(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressController', EgressController);

    EgressController.$inject = ['$scope','$state', 'Egress', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonMethods','Proveedor','$rootScope'];

    function EgressController($scope,$state, Egress, ParseLinks, AlertService, paginationConstants, pagingParams,CommonMethods,Proveedor,$rootScope) {

        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
//        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.propertyName = 'id';
        vm.reverse = true;
         vm.consult = consult;
        vm.dates = {
            initial_time: undefined,
            final_time: undefined
        };

      vm.isDisableButton = function() {
           if (vm.dates.initial_time == undefined || vm.dates.final_time == undefined) return true;
           return false;
       }

        setTimeout(function(){loadProveedors();},500)
        function loadProveedors() {
            Proveedor.query({companyId: $rootScope.companyId}).$promise.then(onSuccessProveedores);
            function onSuccessProveedores(data, headers) {
                vm.proveedores = data;
                loadAll();
            }
        }
        function loadAll () {
            vm.title = 'Egresos';
            if(pagingParams.search == null){
            Egress.query({
               companyId: $rootScope.companyId,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
            }, onSuccess, onError);
           }else{
           var dates = pagingParams.search.split(" ");
             vm.dates = {
                   initial_time: moment(dates[0], 'DD/MM/YYYY').toDate(),
                   final_time: moment(dates[1], 'DD/MM/YYYY').toDate(),
               };
           vm.consult();
           }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.egresses = data;
                vm.page = pagingParams.page;
                formatEgresos(vm.egresses);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        vm.sortBy = function(propertyName) {
            vm.reverse = (vm.propertyName === propertyName) ? !vm.reverse : false;
            vm.propertyName = propertyName;
          };

        function formatEgresos(egresses){
         angular.forEach(egresses,function(value,key){

             if(value.paymentDate == null || value.paymentDate == 'undefined' ){
                 value.paymentDate = "No pagado";
             }
             if(value.folio == null || value.folio == 'undefined' ){
              value.folio = 'Sin Registrar'
             }
             if(value.reference == null || value.reference == 'undefined' ){
               value.reference = 'Sin Registrar'
              }
             angular.forEach(vm.proveedores,function(proveedor,key){


                 if(proveedor.id == value.proveedor){

                    value.proveedor = proveedor.empresa
                 }

             })
         })
                 setTimeout(function() {
                             $("#loadingIcon").fadeOut(300);
                        }, 400)
                         setTimeout(function() {
                             $("#tableData").fadeIn('slow');
                         },900 )
        }



       vm.clickPopover = function(id) {

            $('[data-toggle="'+id+'"]').popover({
            placement : 'bottom',
            html : true,
            animation: true,
            trigger: "focus",
            content : '<div><a href="#"></a><div ><h4 >Reportar el pago de este egreso</h4> <h1 class="text-center"><button type=button" class="btn btn-primary" onclick="formats()" >Reportar pago</button></h1></div>'
            });
            $(document).on("click", ".popoversd" , function(){

            })

        };

        vm.detailEgress = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('egress-detail', {
                id: encryptedId
            })
        }

   vm.editEgress = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('egress.edit', {
                id: encryptedId
            })
        }

       vm.updatePicker = function() {
            vm.picker1 = {
                datepickerOptions: {
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.dates.initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }
        vm.updatePicker();
        function consult () {
                $("#tableData").fadeOut(0);
                setTimeout(function() {
                    $("#loadingIcon").fadeIn(100);
                }, 200)
                Egress.findBetweenDatesByCompany({
                    initial_time: moment(vm.dates.initial_time).format(),
                     final_time: moment(vm.dates.final_time).format(),
                     companyId: $rootScope.companyId,
                     page: pagingParams.page - 1,
                     size: vm.itemsPerPage,
                },onSuccess,onError);

                function onSuccess(data, headers) {
                    moment.locale('es');
                    vm.egresses = data;
                    vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;
                    vm.page = pagingParams.page;
                    vm.title = 'Egresos entre:';
                    vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " +moment(vm.dates.final_time).format("LL");
                    vm.isConsulting = true;
                    formatEgresos(vm.egresses);
                }
                function onError(error) {
                    AlertService.error(error.data.message);
                }
            }
        vm.payEgress = function(id){
              var encryptedId = CommonMethods.encryptIdUrl(id)
                $state.go('egress.edit', {
                    id: encryptedId
                })
        }
        vm.stopConsulting = function() {
              $("#tableData").fadeOut(0);
            setTimeout(function() {
                $("#loadingIcon").fadeIn(100);
            }, 200)
             vm.dates = {
                        initial_time: undefined,
                        final_time: undefined
                    };
            pagingParams.page = 1;
            pagingParams.search = null;
            vm.isConsulting = false;
            loadAll();
            vm.titleConsult = "";
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
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
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.isConsulting==true?moment(vm.dates.initial_time).format('l') +" "+moment(vm.dates.final_time).format('l'):null,
            });
        }
      vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
