(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OwnerController', OwnerController);

    OwnerController.$inject = ['$localStorage', '$scope', '$state', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', 'WSDeleteEntity', 'Modal', 'globalCompany', '$mdDialog'];

    function OwnerController($localStorage, $scope, $state, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, WSDeleteEntity, Modal, globalCompany, $mdDialog) {
        $rootScope.active = "owners";
        var vm = this;

        vm.changeHouse = function (house, i) {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents = [];
            $localStorage.infoHouseNumber = house;
            vm.infoHouseResident = house;

            if (house !== undefined) {
                vm.selectedIndex = i + 1;
                vm.filter.houseId = house.id;
                $rootScope.mainTitle = "Usuarios de la filial " + house.housenumber;
            } else {
                $rootScope.mainTitle = "Usuarios de todas las filiales";
                vm.selectedIndex = 0;
                vm.filter.houseId = house;
            }

            loadOwners();

        };
        vm.enabledOptions = true;
        vm.page = 0;
        vm.links = {
            last: 0
        };

        vm.filter = {
            owner: "empty",
            houseId: "empty",
            name: " "
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
            $state.go('owner-detail', {
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
            $state.go('owner.edit', {
                id: encryptedId
            })
        }



        vm.filterResidents = function(){
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents= [];
            loadOwners();
        };

        function loadPage(page) {
            vm.page = page;
            loadOwners();
        }

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            if ($localStorage.infoHouseNumber !== undefined || $localStorage.infoHouseNumber !== null) {
                vm.changeHouse($localStorage.infoHouseNumber, 1);
            } else {
                loadOwners();
            }
        }


        function loadOwners() {
            if (vm.filter.houseId == undefined) {
                vm.filter.houseId = "empty";
            }
            if (vm.filter.name == "" || vm.filter.name == undefined) {
                vm.filter.name = " ";
            }
            Resident.getOwners({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId(),
                name: vm.filter.name,
                houseId: vm.filter.houseId
            }, onSuccess, onError);


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
                    if (value.id == $localStorage.infoHouseNumber.id) {
                        vm.selectedIndex = key + 1;
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
                                data.email = data.email + Math.floor(Math.random() * 1000000000);
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

    }
})();
