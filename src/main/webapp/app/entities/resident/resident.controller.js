(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentController', ResidentController);

    ResidentController.$inject = ['$localStorage','$scope','$state', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', 'WSDeleteEntity', 'Modal', 'globalCompany','$mdDialog'];

    function ResidentController($localStorage,$scope,$state, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, WSDeleteEntity, Modal, globalCompany,$mdDialog) {
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
            if(vm.filter.houseId!=="empty"){
                House.get({
                    id: vm.filter.houseId
                }, function (house) {
                    $localStorage.infoHouseNumber = house;
                    vm.infoHouseResident= house;
                    $rootScope.mainTitle = "Usuarios de la filial " + house.housenumber;
                    vm.filterResidents();
                });
            }else{
                $rootScope.mainTitle = "Usuarios de todas las filiales" ;
                vm.filterResidents();
            }

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
        };
        vm.changeHouse = function (house,i) {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents= [];
            $localStorage.infoHouseNumber = house;
            vm.infoHouseResident = house;

            if(house!==undefined){
                vm.selectedIndex = i+1;
                vm.filter.houseId = house.id;
                $rootScope.mainTitle = "Usuarios de la filial " + house.housenumber;
            }else{
                $rootScope.mainTitle = "Usuarios de todas las filiales" ;
                vm.selectedIndex = 0;
                vm.filter.houseId = house;
            }


            vm.vehicules = [];
            loadResidents();

        };
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
        };
        vm.residents = [];
        vm.radiostatus = true;
        $rootScope.mainTitle = "Usuarios de todas las filiales";
        vm.isReady = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.showFilterDiv = false;
        vm.consulting = false;

        vm.editResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident.edit', {
                id: encryptedId
            })
        };

        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident-detail', {
                id: encryptedId
            })
        };

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
        };

        function loadPage(page) {
            vm.page = page;
            loadResidents();
        }
        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);
        function onSuccessHouses(data, headers) {
            vm.houses = data;
            if($localStorage.infoHouseNumber!==undefined || $localStorage.infoHouseNumber!==null){
                vm.changeHouse($localStorage.infoHouseNumber,1);
            }else{
                loadResidents();
            }
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
            angular.forEach(vm.houses, function (value, key) {
                if ($localStorage.infoHouseNumber != null || $localStorage.infoHouseNumber != undefined) {
                    if(value.id == $localStorage.infoHouseNumber.id ){
                        vm.selectedIndex = key+1;
                        vm.filter.houseId = value.id;
                    }
                }
            });
            vm.isReady = true;
            vm.isReady2 = true;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.switchEnabledResidents = function () {
            vm.filter.enabled = 1;
            vm.enabledOptions = 1;
            vm.filterResidents();
        }
        vm.switchDisabledResidents = function () {
            vm.filter.enabled = 0;
            vm.enabledOptions = 0;
            vm.filterResidents();
        }



        vm.deleteResident = function (resident) {
            vm.residentToDelete = resident;
            Modal.confirmDialog("¿Está seguro que desea eliminar al usuario " + resident.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    vm.login = resident.userLogin;
                    Resident.delete({
                        id: resident.id
                    }, function () {
                        if (vm.login !== null) {
                            User.getUserById({
                                id: resident.userId
                            }, function (data) {
                                data.activated = 0;
                                data.email = data.email+Math.floor(Math.random() * 1000000000);
                                User.update(data, onSuccessDisabledUser);

                                function onSuccessDisabledUser(data, headers) {
                                    Modal.toast("Se ha eliminado el usuario correctamente.");
                                    Modal.hideLoadingBar();
                                    vm.filterResidents();
                                }
                            });
                        } else {
                            Modal.toast("Se ha eliminado el usuario correctamente.");
                            vm.filterResidents();
                            Modal.hideLoadingBar();
                            WSDeleteEntity.sendActivity({type: 'resident', id: vm.residentToDelete.id})
                        }

                    });
                });
        };

        vm.disableEnabledResident = function (residentInfo) {
            var correctMessage;
            if (vm.enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al usuario " + residentInfo.name + "?";

            } else {
                correctMessage = "¿Está seguro que desea habilitar al usuario " + residentInfo.name + "?";
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
                vm.filterResidents();
                Modal.toast("Se ha deshabilitado el usuario correctamente.");
                Modal.hideLoadingBar();
            }
        }

        function onSuccessGetDisabledUser(data, headers) {
            data.activated = 0;
            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {
                Modal.toast("Se ha deshabilitado el usuario correctamente.");
                Modal.hideLoadingBar();
                vm.filterResidents();
            }
        }


        function onSuccessEnabledResident(data, headers) {
            WSResident.sendActivity(data);
            if (data.isOwner == 1) {
                User.getUserById({
                    id: data.userId
                }, onSuccessGetEnabledUser);
            } else {
                Modal.toast("Se ha habilitado el usuario correctamente.");
                Modal.hideLoadingBar();

                vm.filterResidents();
            }
        }

        function onSuccessGetEnabledUser(data, headers) {
            data.activated = 1;
            User.update(data, onSuccessEnabledUser);

            function onSuccessEnabledUser(data, headers) {
                Modal.toast("Se ha habilitado el usuario correctamente.");
                Modal.hideLoadingBar();
                vm.filterResidents();
            }
        }
    }
})();
