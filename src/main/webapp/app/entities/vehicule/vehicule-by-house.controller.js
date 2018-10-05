(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeByHouseController', VehiculeByHouseController);

    VehiculeByHouseController.$inject = ['$state','CommonMethods','$rootScope','Vehicule', 'House','ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','WSVehicle','companyUser'];

    function VehiculeByHouseController($state,CommonMethods,$rootScope,Vehicule, House, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,WSVehicle, companyUser) {
     $rootScope.active = "vehiculesHouses";
     var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;

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
                   bootbox.confirm({
                                           message: '<div class="gray-font font-15">Sus claves de seguridad aún no han sido definidas, recuerde que el tener las claves establecidas le provee mayor seguridad.</div>',

                       closeButton: false,

                       buttons: {
                           confirm: {
                               label: 'Establecer ahora',
                               className: 'btn-success'
                           },
                           cancel: {
                               label: 'Recordármelo luego',
                               className: 'btn-danger'
                           }
                       },
                       callback: function(result) {
                           if (result) {
                               $state.go('keysConguration');
                           }

                       }
                   })
               }
            }
            },500)
         vm.changesTitles = function() {
            if (enabledOptions) {
                vm.buttonDisabledEnabledVehicules = "Vehículos deshabilitados";
                            vm.titleVehiculeIndex = "Mis vehículos ";
                            vm.titleDisabledButton = "Deshabilitar vehículo";
                            vm.iconDisabled = "fa fa-user-times";
                            vm.color = "red";
            } else {
              vm.buttonDisabledEnabledVehicules = "Vehículos habilitados";
                           vm.titleVehiculeIndex = "Mis vehículos (deshabilitados)";
                           vm.titleDisabledButton = "Habilitar vehículo";
                           vm.iconDisabled = "fa fa-undo";
                           vm.color = "green";
            }
          }
        setTimeout(function(){ loadVehicules();},500)



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
               setTimeout(function() {
                         $("#loadingIcon").fadeOut(300);
                         $("#loadingIcon").fadeOut(300);
               }, 400)
                setTimeout(function() {
                    $("#vehicules_container").fadeIn('slow');
                },900 )
               }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        vm.swithEnabledDisabledVehicules = function() {
             $("#vehicules_container").fadeOut(0);
                      setTimeout(function() {
                          $("#loadingIcon").fadeIn(100);
                      }, 200)
            enabledOptions = !enabledOptions;
            loadVehicules();
        }

         vm.deleteVehicule = function(id_vehicule, license_plate) {
             bootbox.confirm({
                       message: "¿Está seguro que desea eliminar al vehículo " + license_plate + "?",
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
                        Vehicule.delete({
                            id: id_vehicule
                        }, onSuccess);

                        function onSuccess(data, headers) {
                        WSVehicle.deleteEntity({id:data.id,type:'vehicle'});
                            toastr["success"]("Se ha eliminado el vehículo correctamente.");
                            loadVehicules();
                        }
                    }
                }
            });


        };

           vm.disableEnabledVehicule = function(vehicule) {

                    var correctMessage;
                    if (enabledOptions) {
                        correctMessage = "¿Está seguro que desea deshabilitar al vehículo " + vehicule.licenseplate.toUpperCase() + "?";
                      } else {
                          correctMessage = "¿Está seguro que desea habilitar al vehículo " + vehicule.licenseplate.toUpperCase() + "?";
                    }
                    bootbox.confirm({

                        message: correctMessage,

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
                                if (enabledOptions) {
                                    vehicule.enabled = 0;
                                    Vehicule.update(vehicule, onSuccess);
                                    function onSuccess(data, headers) {
                                    WSVehicle.sendActivity(data);
                                            loadVehicules();
                                            toastr["success"]("Se ha deshabilitado el vehículo correctamente.");
                                            bootbox.hideAll();
                                    }

                                } else {
                                    vehicule.enabled = 1;
                                    Vehicule.update(vehicule, onSuccess);
                                    function onSuccess(data, headers) {
                                     WSVehicle.sendActivity(data);
                                            bootbox.hideAll();
                                            toastr["success"]("Se ha habilitado el vehículo correctamente.");
                                            loadVehicules();

                                    }
                                }

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
