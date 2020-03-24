(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TenantController', TenantController);

    TenantController.$inject = ['$localStorage', '$scope', '$state', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', 'WSDeleteEntity', 'Modal', 'globalCompany', '$mdDialog'];

    function TenantController($localStorage, $scope, $state, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, WSDeleteEntity, Modal, globalCompany, $mdDialog) {
        $rootScope.active = "tenant";
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
                $rootScope.mainTitle = "Inquilinos de la filial " + house.housenumber;
            } else {
                $rootScope.mainTitle = "Inquilinos de todas las filiales";
                vm.selectedIndex = 0;
                vm.filter.houseId = house;
            }
            console.log('4')
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
        $rootScope.mainTitle = "Inquilinos de todas las filiales";
        vm.isReady = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.showFilterDiv = false;
        vm.consulting = false;

        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('tenant-detail', {
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
            $state.go('tenant.edit', {
                id: encryptedId
            })
        }


        vm.filterResidents = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents = [];
            console.log('3')
            loadOwners();
        };

        function loadPage(page) {
            vm.page = page;
            console.log('2')
            loadOwners();
        }

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            Principal.identity().then(function (account) {
                vm.account = account;
                switch (account.authorities[0]) {
                    case "ROLE_OWNER":
                        vm.changeHouse($localStorage.houseSelected, 1);
                        break;
                    case "ROLE_USER":
                        vm.changeHouse($localStorage.houseSelected, 1);
                        break;
                    case "ROLE_MANAGER":
                        if ($localStorage.infoHouseNumber !== undefined || $localStorage.infoHouseNumber !== null) {
                            vm.changeHouse($localStorage.infoHouseNumber, 1);
                        } else {
                            loadOwners();
                        }
                        break;
                    case "ROLE_JD":
                        if ($localStorage.infoHouseNumber !== undefined || $localStorage.infoHouseNumber !== null) {
                            vm.changeHouse($localStorage.infoHouseNumber, 1);
                        } else {
                            console.log('1')
                            loadOwners();
                        }
                        break;
                }
            })



        }


        function loadOwners() {
            if (vm.filter.houseId == undefined) {
                vm.filter.houseId = "empty";
            }
            if (vm.filter.name == "" || vm.filter.name == undefined) {
                vm.filter.name = " ";
            }
            Resident.getTenants({
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
            console.log(data)
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
