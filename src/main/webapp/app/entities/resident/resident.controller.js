(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentController', ResidentController);

    ResidentController.$inject = ['$scope','$state', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', 'WSDeleteEntity', 'Modal', 'globalCompany','$mdDialog'];

    function ResidentController($scope,$state, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, WSDeleteEntity, Modal, globalCompany,$mdDialog) {
        $rootScope.active = "residents";
        var vm = this;


        vm.open = function(ev) {
            $mdDialog.show({
                templateUrl: 'app/entities/resident/residents-filter.html',
                scope: $scope,
                preserveScope: true,
                targetEvent: ev
            });
        };

        vm.close = function() {
            $mdDialog.hide();
        };
        vm.closeAndFilter = function() {
            vm.filterResidents();
            $mdDialog.hide();
        };
        vm.filterResidents = function(){
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents= [];
            loadResidents();
        }

        vm.enabledOptions = true;
        vm.page = 0;
        vm.links = {
            last: 0
        };

        vm.filter = {
            owner : "empty",
            houseId: "empty",
            name:" ",
            enabled: 1
        }
        vm.residents = [];
        vm.radiostatus = true;
        $rootScope.mainTitle = "Usuarios autorizados";
        vm.isReady = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.showFilterDiv = false;
        vm.consulting = false;
        vm.editResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident.edit', {
                id: encryptedId
            })
        }

        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident-detail', {
                id: encryptedId
            })
        }
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = 9;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.filterAuthorized = "";
        vm.setAuthorizedView = function (val) {
            vm.filterAuthorized = val;
        }
        vm.editResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident.edit', {
                id: encryptedId
            })
        }


        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident-detail', {
                id: encryptedId
            })
        }
        vm.changeFilterShowing = function () {
            vm.showFilterDiv = !vm.showFilterDiv;
        }

        vm.changesTitles = function () {
            if (vm.enabledOptions) {
                vm.title = "Usuarios autorizados";
                vm.buttonTitle = "Ver usuarios autorizados deshabilitados";
                vm.actionButtonTitle = "Deshabilitar";
                vm.iconDisabled = "fa fa-user-times";
                vm.color = "red-font";
            } else {
                vm.title = "Usuarios deshabilitados";
                vm.buttonTitle = "Ver usuarios autorizados habilitados";
                vm.actionButtonTitle = "Habilitar";
                vm.iconDisabled = "fa fa-undo";
                vm.titleDisabledButton = "Habilitar usuario";
                vm.color = "green";
            }
        }

        function loadPage(page) {
            vm.page = page;
            loadResidents();
        }
        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            loadResidents();
        }


        function loadResidents() {
            if(vm.filter.houseId==undefined){
                vm.filter.houseId = "empty"
            }
            if(vm.filter.name==""||vm.filter.name==undefined){
                vm.filter.name = " ";
            }
            if (vm.enabledOptions) {
                vm.changesTitles();
                Resident.getResidents({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: globalCompany.getId(),
                    name: vm.filter.name,
                    houseId: vm.filter.houseId,
                    owner: vm.filter.owner,
                    enabled: vm.filter.enabled,
                },onSuccess, onError);
            } else {
                vm.changesTitles();
                Resident.getResidents({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: globalCompany.getId(),
                    name: vm.filter.name,
                    houseId: vm.filter.houseId,
                    owner: vm.filter.owner,
                    enabled: vm.filter.enabled,
                },onSuccess, onError);
            }

            function sort() {
                var result = [];
                if (vm.predicate !== 'name') {
                    result.push('name,asc');
                }
                return result;
            }
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            for (var i = 0; i < data.length; i++) {
                vm.residents.push(data[i])
            }
            vm.isReady = true;
            vm.isReady2 = true;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.switchEnabledResidents = function () {
            vm.filter.enabled = 1;
            vm.filterResidents();
        }
        vm.switchDisabledResidents = function () {
            vm.filter.enabled = 0;
            vm.filterResidents();
        }



        vm.deleteResident = function (resident) {
            vm.residentToDelete = resident;
            Modal.confirmDialog("¿Está seguro que desea eliminar al residente " + resident.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    vm.login = resident.userLogin;
                    Resident.delete({
                        id: resident.id
                    }, onSuccessDelete);
                });
        };

        function onSuccessDelete(result) {
            if (vm.login !== null) {
                User.delete({login: vm.login},
                    function () {
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha eliminado el residente correctamente.");
                        loadResidents();
                        WSDeleteEntity.sendActivity({type: 'resident', id: vm.residentToDelete.id})
                    });
            } else {
                Modal.toast("Se ha eliminado el residente correctamente.");
                loadResidents();
                Modal.hideLoadingBar();
                WSDeleteEntity.sendActivity({type: 'resident', id: vm.residentToDelete.id})
            }

        }

        vm.disableEnabledResident = function (residentInfo) {
            var correctMessage;
            if (vm.enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + residentInfo.name + "?";

            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + residentInfo.name + "?";
            }

            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                Resident.get({id: residentInfo.id}).$promise.then(onSuccessGetResident);
            });
        };


        function onSuccessGetResident(result) {
            enabledDisabledResident(result);
        }

        function enabledDisabledResident(resident) {
            if (vm.enabledOptions) {
                resident.enabled = 0;
                Resident.update(resident, onSuccessDisabledResident);
            } else {
                resident.enabled = 1;
                Resident.update(resident, onSuccessEnabledResident);

            }
        }

        function onSuccessDisabledResident(data, headers) {
            WSResident.sendActivity(data);
            if (data.isOwner == 1) {
                User.getUserById({
                    id: data.userId
                }, onSuccessGetDisabledUser);

            } else {
                loadResidents();

                Modal.toast("Se ha deshabilitado el residente correctamente.");
                Modal.hideLoadingBar();
            }
        }

        function onSuccessGetDisabledUser(data, headers) {
            data.activated = 0;
            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {

                Modal.toast("Se ha deshabilitado el residente correctamente.");
                Modal.hideLoadingBar();
                loadResidents();
            }
        }


        function onSuccessEnabledResident(data, headers) {
            WSResident.sendActivity(data);
            if (data.isOwner == 1) {
                User.getUserById({
                    id: data.userId
                }, onSuccessGetEnabledUser);

            } else {
                loadResidents();
            }
        }

        function onSuccessGetEnabledUser(data, headers) {
            data.activated = 1;
            User.update(data, onSuccessEnabledUser);

            function onSuccessEnabledUser(data, headers) {

                Modal.toast("Se ha habilitado el residente correctamente.");
                Modal.hideLoadingBar();
                loadResidents();
            }
        }
    }
})();
