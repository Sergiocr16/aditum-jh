(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'globalCompany', 'Company','Modal'];

    function AccessDoorController($mdToast, $timeout, Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, globalCompany, Company, Modal) {
        var vm = this;
        $rootScope.mainTitle = "Puerta de Acceso";
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        vm.loading = false;
        vm.found = false;
        vm.identificationNumber = '';
        vm.licensePlate = '';
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

        vm.insertVisitor = function () {
            Modal.confirmDialog("¿Está seguro que desea registrar la visita?", "", function () {
                Modal.showLoadingBar();
                var visitor = {
                    name: vm.authorized.name.toUpperCase(),
                    lastname: vm.authorized.lastname.toUpperCase(),
                    secondlastname: vm.authorized.secondlastname.toUpperCase(),
                    identificationnumber: vm.authorized.identificationnumber.toUpperCase(),
                    licenseplate: vm.authorized.licenseplate,
                    companyId: globalCompany.getId(),
                    isinvited: 3,
                    responsableOfficer: vm.authorized.destiny,
                    arrivaltime: moment(new Date()).format(),
                    houseId: vm.authorized.houseId
                };
                Visitant.save(visitor, onSaveSuccess, onSaveError);
            })
        };
        function onSaveSuccess(result) {
            Modal.toastGiant("Se registró la entrada del visitante correctamente.");
            Modal.hideLoadingBar();
            vm.authorized = undefined;
        }

        function onSaveError() {
            Modal.toastGiant("Se registrará la visita una vez la conexión haya vuelto.");
            Modal.hideLoadingBar();
            vm.authorized = undefined;
        }
    }
})();
