(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeController', VehiculeController);

    VehiculeController.$inject = ['$state', 'CommonMethods', '$rootScope', 'Vehicule', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'WSVehicle', 'WSDeleteEntity', 'globalCompany','Modal'];

    function VehiculeController($state, CommonMethods, $rootScope, Vehicule, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, WSVehicle, WSDeleteEntity, globalCompany,Modal) {
        $rootScope.active = "vehicules";
        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.showFilterDiv = false;
        vm.house = "-1";
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.radioEnablestatus = true;
        vm.transition = transition;
        $rootScope.mainTitle = "Vehículos";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.editVehicle = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('vehicule.edit', {
                id: encryptedId
            })

        }
        vm.changesTitles = function () {
            if (enabledOptions) {
                vm.title = "Vehículos habilitados";
                vm.buttonTitle = "Ver vehículos deshabilitados";
                vm.titleDisabledButton = "Deshabilitar vehículo";
                vm.titleDisabledButton = "Deshabilitar vehículo";
                vm.actionButtonTitle = "Deshabilitar";
                vm.iconDisabled = "fa fa-user-times";
                vm.color = "red-font";

            } else {
                vm.title = "Vehículos deshabilitados";
                vm.buttonTitle = "Ver vehículos habilitados";
                vm.actionButtonTitle = "Habilitar";
                vm.iconDisabled = "fa fa-undo";
                vm.titleDisabledButton = "Habilitar vehículo";
                vm.color = "green";
            }
        }
        loadHouses();

        function loadHouses() {
            House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                vm.houses = data;
                loadVehicules();
            }

        }

        function loadVehicules(option) {
            if (enabledOptions) {
                vm.changesTitles();
                Vehicule.vehiculesEnabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: globalCompany.getId(),
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Vehicule.vehiculesDisabled({
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

            function onSuccess(data) {
                if (option !== 1) {
                    vm.queryCount = data.length;
                    vm.page = pagingParams.page;
                    vm.vehicules = formatVehicules(data);
                } else {
                    var vehiculesByHouse = [];
                    vm.vehicules = data;
                    for (var i = 0; i < vm.vehicules.length; i++) {
                        if (vm.house.id === vm.vehicules[i].houseId) {
                            vehiculesByHouse.push(vm.vehicules[i])
                        }
                    }
                    vm.vehicules = formatVehicules(vehiculesByHouse);
                }
                vm.isReady = true;
                vm.isReady2 = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

        }


        vm.switchEnabledVehicules = function () {
            vm.isReady2 = false;

            enabledOptions = true;
            vm.radioEnablestatus = true;
            vm.radioDisablestatus = false;
            vm.findVehiculesByHouse(vm.house);
        }
        vm.switchDisabledVehicules = function () {
            vm.isReady2 = false;
            enabledOptions = false;
            vm.radioEnablestatus = false;
            vm.radioDisablestatus = true;
            vm.findVehiculesByHouse(vm.house);

        }

        vm.findVehiculesByHouse = function (house) {

            vm.house = house;

            if (house == undefined || house=='-1') {
                loadVehicules();
            } else {
                loadVehicules(1);
            }
        }

        function formatVehicules(vehicules) {
            var formattedVehicules = [];
            for (var i = 0; i < vehicules.length; i++) {

                for (var e = 0; e < vm.houses.length; e++) {
                    if (vehicules[i].houseId == vm.houses[e].id) {
                        vehicules[i].house_id = vm.houses[e].housenumber;
                    }
                }
            }

            return vehicules;
        }

        vm.deleteVehicule = function (id_vehicule, license_plate) {
            Modal.confirmDialog("¿Está seguro que desea eliminar al vehículo "+ license_plate + "?","Una vez eliminado no podrá recuperar los datos",
                function(){
                    Modal.showLoadingBar();
                    Vehicule.delete({
                        id: id_vehicule
                    }, onSuccessDelete);
                });


            function onSuccessDelete(data, headers) {
                Modal.hideLoadingBar();
                Modal.toast("Se ha eliminado el vehículo correctamente.");
                loadVehicules();
                WSDeleteEntity.sendActivity({type: 'vehicle', id: id_vehicule})
            }

        };

        vm.disableEnabledVehicule = function (vehicule) {

            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al vehículo " + vehicule.licenseplate + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al vehículo " + vehicule.licenseplate + "?";
            }
            Modal.confirmDialog(correctMessage,"",function(){
                Modal.showLoadingBar();
                if (enabledOptions) {
                    vehicule.enabled = 0;
                    Vehicule.update(vehicule, onSuccessDisable);


                } else {
                    vehicule.enabled = 1;
                    Vehicule.update(vehicule, onSuccessEnable);

                }
            });


            function onSuccessEnable(data, headers) {
                WSVehicle.sendActivity(data);
                loadVehicules();

                Modal.toast("Se ha habilitado el vehículo correctamente.");
                Modal.hideLoadingBar();

            }

            function onSuccessDisable(data, headers) {
                WSVehicle.sendActivity(data);
                loadVehicules();

                Modal.toast("Se ha deshabilitado el vehículo correctamente.");
                Modal.hideLoadingBar();
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
