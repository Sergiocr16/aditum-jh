(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorMacroController', AccessDoorMacroController);

    AccessDoorMacroController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'WSOfficer', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal', 'Officer', 'CompanyConfiguration'];

    function AccessDoorMacroController($mdToast, $timeout, Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, WSOfficer, PadronElectoral, Destinies, globalCompany, Modal, Officer, CompanyConfiguration) {
        var vm = this;
        $rootScope.mainTitle = "Puerta de Acceso";

    }
})();
