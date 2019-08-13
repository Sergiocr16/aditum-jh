(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HousesInfoAccessDoorController', HousesInfoAccessDoorController);

    HousesInfoAccessDoorController.$inject = ['Auth', '$timeout', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'ParseLinks', 'globalCompany', 'Modal'];

    function HousesInfoAccessDoorController(Auth, $timeout, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, ParseLinks, globalCompany, Modal) {
        var vm = this;
        vm.queryType = 1;
        vm.houseSelected = -1;
        $rootScope.houseSelected = vm.houseSelected;
        vm.condominiumSelected = -1;
        vm.noDataFound = false;
        $rootScope.condominiumSelected = vm.condominiumSelected;
        vm.isReady = true;
        vm.consultingAll = true;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.itemsPerPage = 9;
        vm.residents = [];
        vm.vehicules = [];
        vm.selectHouse = function (house) {
            $rootScope.houseSelected = vm.houseSelected;
            vm.showingData = false;
            if (vm.houseSelected === -1) {
                vm.isReady = true;
                vm.consultingAll = true;
                vm.residents = [];
                vm.vehicules = [];
            } else {
                vm.consultingAll = false;
                vm.filterInfo();
            }
        };
        vm.selectCondo = function (condoId) {
            vm.houseSelected = -1;
            vm.residents = [];
            vm.vehicules = [];
            vm.consultingAll = true;
            vm.isReady = true;
            vm.showingData = false;
            $rootScope.condominiumSelected = vm.condominiumSelected;
        };

        vm.changeQueryType = function (type) {
            if(type!==vm.queryType) {
                vm.queryType = type;
                vm.showingData = false;
                if (vm.houseSelected === -1) {
                    vm.isReady = true;
                    vm.consultingAll = true;
                    vm.residents = [];
                    vm.vehicules = [];
                } else {
                    vm.consultingAll = false;
                    vm.filterInfo();
                }
            }
        }
        vm.showKeys = function (houseSelected) {
            var emergencyKey, securityKey;
            emergencyKey = houseSelected.emergencyKey == null ? "No definida" : houseSelected.emergencyKey;
            securityKey = houseSelected.securityKey == null ? "No definida" : houseSelected.securityKey;
            Modal.customDialog("<md-dialog>" +
                "<md-dialog-content class='md-dialog-content text-center'>" +
                "<h1 class='md-title'>Claves filial <b>" + houseSelected.housenumber + "</b></h1>" +
                "<div class='md-dialog-content-body'>" +
                "<p>Emergencia: <b style='font-size: 20px'>" + emergencyKey + "</b></p>" +
                "<p>Seguridad: <b style='font-size: 20px'>" + securityKey + "</b></p>" +
                "</div>" +
                "</md-dialog-content>" +
                "</md-dialog>")
        };
        vm.filterInfo = function () {
            vm.isReady = false;
            vm.showingData = true;
            vm.noDataFound = false;
            switch (vm.queryType) {
                case 1:
                    vm.filterResidents();
                    break;
                case 2:
                    vm.filterVehicules();
                    break;
            }
        };
// RESIDENTES
        vm.filterResidents = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents = [];
            // if (vm.condominiumSelected === -1) {
            //     loadResidentsMacro();
            // } else {
                loadResidents();
            // }
        };

        function loadResidentsMacro() {
            vm.consultingAll = false;
            var filter = vm.filter;
            if (vm.filter === "" || vm.filter === undefined) {
                filter = " ";
            }
            Resident.findResidentsMacroByFilter({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sortResidents(),
                macroId: $rootScope.macroCondominium.id,
                filter: filter,
            }, onSuccessResidents, onError);
        }

        function loadResidents() {
            var houseId = {};
            if (vm.houseSelected === -1) {
                houseId.id = "empty";
            } else {
                houseId.id = vm.houseSelected.id;
            }
            var filter = vm.filter;
            if (vm.filter === "" || vm.filter === undefined) {
                filter = " ";
            }
            Resident.getResidents({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sortResidents(),
                companyId: globalCompany.getId(),
                name: filter,
                houseId: houseId.id,
                owner: "empty",
                enabled: 1,
            }, onSuccessResidents, onError);
        }

        function sortResidents() {
            var result = [];
            if (vm.predicate !== 'name') {
                result.push('name,asc');
            }
            return result;
        }

        function onSuccessResidents(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            for (var i = 0; i < data.length; i++) {
                vm.residents.push(data[i])
            }

            if (vm.residents.length === 0 && vm.filter !== undefined) {
                vm.noDataFound = true;
            } else {
                vm.noDataFound = false;
            }
            vm.isReady = true;
        }

        vm.loadPageResidents = function (page) {
            vm.page = page;
            if (vm.condominiumSelected === -1) {
                loadResidentsMacro();
            } else {
                loadResidents();
            }
        };

        // VEHICULOS
        vm.filterVehicules = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.vehicules = [];
            // if (vm.condominiumSelected === -1) {
            //     loadVehiculesMacro();
            // } else {
                loadVehicules();
            // }
        };

        function loadVehicules() {
            var houseId = {};
            if (vm.houseSelected === -1) {
                houseId.id = "empty";
            } else {
                houseId.id = vm.houseSelected.id;
            }
            var filter = vm.filter;
            if (vm.filter === "" || vm.filter === undefined) {
                filter = " ";
            }
            Vehicule.getVehicules({
                page: vm.page,
                size: vm.itemsPerPage,
                houseId: houseId.id,
                licencePlate: filter,
                enabled: 1,
                sort: sortVehicules(),
                companyId: globalCompany.getId(),
            }, onSuccessVehicules, onError);
        }

        function loadVehiculesMacro() {
            vm.consultingAll = false;
            var filter = vm.filter;
            if (vm.filter === "" || vm.filter === undefined) {
                filter = " ";
            }
            Vehicule.findVehiculesMacroByFilter({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sortVehicules(),
                macroId: $rootScope.macroCondominium.id,
                filter: filter,
            }, onSuccessVehicules, onError);
        }

        function sortVehicules() {
            var result = [];
            if (vm.predicate !== 'licenseplate') {
                result.push('licenseplate,asc');
            }
            return result;
        }

        function onSuccessVehicules(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            for (var i = 0; i < data.length; i++) {
                vm.vehicules.push(data[i])
            }
            vm.isReady = true;
        }

        vm.loadPageVehicules = function (page) {
            vm.page = page;
            if (vm.condominiumSelected === -1) {
                loadVehiculesMacro();
            } else {
                loadVehicules();
            }
        };

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
