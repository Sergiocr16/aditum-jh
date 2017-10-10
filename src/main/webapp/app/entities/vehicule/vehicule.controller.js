(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeController', VehiculeController);

    VehiculeController.$inject = ['$state','CommonMethods','$rootScope','Vehicule', 'House','ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','WSVehicle','WSDeleteEntity'];

    function VehiculeController($state,CommonMethods,$rootScope,Vehicule, House, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,WSVehicle,WSDeleteEntity) {
         $rootScope.active = "vehicules";
     var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.editVehicle = function(id){
        var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('vehicule.edit', {
                id: encryptedId
            })

        }
         vm.changesTitles = function() {
            if (enabledOptions) {
                vm.title = "Vehículos habilitados";
                vm.buttonTitle = "Ver vehículos deshabilitados";
                vm.actionButtonTitle = "Deshabilitar";
            } else {
                vm.title = "Vehículos deshabilitados";
                vm.buttonTitle = "Ver vehículos habilitados";
                vm.actionButtonTitle = "Habilitar";
            }
          }
        setTimeout(function(){
          loadHouses();
        },500)

        function loadHouses() {
            House.query({companyId: $rootScope.companyId}).$promise.then(onSuccessHouses);
            function onSuccessHouses(data, headers) {
                vm.houses = data;
                loadVehicules();
            }

        }

        function loadVehicules(option) {
            if(enabledOptions){
              vm.changesTitles();
                Vehicule.vehiculesEnabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: $rootScope.companyId,
               }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Vehicule.vehiculesDisabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: $rootScope.companyId,
                }).$promise.then(onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
              function onSuccess(data) {
                   if (option !== 1) {
                       vm.queryCount = data.length;
                       vm.page = pagingParams.page;
                       vm.vehicules = formatVehicules(data);
                   } else {
                       var vehiculesByHouse = [];
                       vm.vehicules = data;
                       for (var i = 0; i < vm.vehicules.length; i++) {
                           if (vm.house.id === vm.vehicules[i].houseId) {
                               vehiculesByHouse.push(vm.vehicules[i])
                           }
                       }
                       vm.vehicules = formatVehicules(vehiculesByHouse);
                   }
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

        vm.switchEnabledDisabledResidents = function() {
            enabledOptions = !enabledOptions;
            vm.findVehiculesByHouse(vm.house);
        }


        vm.findVehiculesByHouse = function(house) {
             $("#tableData").fadeOut(0);
             setTimeout(function() {
                 $("#loadingIcon").fadeIn(100);
             }, 200)
            vm.house = house;

            if (house == undefined) {
                loadVehicules();
            } else {
                loadVehicules(1);
            }
        }
        function formatVehicules(vehicules) {
            var formattedVehicules = [];
            for (var i = 0; i < vehicules.length; i++) {

                for (var e = 0; e < vm.houses.length; e++) {
                    if (vehicules[i].houseId == vm.houses[e].id) {
                        vehicules[i].house_id = vm.houses[e].housenumber;
                    }
                }
            }

            return vehicules;
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
                        }, onSuccessDelete);
                    }
                }
            });

             function onSuccessDelete(data, headers) {
                 toastr["success"]("Se ha eliminado el vehículo correctamente.");
                 loadVehicules();
                 WSDeleteEntity.sendActivity({type:'vehicle',id:id_vehicule})
             }

        };

           vm.disableEnabledVehicule = function(vehicule) {

                    var correctMessage;
                    if (enabledOptions) {
                        correctMessage = "¿Está seguro que desea deshabilitar al vehículo " + vehicule.licenseplate + "?";
                      } else {
                          correctMessage = "¿Está seguro que desea habilitar al vehículo " + vehicule.licenseplate + "?";
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
                                    Vehicule.update(vehicule, onSuccessDisable);


                                } else {
                                    vehicule.enabled = 1;
                                    Vehicule.update(vehicule, onSuccessEnable);

                                }

                            }
                        }
                    });
               function onSuccessEnable(data, headers) {
                   WSVehicle.sendActivity(data);
                   bootbox.hideAll();
                   toastr["success"]("Se ha habilitado el vehículo correctamente.");
                   loadVehicules();

               }

               function onSuccessDisable(data, headers) {
                   WSVehicle.sendActivity(data);
                   loadVehicules();
                   toastr["success"]("Se ha deshabilitado el vehículo correctamente.");
                   bootbox.hideAll();
               }
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
