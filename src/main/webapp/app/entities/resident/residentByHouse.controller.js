(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseController', ResidentByHouseController);

    ResidentByHouseController.$inject = ['$state','DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope','WSResident'];

    function ResidentByHouseController($state,DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope,WSResident) {
        $rootScope.active = "residentsHouses";
        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.editResident = function(id){
        var encryptedId = CommonMethods.encryptIdUrl(id)
        $state.go('residentByHouse.edit', {
            id: encryptedId
        })
        }
        vm.userId = $rootScope.companyUser.id;
        vm.loadPage = loadPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        House.get({ id: $rootScope.companyUser.houseId}).$promise.then(onSuccess);

          function onSuccess (house) {
              if (house.securityKey == null && house.emergencyKey == null) {
                   bootbox.confirm({
                       message: '<div class="gray-font font-15">Sus claves de seguridad aun no han sido definidas, recuerde que el tener establecidas las claves le provee mayor seguridad.</div>',
                       closeButton: false,

                       buttons: {
                           confirm: {
                               label: 'Establecer ahora',
                               className: 'btn-success'
                           },
                           cancel: {
                               label: 'Recordarmelo luego',
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
        setTimeout(function(){ loadResidents();},600)
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
                            Resident.update(resident, onSuccessDisabled);
                        } else {
                            resident.enabled = 1;
                            Resident.update(resident, onSuccessEnabled);
                        }

                    }
                }

            });

            function onSuccessDisabled(data, headers) {
                WSResident.sendActivity(data);
                if (resident.isOwner == 1) {
                    User.getUserById({
                        id: resident.userId
                    }, onSuccessGetUserDisabled);

                } else {
                    loadResidents();
                    toastr["success"]("Se ha deshabilitado el residente correctamente.");
                    bootbox.hideAll();
                }
                function onSuccessGetUserDisabled(data, headers) {
                    data.activated = 0;
                    User.update(data, onSuccessUser);

                    function onSuccessUser(data, headers) {
                        toastr["success"]("Se ha deshabilitado el residente correctamente.");
                        bootbox.hideAll();
                    }
                }
            }

            function onSuccessEnabled(data, headers) {
                WSResident.sendActivity(data);
                if (resident.isOwner == 1) {
                    User.getUserById({
                        id: resident.userId
                    }, onSuccessUserEnabled);

                } else {
                    bootbox.hideAll();
                    toastr["success"]("Se ha habilitado el residente correctamente.");
                    loadResidents();
                }

                function onSuccessUserEnabled(data, headers) {
                    data.activated = 1;
                    User.update(data, onSuccessUser);

                    function onSuccessUser(data, headers) {
                        toastr["success"]("Se ha habilitado el residente correctamente.");
                        bootbox.hideAll();
                    }
                }
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
