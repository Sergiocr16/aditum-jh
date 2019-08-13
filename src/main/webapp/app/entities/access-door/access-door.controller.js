(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter','globalCompany','Company'];

    function AccessDoorController($mdToast, $timeout, Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter,  globalCompany, Company) {
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
                Company.getAuthorizedInCompanyByIdentification({
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
                Company.getAuthorizedInCompanyByPlate({
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
