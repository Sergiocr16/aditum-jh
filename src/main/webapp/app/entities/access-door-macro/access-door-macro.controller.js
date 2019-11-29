(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorMacroController', AccessDoorMacroController);

    AccessDoorMacroController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'MacroCondominium', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'WSOfficer', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal', 'Officer', 'CompanyConfiguration'];

    function AccessDoorMacroController($mdToast, $timeout, Auth, $state, $scope, $rootScope, CommonMethods, MacroCondominium, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, WSOfficer, PadronElectoral, Destinies, globalCompany, Modal, Officer, CompanyConfiguration) {
        var vm = this;
        $rootScope.mainTitle = "Puerta de Acceso";
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        vm.loading = false;
        vm.found = false;
        vm.identificationNumber='';
        vm.licensePlate='';
        vm.deleteVehiculeSpots = function () {
            vm.licensePlate = '';
            vm.loading = false;
            vm.found = false;
        };
        vm.deleteResidentSpots = function () {
            vm.identificationNumber = '';
            vm.loading = false;
            vm.found = false;
        };

        vm.findAuthorized = function () {
            vm.loading = false;
            vm.found = false;
            vm.licensePlate = '';
            if (vm.identificationNumber.length >= 9) {
                vm.loading = true;
                MacroCondominium.getAuthorizedInMacroByIdentification({
                        id: globalCompany.getId(),
                        identification: vm.identificationNumber
                    },
                    authorizedFound,
                    nothingFound
                )
            }
        };

        vm.findAuthorizedByPlate = function () {
            vm.loading = false;
            vm.found = false;
            vm.identificationNumber = '';
            if (vm.licensePlate.length > 2) {
                vm.loading = true;
                MacroCondominium.getAuthorizedInMacroByPlate({
                        id: globalCompany.getId(),
                        plate: vm.licensePlate
                    },
                    authorizedFound,
                    nothingFound
                )
            }
        };

        function authorizedFound(authorized) {
         vm.authorized = authorized;
         vm.loading = false;
         vm.found = true;
        }

        function nothingFound(data) {
            vm.loading = false;
            vm.found = false;
        }

    }
})();
