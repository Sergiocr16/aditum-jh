(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeByHouseController', VehiculeByHouseController);

    VehiculeByHouseController.$inject = ['$state','CommonMethods','$rootScope','Vehicule', 'House','ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','WSVehicle','companyUser','Modal'];

    function VehiculeByHouseController($state,CommonMethods,$rootScope,Vehicule, House, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,WSVehicle, companyUser,Modal) {
     $rootScope.active = "vehiculesHouses";
     var enabledOptions = true;
        var vm = this;
        vm.isReady = false;
        vm.isReady2 = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        $rootScope.mainTitle = "Vehículos de la filial";
        vm.editVehicle = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('vehiculeByHouse.edit', {
                id: encryptedId
            })
        }
        setTimeout(function(){
        House.get({ id: companyUser.houseId}).$promise.then(onSuccess);
          function onSuccess (house) {
              if (house.securityKey == null && house.emergencyKey == null) {
                  Modal.actionToast("Sus claves de seguridad aún no han sido definidas.","Establecer ahora",function(){
                      $state.go('keysConfiguration');
                  })
                   //
                   // bootbox.confirm({
                   //                         message: '<div class="gray-font font-15">Sus claves de seguridad aún no han sido definidas, recuerde que el tener las claves establecidas le provee mayor seguridad.</div>',
                   //
                   //     closeButton: false,
                   //
                   //     buttons: {
                   //         confirm: {
                   //             label: 'Establecer ahora',
                   //             className: 'btn-success'
                   //         },
                   //         cancel: {
                   //             label: 'Recordármelo luego',
                   //             className: 'btn-danger'
                   //         }
                   //     },
                   //     callback: function(result) {
                   //         if (result) {
                   //             $state.go('keysConguration');
                   //         }
                   //
                   //     }
                   // })
               }
            }
            },500)
         vm.changesTitles = function() {
            if (enabledOptions) {
                vm.buttonDisabledEnabledVehicules = "Vehículos deshabilitados";
                            vm.titleVehiculeIndex = "Mis vehículos ";
                            vm.titleDisabledButton = "Deshabilitar";
                            vm.iconDisabled = "fa fa-user-times";
                            vm.color = "red";
            } else {
              vm.buttonDisabledEnabledVehicules = "Vehículos habilitados";
                           vm.titleVehiculeIndex = "Mis vehículos (deshabilitados)";
                           vm.titleDisabledButton = "Habilitar";
                           vm.iconDisabled = "fa fa-undo";
                           vm.color = "green";
            }
          }


        loadVehicules()

        function loadVehicules() {
            if(enabledOptions){
              vm.changesTitles();
                Vehicule.findVehiculesEnabledByHouseId({
                    houseId: companyUser.houseId
               }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Vehicule.findVehiculesDisabledByHouseId({
                    houseId: companyUser.houseId
                }).$promise.then(onSuccess, onError);
            }

              function onSuccess(data) {

                  vm.vehicules = data;
                  vm.isReady = true;
                  vm.isReady2 = true;
               }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        vm.swithEnabledDisabledVehicules = function() {
            vm.isReady2 = false;
            enabledOptions = !enabledOptions;
            loadVehicules();
        }


           vm.disableEnabledVehicule = function(vehicule) {

                    var correctMessage;
                    if (enabledOptions) {
                        correctMessage = "¿Está seguro que desea deshabilitar al vehículo " + vehicule.licenseplate.toUpperCase() + "?";
                      } else {
                          correctMessage = "¿Está seguro que desea habilitar al vehículo " + vehicule.licenseplate.toUpperCase() + "?";
                    }

                   Modal.confirmDialog(correctMessage,"",function(){
                       Modal.showLoadingBar();

                       if (enabledOptions) {
                           vehicule.enabled = 0;
                           Vehicule.update(vehicule, onSuccess);
                           function onSuccess(data, headers) {
                               WSVehicle.sendActivity(data);
                               loadVehicules();
                               Modal.toast("Se ha deshabilitado el vehículo correctamente.");
                               Modal.hideLoadingBar();
                           }

                       } else {
                           vehicule.enabled = 1;
                           Vehicule.update(vehicule, onSuccess);
                           function onSuccess(data, headers) {
                               WSVehicle.sendActivity(data);
                               Modal.hideLoadingBar();
                               Modal.toast("Se ha habilitado el vehículo correctamente.");
                               loadVehicules();

                           }
                       }

                   });

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
