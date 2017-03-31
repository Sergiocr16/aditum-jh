(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentController', ResidentController);

    ResidentController.$inject = ['DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope'];

    function ResidentController(DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope) {
         $rootScope.active = "residents";
        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.changesTitles = function() {
            if (enabledOptions) {
                vm.title = "Residentes habilitados";
                vm.buttonTitle = "Ver residentes deshabilitados";
                vm.actionButtonTitle = "Deshabilitar";
            } else {
                vm.title = "Residentes deshabilitados";
                vm.buttonTitle = "Ver residentes habilitados";
                vm.actionButtonTitle = "Habilitar";
            }
        }
        loadHouses();

        function loadHouses() {
            House.query({
                companyId: $rootScope.companyId
            }).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
                loadResidents();
            }

        }

        function loadResidents(option) {
            if (enabledOptions) {
                vm.changesTitles();
                Resident.residentsEnabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: $rootScope.companyId,
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Resident.residentsDisabled({
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

            function onSuccess(data, headers) {
                if (option !== 1) {
                    vm.queryCount = data.length;

                    vm.page = pagingParams.page;
                    vm.residents = formatResidents(data);
                      console.log(vm.residents)
                } else {
                    var residentsByHouse = [];
                    vm.residents = data;
                    for (var i = 0; i < vm.residents.length; i++) {
                        if (vm.house.id === vm.residents[i].houseId) {
                            residentsByHouse.push(vm.residents[i])
                        }
                    }
                    vm.residents = formatResidents(residentsByHouse);
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
            vm.findResidentsByHouse(vm.house);
        }
        vm.findResidentsByHouse = function(house) {
             $("#tableData").fadeOut(0);
             setTimeout(function() {
                 $("#loadingIcon").fadeIn(100);
             }, 200)
            vm.house = house;
            if (house == undefined) {
                loadResidents();
            } else {
                loadResidents(1);
            }
        }

        function formatResidents(residents) {
            var formattedResidents = [];
            for (var i = 0; i < residents.length; i++) {

                for (var e = 0; e < vm.houses.length; e++) {
                    if (residents[i].houseId == vm.houses[e].id) {
                        residents[i].house_id = vm.houses[e].housenumber;
                        residents[i].name = residents[i].name + " " + residents[i].lastname;
                    }
                }
            }

            return residents;
        }

        vm.deleteResident = function(id_resident, name, lastname) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar al residente " + name + "?",
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

                        Resident.delete({
                            id: id_resident
                        }, onSuccess);

                        function onSuccess(data, headers) {
                            toastr["success"]("Se ha eliminado el residente correctamente.");
                            loadResidents();
                        }
                    }
                }
            });


        };


        vm.disableEnabledResident = function(residentInfo) {

            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + residentInfo.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + residentInfo.name + "?";
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
                        Resident.get({id: residentInfo.id}).$promise.then(onSuccess);
                        function onSuccess (result) {
                            enabledDisabledResident(result);
                         }

                         function enabledDisabledResident(resident){
                             if (enabledOptions) {
                                 resident.enabled = 0;
                                 Resident.update(resident, onSuccess);

                                 function onSuccess(data, headers) {
                                     if (resident.isOwner == 1) {
                                         User.getUserById({
                                             id: residentInfo.userId
                                         }, onSuccess);

                                         function onSuccess(data, headers) {
                                             data.activated = 0;
                                             User.update(data, onSuccessUser);

                                             function onSuccessUser(data, headers) {
                                                 toastr["success"]("Se ha deshabilitado el residente correctamente.");
                                                 bootbox.hideAll();
                                                  loadResidents();
                                             }
                                         }
                                     } else {
                                         loadResidents();
                                         toastr["success"]("Se ha deshabilitado el residente correctamente.");
                                         bootbox.hideAll();
                                     }
                                 }

                             } else {
                                 resident.enabled = 1;
                                 Resident.update(resident, onSuccess);

                                 function onSuccess(data, headers) {
                                     if (resident.isOwner == 1) {
                                         User.getUserById({
                                             id: resident.userId
                                         }, onSuccess);

                                         function onSuccess(data, headers) {
                                             data.activated = 1;
                                             User.update(data, onSuccessUser);

                                             function onSuccessUser(data, headers) {
                                                 toastr["success"]("Se ha habilitado el residente correctamente.");
                                                 bootbox.hideAll();
                                                    loadResidents();
                                             }
                                         }
                                     } else {
                                         bootbox.hideAll();
                                         toastr["success"]("Se ha habilitado el residente correctamente.");
                                         loadResidents();
                                     }
                                 }
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
