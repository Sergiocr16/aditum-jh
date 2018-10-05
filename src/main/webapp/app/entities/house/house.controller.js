(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseController', HouseController);

    HouseController.$inject = ['$state','House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','$rootScope','CommonMethods'];

    function HouseController($state,House, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,$rootScope,CommonMethods ) {
    $rootScope.active = "houses";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.estado = "";
        vm.ocultarACondos = true;
        setTimeout(function(){loadAll();
        // if($rootScope.companyId>2){
        // vm.ocultarACondos = true;
        // }
        },1500)

        vm.editHouse = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
                    $state.go('house.edit', {
                        id: encryptedId
                    })
        }
        function loadAll () {
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
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                  angular.forEach(data,function(value,key){
                  value.housenumber = parseInt(value.housenumber);
                      if(value.housenumber==9999){
                      value.housenumber="Oficina"
                      }
                  })
                vm.houses = data;
                vm.page = pagingParams.page;
                  setTimeout(function() {
                            $("#loadingIcon").fadeOut(300);
                  }, 400)
                   setTimeout(function() {
                       $("#tableData").fadeIn('slow');
                   },700 )
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



       vm.showKeys = function(house_number, securityKey, emergencyKey) {
            if (securityKey == null || emergencyKey == null || securityKey == "" || emergencyKey == "" ) {
                toastr["error"]("Esta casa aún no tiene claves de seguridad asignadas.");
            } else {
                bootbox.dialog({
                    message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div> <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de seguridad: <span class="font-20 bold" id="security_key">1134314</span></h1></div> <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de emergencia: <span class="font-20 bold" id="emergency_key">1134314</span></h1></div>',
                    closeButton: false,
                    buttons: {
                        confirm: {
                            label: 'Ocultar',
                            className: 'btn-success'
                        }
                    },
                })
                document.getElementById("key_id_house").innerHTML = "" + house_number;
                document.getElementById("security_key").innerHTML = "" + securityKey;
                document.getElementById("emergency_key").innerHTML = "" + emergencyKey;
            }
        }
       vm.showLoginCode = function(house_number, codeStatus, loginCode) {
             var estado = "";
            if (loginCode == null) {
                toastr["error"]("Esta casa aún no tiene un código de iniciación asignado.");
            } else {

                if(codeStatus==false || codeStatus ==0){estado = 'No activada'} else { estado = "Activada"};
                bootbox.dialog({
                    message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div> <div class="text-center gray-font font-15"> <h1 class="font-20">Código de iniciación: <span class="font-15 bold" id="login_code">1134314</span></h1></div> <div class="text-center gray-font font-15"> <h1 class="font-20">Estado: <span class="font-15 bold" id="code_status">1134314</span></h1></div>',
                    closeButton: false,
                    buttons: {
                        confirm: {
                            label: 'Ocultar',
                            className: 'btn-success'
                        }
                    },
                })

                document.getElementById("key_id_house").innerHTML = "" + house_number;
                document.getElementById("login_code").innerHTML = "" + loginCode;
                document.getElementById("code_status").innerHTML = "" + estado;
            }
        }

    }
})();
