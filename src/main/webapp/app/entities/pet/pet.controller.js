(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PetController', PetController);

    PetController.$inject = ['$state','CommonMethods', 'Modal', 'Pet', 'ParseLinks', 'AlertService', 'paginationConstants', 'House', '$localStorage', 'globalCompany', '$rootScope'];

    function PetController($state,CommonMethods, Modal, Pet, ParseLinks, AlertService, paginationConstants, House, $localStorage, globalCompany, $rootScope) {
        var vm = this;
        vm.pets = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.filter = {
            houseId: "empty",
            name: " ",
        };
        $rootScope.active = "pet"
        $rootScope.mainTitle = "mascotas"
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.isReady = false;

        vm.filterPets = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.pets = [];
            loadAll();
        };
        vm.changeHouse = function (house, i) {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.pets = [];
            $localStorage.infoHouseNumber = house;
            vm.infoHouseResident = house;
            if (house !== undefined) {
                vm.selectedIndex = i + 1;
                vm.filter.houseId = house.id;
                $rootScope.mainTitle = "Mascotas de la filial " + house.housenumber;
            } else {
                $rootScope.mainTitle = "Mascotas de todas las filiales";
                vm.selectedIndex = 0;
                vm.filter.houseId = house;
            }
            loadAll();
        };
        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            if ($localStorage.infoHouseNumber !== undefined || $localStorage.infoHouseNumber !== null) {
                vm.changeHouse($localStorage.infoHouseNumber, 1);
            } else {
                loadAll();
            }
        }

        vm.editPet = function (pet) {
            var encryptedId = CommonMethods.encryptIdUrl(pet.id)
            $state.go('pet.edit', {
                id: encryptedId
            })
        };
        vm.deletePet = function (pet) {
            Modal.confirmDialog("¿Está seguro que desea eliminar a la mascota?", "Una vez eliminada no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    Pet.delete({
                        id: pet.id
                    }, function () {
                        Modal.toast("Se ha eliminado la mascota correctamente.");
                        Modal.hideLoadingBar();
                        vm.filterPets();
                    });
                });
        }

        function loadAll() {
            if (vm.filter.houseId == undefined) {
                vm.filter.houseId = "empty"
            }
            if (vm.filter.name == "" || vm.filter.name == undefined) {
                vm.filter.name = " ";
            }
            if (vm.filter.houseId == "empty") {
                Pet.getByCompany({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    companyId: globalCompany.getId(),
                    name: vm.filter.name,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Pet.getByHouse({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    houseId: vm.filter.houseId,
                    name: vm.filter.name,
                    sort: sort()
                }, onSuccess, onError);
            }


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
                for (var i = 0; i < data.length; i++) {
                    vm.pets.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.pets = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
