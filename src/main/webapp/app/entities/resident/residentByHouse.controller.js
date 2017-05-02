(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseController', ResidentByHouseController);

    ResidentByHouseController.$inject = ['DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope','JhiTrackerService'];

    function ResidentByHouseController(DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope,JhiTrackerService) {
            $rootScope.active = "residentsHouses";
        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.userId = $rootScope.companyUser.id;
        vm.loadPage = loadPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.changesTitles = function() {
            if (enabledOptions) {
                vm.titleCondominosIndex = "Residentes de la filial ";
                vm.buttonTitle = "Ver residentes deshabilitados";
                vm.actionButtonTitle = "Deshabilitar";
                 vm.iconDisabled = "fa fa-user-times";
                 vm.color = "red";
            } else {
                vm.titleCondominosIndex = "Residentes de la filial (deshabilitados)";
                vm.buttonTitle = "Ver residentes habilitados";
                vm.actionButtonTitle = "Habilitar";
               vm.iconDisabled = "fa fa-undo";
                 vm.color = "green";
            }
        }
        loadResidents();

        function loadResidents() {

            if (enabledOptions) {
                vm.changesTitles();
                Resident.findResidentesEnabledByHouseId({
                    houseId: $rootScope.companyUser.houseId
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Resident.findResidentesDisabledByHouseId({
                    houseId: $rootScope.companyUser.houseId
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {
               vm.residents = data;
                   setTimeout(function() {
                             $("#loadingIcon").fadeOut(300);
                   }, 400)
                    setTimeout(function() {
                        $("#residents_container").fadeIn('slow');
                    },700 )
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.switchEnabledDisabledResidents = function() {
             $("#residents_container").fadeOut(0);
             setTimeout(function() {
                 $("#loadingIcon").fadeIn(100);
             }, 200)
            enabledOptions = !enabledOptions;
            loadResidents();
        }
        vm.disableEnabledResident = function(resident) {

            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + resident.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + resident.name + "?";
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
                        if (enabledOptions) {
                            resident.enabled = 0;
                            Resident.update(resident, onSuccess);
                            function onSuccess(data, headers) {
                            JhiTrackerService.sendResident(data);
                                if (resident.isOwner == 1) {
                                    User.getUserById({
                                        id: resident.userId
                                    }, onSuccess);
                                    function onSuccess(data, headers) {
                                        data.activated = 0;
                                        User.update(data, onSuccessUser);

                                        function onSuccessUser(data, headers) {
                                            toastr["success"]("Se ha deshabilitado el residente correctamente.");
                                            bootbox.hideAll();
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
                            JhiTrackerService.sendResident(data);
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
