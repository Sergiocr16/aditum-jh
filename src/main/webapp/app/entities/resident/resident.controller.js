(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentController', ResidentController);

    ResidentController.$inject = ['DataUtils', 'Resident', 'User','CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal'];

    function ResidentController(DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal) {
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
            House.query({}, onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
                loadResidentsEnabled();
            }
        }

        function loadResidentsEnabled() {

            vm.changesTitles();
            Resident.query({
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
                vm.residents = formatResidents(data);
                vm.page = pagingParams.page;
                $("#loadingIcon").fadeOut(0);
                setTimeout(function() {
                    $("#tableData").fadeIn(300);
                }, 200)
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.findResidentsByHouse = function(house) {

            var residentsByHouse = [];
            if (house == undefined) {
                if (enabledOptions) {
                    loadResidentsEnabled();
                } else {
                    loadResidentsDisabled();
                }
            } else {
                $("#tableData").fadeOut(0);

                if (enabledOptions) {
                    vm.changesTitles();
                    Resident.query({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage

                    }, onSuccess);

                    function onSuccess(data, headers) {

                        vm.residents = data;
                        for (var i = 0; i < vm.residents.length; i++) {

                            if (house.id === vm.residents[i].houseId) {
                                residentsByHouse.push(vm.residents[i])
                            }
                        }
                        vm.residents = formatResidents(residentsByHouse);
                        console.log('aqui');

                        setTimeout(function() {
                            $("#tableData").fadeIn(300);
                        }, 200)
                    }


                } else {
                    $scope.changesTitles();
                    Resident.query({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage

                    }, onSuccess);

                    function onSuccess(data, headers) {
                        vm.residents = data;
                        for (var i = 0; i < vm.residents.length; i++) {
                            if (house.id === vm.residents[i].houseId) {
                                residentsByHouse.push(vm.residents[i])
                            }
                        }
                        vm.residents = formatResidents(residentsByHouse);
                        $("#loadingIcon").fadeOut(0);
                        setTimeout(function() {
                            $("#tableData").fadeIn(300);
                        }, 200)
                    }
                }
            }
        }
        vm.deleteResident = function(id_resident, name,lastname) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar al residente " + name + " " + lastname +"?",
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
                            if (enabledOptions) {
                                loadResidentsEnabled();
                            } else {


                            }
                        }
                    }
                }
            });


        };
    vm.disableEnabledResident = function(resident) {

        var correctMessage;
        if (enabledOptions) {
            correctMessage = "¿Está seguro que desea deshabilitar al residente " + resident.name + " " + resident.lastname + "?";
        } else {
            correctMessage = "¿Está seguro que desea habilitar al residente " + resident.name  + "?";
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
                        resident.enabled = 0;
                        Resident.update(resident,onSuccess);
                           function onSuccess(data, headers) {
                              User.getUserById({id: resident.userId},onSuccess);
                                 function onSuccess(data, headers) {
                                  if(resident.isOwner==1){
                                  data.activated = 0;
                                    User.update(data,onSuccessUser);
                                     function onSuccessUser(data, headers) {
                                         toastr["success"]("Se ha deshabilitado el residente correctamente.");
                                         bootbox.hideAll();
                                     }
                                  } else{
                                       toastr["success"]("Se ha deshabilitado el residente correctamente.");
                                       bootbox.hideAll();
                                  }
                                  }
                            }

                    } else {
                       resident.enabled = 1;
                       Resident.update(resident,onSuccess);
                          function onSuccess(data, headers) {
                             User.getUserById({id: resident.userId},onSuccess);
                                function onSuccess(data, headers) {
                                 if(resident.isOwner==1){
                                 data.activated = 1;
                                   User.update(data,onSuccessUser);
                                    function onSuccessUser(data, headers) {
                                        toastr["success"]("Se ha habilitado el residente correctamente.");
                                        bootbox.hideAll();
                                    }
                                 } else{
                                      toastr["success"]("Se ha habilitado el residente correctamente.");
                                      bootbox.hideAll();
                                 }

                                 }
                           }
                    }

                }
            }
        });
    };
        function formatResidents(residents) {
            var formattedResidents = [];
            for (var i = 0; i < residents.length; i++) {

                for (var e = 0; e < vm.houses.length; e++) {
                    if (residents[i].houseId == vm.houses[e].id) {

                        residents[i].house_id = vm.houses[e].housenumber;
                    }
                }

            }
            return residents;
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
    }
})();
