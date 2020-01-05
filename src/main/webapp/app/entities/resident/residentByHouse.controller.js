(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseController', ResidentByHouseController);

    ResidentByHouseController.$inject = ['$state', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', 'globalCompany', 'Modal'];

    function ResidentByHouseController($state, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, globalCompany, Modal) {
        $rootScope.active = "residentsHouses";
        var enabledOptions = true;
        var vm = this;
        $rootScope.mainTitle = "Usuarios de la filial";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.editResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('residentByHouse.edit', {
                id: encryptedId
            })
        }
        setTimeout(function () {
            // vm.userId = $rootScope.companyUser.id;

            vm.loadPage = loadPage;
            vm.openFile = DataUtils.openFile;
            vm.byteSize = DataUtils.byteSize;

            House.get({id: globalCompany.getHouseId()}).$promise.then(onSuccess);

            function onSuccess(house) {
                // if (house.securityKey == null && house.emergencyKey == null) {
                //     Modal.actionToast("Sus claves de seguridad aún no han sido definidas.", "Establecer ahora", function () {
                //         $state.go('keysConfiguration');
                //     })
                //
                // }
            }
        }, 500)


        vm.changesTitles = function () {
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
        };
        loadResidents();
        console.log(globalCompany.getHouseId());

        function loadResidents() {
            if (enabledOptions) {
                vm.changesTitles();
                Resident.findResidentesEnabledByHouseId({
                    houseId: globalCompany.getHouseId()
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Resident.findResidentesDisabledByHouseId({
                    houseId: globalCompany.getHouseId()
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {
                vm.residents = data;
                vm.isReady = true;
                vm.isReady2 = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident-detail', {
                id: encryptedId
            })
        }
        vm.switchEnabledDisabledResidents = function () {
            vm.isReady2 = false;
            enabledOptions = !enabledOptions;
            loadResidents();
        }
        vm.disableEnabledResident = function (resident) {

            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + resident.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + resident.name + "?";
            }

            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                if (enabledOptions) {
                    resident.enabled = 0;
                    Resident.update(resident, onSuccessDisabled);
                } else {
                    resident.enabled = 1;
                    Resident.update(resident, onSuccessEnabled);
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
                    Modal.toast("Se ha deshabilitado el residente correctamente.");
                    Modal.hideLoadingBar();

                }

                function onSuccessGetUserDisabled(data, headers) {
                    data.activated = 0;
                    User.update(data, onSuccessUser);

                    function onSuccessUser(data, headers) {
                        Modal.toast("Se ha deshabilitado el residente correctamente.");
                        Modal.hideLoadingBar();
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
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha habilitado el residente correctamente.");
                    loadResidents();
                }

                function onSuccessUserEnabled(data, headers) {
                    data.activated = 1;
                    User.update(data, onSuccessUser);

                    function onSuccessUser(data, headers) {
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha habilitado el residente correctamente.");
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
