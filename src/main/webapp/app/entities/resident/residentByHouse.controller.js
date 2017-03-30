(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseController', ResidentByHouseController);

    ResidentByHouseController.$inject = ['DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope'];

    function ResidentByHouseController(DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope) {
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
        loadResidents();

        function loadResidents() {

                Resident.findResidentesByHouseId({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    houseId: $rootScope.companyUser.houseId
                }).$promise.then(onSuccess, onError);


            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data) {
              vm.queryCount = vm.totalItems;
              vm.page = pagingParams.page;
               vm.residents = data;
               console.log(vm.residents)
                $("#loadingIcon").fadeOut(0);
                setTimeout(function() {
                    $("#residents_container").fadeIn(300);
                }, 200)
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
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
                        CommonMethods.waitingMessage();
                        if (enabledOptions) {
                            resident.enabled = 0;
                            Resident.update(resident, onSuccess);

                            function onSuccess(data, headers) {
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
