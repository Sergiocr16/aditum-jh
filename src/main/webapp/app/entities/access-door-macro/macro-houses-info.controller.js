(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroHousesAccessDoorController', MacroHousesAccessDoorController);

    MacroHousesAccessDoorController.$inject = ['Auth', '$timeout', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'ParseLinks', 'globalCompany', 'Modal'];

    function MacroHousesAccessDoorController(Auth, $timeout, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, ParseLinks, globalCompany, Modal) {
        var vm = this;
        vm.queryType = 1;
        vm.houseSelected = -1;
        $rootScope.houseSelected = vm.houseSelected;
        vm.condominiumSelected = -1;
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
            $rootScope.condominiumSelected = vm.condominiumSelected;
        };

        vm.changeQueryType = function (type) {
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
        }
        vm.filterInfo = function () {
            vm.isReady = false;
            vm.showingData = true;
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
            loadResidents();
        };

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
                sort: sort(),
                companyId: vm.condominiumSelected.id,
                name: filter,
                houseId: houseId.id,
                owner: "empty",
                enabled: 1,
            }, onSuccessResidents, onError);

            function sort() {
                var result = [];
                if (vm.predicate !== 'name') {
                    result.push('name,asc');
                }
                return result;
            }
        }

        function onSuccessResidents(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            for (var i = 0; i < data.length; i++) {
                vm.residents.push(data[i])
            }
            vm.isReady = true;
        }

        vm.loadPageResidents = function (page) {
            vm.page = page;
            loadResidents();
        };

        // VEHICULOS
        vm.filterVehicules = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.vehicules = [];
            loadVehicules();
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
                sort: sort(),
                companyId: vm.condominiumSelected.id,
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
                console.log(vm.vehicules)
                vm.isReady = true;
            }
        }

        vm.loadPageVehicules = function (page) {
            vm.page = page;
            loadVehicules();
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
