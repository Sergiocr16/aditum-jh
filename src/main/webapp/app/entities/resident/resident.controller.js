(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentController', ResidentController);

    ResidentController.$inject = ['$state', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', 'WSDeleteEntity', 'Modal','globalCompany'];

    function ResidentController($state, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, WSDeleteEntity, Modal,globalCompany) {
        $rootScope.active = "residents";

        var enabledOptions = true;
        var vm = this;
        vm.radiostatus = true;
        $rootScope.mainTitle = "Usuarios autorizados";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.showFilterDiv = false;
        vm.house = "-1";
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
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
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
        vm.changeFilterShowing = function(){
            vm.showFilterDiv=!vm.showFilterDiv;

        }
        vm.changesTitles = function () {
            if (enabledOptions) {
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
        setTimeout(function () {
            loadHouses();
        }, 500)


        function loadHouses() {
            House.query({
                companyId: globalCompany.getId()
            }).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                vm.houses = data;
                vm.filterAuthorized = 2;
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
                    companyId: globalCompany.getId(),
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Resident.residentsDisabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: globalCompany.getId(),
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
                vm.isReady = true;
                vm.isReady2 = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.switchEnabledResidents = function () {
            vm.isReady2 = false;
            enabledOptions = true;
            vm.radiostatus = true;
            vm.findResidentsByHouse(vm.house);
        }
        vm.switchDisabledResidents = function () {
            vm.isReady2 = false;
            enabledOptions = false;
            vm.findResidentsByHouse(vm.house);
        }
        vm.findResidentsByHouse = function (house) {

            vm.house = house;
            if (house == undefined || house=='-1') {
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
                        if (residents[i].email == null) {
                            residents[i].email = "No registrado"
                        }
                        ;
                        if (residents[i].phonenumber == null) {
                            residents[i].phonenumber = "No registrado"
                        }
                        ;
                    }
                }
            }
            return residents;
        }

        vm.deleteResident = function (resident) {
            vm.residentToDelete = resident;
            Modal.confirmDialog("¿Está seguro que desea eliminar al residente "+ resident.name + "?","Una vez eliminado no podrá recuperar los datos",
                function(){
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
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + residentInfo.name + "?";

            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + residentInfo.name + "?";
            }

            Modal.confirmDialog(correctMessage,"",function(){
                Modal.showLoadingBar();
                Resident.get({id: residentInfo.id}).$promise.then(onSuccessGetResident);
            });
        };


        function onSuccessGetResident(result) {
            enabledDisabledResident(result);
        }

        function enabledDisabledResident(resident) {
            if (enabledOptions) {
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
