(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeController', VehiculeController);

    VehiculeController.$inject = ['$localStorage','$scope', '$mdDialog', '$state', 'CommonMethods', '$rootScope', 'Vehicule', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'WSVehicle', 'WSDeleteEntity', 'globalCompany', 'Modal'];

    function VehiculeController($localStorage,$scope, $mdDialog, $state, CommonMethods, $rootScope, Vehicule, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, WSVehicle, WSDeleteEntity, globalCompany, Modal) {
        $rootScope.active = "vehicules";
        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.radioEnablestatus = true;
        vm.transition = transition;
        $rootScope.mainTitle = "Vehículos de todas las filiales";
        vm.isReady = false;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.filter = {
            houseId: "empty",
            licensePlate: " ",
            enabled: 1
        };

        vm.open = function (ev) {
            $mdDialog.show({
                templateUrl: 'app/entities/vehicule/vehicules-filter.html',
                scope: $scope,
                preserveScope: true,
                targetEvent: ev
            });
        };

        vm.close = function () {

            $mdDialog.hide();
        };
        vm.closeAndFilter = function () {
            if(vm.filter.houseId!=="empty"){
                House.get({
                    id: vm.filter.houseId
                }, function (house) {
                    $localStorage.infoHouseNumber = house;
                    vm.infoHouseVehicule = house;
                    $rootScope.mainTitle = "Vehículos de la filial " + house.housenumber;
                    vm.filterVehicules();
                });
            }else{
                $rootScope.mainTitle = "Vehículos de todas las filiales" ;
                vm.filterVehicules();
            }



            $mdDialog.hide();
        };
        vm.filterVehicules = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.vehicules = [];
            loadVehicules();
        };
        vm.changeHouse = function (house,i) {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            $localStorage.infoHouseNumber = house;
            vm.infoHouseVehicule = house;

            if(house!==undefined){
                vm.selectedIndex = i+1;
                vm.filter.houseId = house.id;
                $rootScope.mainTitle = "Vehículos de la filial " + house.housenumber;
            }else{
                $rootScope.mainTitle = "Vehículos de todas las filiales" ;
                vm.selectedIndex = 0;
                vm.filter.houseId = house;
            }


            vm.vehicules = [];
            loadVehicules();

        };

        vm.itemsPerPage = 12;
        vm.vehicules = [];
        vm.editVehicle = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('vehicule.edit', {
                id: encryptedId
            })

        }
        vm.changesTitles = function () {
            if (vm.filter.enabled==1) {
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
        };
        loadHouses();

        function loadHouses() {
            House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
                if($localStorage.infoHouseNumber!==undefined || $localStorage.infoHouseNumber!==null){
                    vm.changeHouse($localStorage.infoHouseNumber,1);
                }else{
                    loadVehicules();
                }

            }
        }

        function loadVehicules() {
            if (vm.filter.houseId == undefined) {
                vm.filter.houseId = "empty"
            }
            if (vm.filter.licensePlate == "" || vm.filter.licensePlate == undefined) {
                vm.filter.licensePlate = " ";
            }
            vm.changesTitles();
            Vehicule.getVehicules({
                page: vm.page,
                size: vm.itemsPerPage,
                houseId: vm.filter.houseId,
                licencePlate: vm.filter.licensePlate,
                enabled: vm.filter.enabled,
                sort: sort(),
                companyId: globalCompany.getId(),
            }, onSuccess, onError);

            function sort() {
                var result = [];
                if (vm.predicate !== 'licenseplate') {
                    result.push('licenseplate,asc');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.vehicules.push(data[i])
                }
                vm.isReady = true;
                angular.forEach(vm.houses, function (value, key) {
                    if ($localStorage.infoHouseNumber != undefined) {
                        if(value.id == $localStorage.infoHouseNumber.id ){
                            vm.selectedIndex = key+1;
                            vm.filter.houseId = value.id;
                        }
                    }
                });
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.switchEnabledVehicules = function () {
            vm.isReady = false;
            vm.filter.enabled = 1;
            vm.actionButtonTitle = "Deshabilitar";
            vm.filterVehicules();
        }
        vm.switchDisabledVehicules = function () {
            vm.isReady = false;
            vm.filter.enabled = 0;
            vm.actionButtonTitle = "Habilitar";
            vm.filterVehicules();
        }


        vm.deleteVehicule = function (id_vehicule, license_plate) {
            Modal.confirmDialog("¿Está seguro que desea eliminar al vehículo " + license_plate + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    Vehicule.delete({
                        id: id_vehicule
                    }, onSuccessDelete);
                });


            function onSuccessDelete(data, headers) {
                Modal.hideLoadingBar();
                Modal.toast("Se ha eliminado el vehículo correctamente.");
                vm.filterVehicules();
                WSDeleteEntity.sendActivity({type: 'vehicle', id: id_vehicule})
            }

        };

        vm.disableEnabledVehicule = function (vehicule) {

            var correctMessage;
            if (vm.filter.enabled==1) {
                correctMessage = "¿Está seguro que desea deshabilitar al vehículo " + vehicule.licenseplate + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al vehículo " + vehicule.licenseplate + "?";
            }
            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                if (vm.filter.enabled==1) {
                    vehicule.enabled = 0;
                    Vehicule.update(vehicule, onSuccessDisable);


                } else {
                    vehicule.enabled = 1;
                    Vehicule.update(vehicule, onSuccessEnable);

                }
            });


            function onSuccessEnable(data, headers) {
                WSVehicle.sendActivity(data);
                vm.filterVehicules();
                Modal.toast("Se ha habilitado el vehículo correctamente.");
                Modal.hideLoadingBar();

            }

            function onSuccessDisable(data, headers) {
                WSVehicle.sendActivity(data);
                vm.filterVehicules();
                Modal.toast("Se ha deshabilitado el vehículo correctamente.");
                Modal.hideLoadingBar();
            }
        };

        function loadPage(page) {
            vm.page = page;
            loadVehicules();
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
