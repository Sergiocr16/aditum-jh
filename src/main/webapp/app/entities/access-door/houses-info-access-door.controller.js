(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HousesInfoAccessDoorController', HousesInfoAccessDoorController);

    HousesInfoAccessDoorController.$inject = ['Auth', '$timeout', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitorInvitation', 'ParseLinks', 'globalCompany', 'Modal', 'VisitantInvitation'];

    function HousesInfoAccessDoorController(Auth, $timeout, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitorInvitation, ParseLinks, globalCompany, Modal, VisitantInvitation) {
        var vm = this;

        vm.clearSearchTerm = function () {
            vm.searchTerm = '';
        };
        vm.searchTerm;
        vm.typingSearchTerm = function (ev) {
            ev.stopPropagation();
        }
        $scope.$on("$destroy", function () {
            $rootScope.visitorHouseNotification = undefined;
        });
        $scope.$watch(function () {
            return $rootScope.visitorHouseNotification;
        }, function () {
            if ($rootScope.visitorHouseNotification != undefined) {
                vm.showDataNewVisitorInvited();
            }
        }, true);
        vm.houseSelected = -1;
        vm.queryType = 1;
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
        vm.visitors = [];
        vm.selectHouse = function (house) {
            $rootScope.houseSelected = vm.houseSelected;
            vm.showingData = false;
            if (vm.houseSelected === -1) {
                vm.isReady = true;
                vm.consultingAll = true;
                vm.residents = [];
                vm.vehicules = [];
                $rootScope.visitorHouseNotification = undefined;
                if (vm.queryType == 3) {
                    vm.filterInfo();
                }
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
            if (type !== vm.queryType) {
                vm.queryType = type;
                vm.showingData = false;
                if (vm.queryType == 3) {
                    vm.filterInfo();
                } else {
                    $rootScope.visitorHouseNotification = undefined;
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
        vm.showPhone = function (houseSelected) {
            var phone;
            phone = houseSelected.extension == null ? "No definido" : houseSelected.extension;
            Modal.customDialog("<md-dialog>" +
                "<md-dialog-content class='md-dialog-content text-center'>" +
                "<h1 class='md-title'>Filial <b>" + houseSelected.housenumber + "</b></h1>" +
                "<div class='md-dialog-content-body'>" +
                "<p>Número de teléfono: <b style='font-size: 20px'>" + phone + "</b></p>" +
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
                case 3:
                    vm.filterVisitants();
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
        // VISITANTES
        vm.filterVisitants = function () {
            vm.isReady = false;
            $rootScope.visitorInvited = [];
            if (vm.houseSelected == -1) {
                loadVisitorsByCompany();
            } else {
                loadVisitorsByHouse();
            }
        };

        function loadVisitorsByHouse() {
            VisitantInvitation.getActiveInvitedByHouse({
                houseId: vm.houseSelected.id,
                sort: sortVisitors(),
            }, onSuccessVisitors, onError);
        }

        function loadVisitorsByCompany() {
            VisitantInvitation.getActiveInvitedByCompany({
                sort: sortVisitors(),
                companyId: globalCompany.getId(),
            }, onSuccessVisitors, onError);
        }


        function sortVisitors() {
            var result = [];
            if (vm.predicate !== 'name') {
                result.push('name,asc');
            }
            return result;
        }

        function onSuccessVisitors(data, headers) {
            for (var i = 0; i < data.length; i++) {
                $rootScope.visitorInvited.push(formatVisitantInvited(data[i]))
            }
            vm.isReady = true;
        }

        vm.validateVisitorCed = function (visitor) {
            if (hasCaracterEspecial(visitor.identificationnumber)) {
                visitor.validCed = false;
            } else {
                visitor.validCed = true;
            }
        };
        vm.validateVisitorPlate = function (visitor) {
            if (hasCaracterEspecial(visitor.licenseplate)) {
                visitor.validPlate = false;
            } else {
                visitor.validPlate = true;
            }
        };

        function formatVisitantInvited(itemVisitor) {
            if (itemVisitor.licenseplate == null || itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
                itemVisitor.hasLicense = false;
            } else {
                itemVisitor.hasLicense = true;
            }
            if (itemVisitor.identificationnumber == null || itemVisitor.identificationnumber == undefined || itemVisitor.identificationnumber == "") {
                itemVisitor.hasIdentification = false;
            } else {
                itemVisitor.identificationnumber;
                itemVisitor.hasIdentification = true;
            }
            itemVisitor.validCed = true;
            itemVisitor.validPlate = true;
            itemVisitor.onTime = true;
            return itemVisitor;
            return null;
        }

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

        vm.verifyVisitantInivitedDate = function (visitant) {
            if (visitant.onTime === true && visitant !== undefined) {
                var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
                var initTime = new Date(visitant.invitationstartingtime).getTime();
                var finishTime = new Date(visitant.invitationlimittime).getTime();
                if (initTime <= currentTime && currentTime <= finishTime) {
                    return true;
                } else {
                    visitant.onTime = false;
                    CommonMethods.deleteFromArrayWithId(visitant, vm.visitors);
                    return false;
                }
            } else {
                return false;
                visitant = undefined;
            }
        }
        vm.registerVisitantFromVisitantsList = function (visitant) {
            vm.visitantToInsert = visitant;

            Modal.confirmDialog("¿Está seguro que desea registrar la visita de " + visitant.name + " " + visitant.lastname + "?", "", function () {
                vm.insertingVisitant = 1;
                var temporalLicense;
                Modal.showLoadingBar();
                if (vm.visitantToInsert.licenseplate != undefined || vm.visitantToInsert.licenseplate != null) {
                    temporalLicense = vm.visitantToInsert.licenseplate.toUpperCase();
                }
                var visitante = {
                    name: vm.visitantToInsert.name,
                    lastname: vm.visitantToInsert.lastname,
                    secondlastname: vm.visitantToInsert.secondlastname,
                    identificationnumber: vm.visitantToInsert.identificationnumber.toUpperCase(),
                    licenseplate: temporalLicense,
                    companyId: globalCompany.getId(),
                    isinvited: 3,
                    arrivaltime: moment(new Date()).format(),
                    houseId: vm.visitantToInsert.houseId,
                    responsableofficer: vm.visitantToInsert.destiny
                }
                Visitant.save(visitante, onSaveSuccess, onSaveError);

                function onSaveSuccess(result) {
                    Modal.toastGiant("Se registró la entrada del visitante correctamente.");
                    Modal.hideLoadingBar();
                }

                function onSaveError(error) {
                    Modal.toastGiant("Se registrará la visita una vez la conexión haya vuelto.", "No hay conexión a internet");
                    Modal.hideLoadingBar();
                }
            })
        };

        vm.showDataNewVisitorInvited = function () {
            $rootScope.visitorInvited = [];
            if ($rootScope.visitorHouseNotification == -1 || $rootScope.visitorHouseNotification == undefined ) {
                $rootScope.houseSelected = -1;
                vm.queryType = 3;
            } else {
                for (var i = 0; i < $rootScope.houses.length; i++) {
                    if ($rootScope.visitorHouseNotification == $rootScope.houses[i].id) {
                        $rootScope.houseSelected = $rootScope.houses[i];
                    }
                }
                vm.queryType = 3;
            }
            vm.houseSelected = $rootScope.houseSelected;
            vm.filterInfo();
        }

        // if($rootScope.visitorHouseNotification != undefined ){
        //     vm.showDataNewVisitorInvited();
        // }

    }
})();
