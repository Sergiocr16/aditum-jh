(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HousesInfoAccessDoorController1', HousesInfoAccessDoorController1);

    HousesInfoAccessDoorController1.$inject = ['Auth', '$timeout', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal'];

    function HousesInfoAccessDoorController1(Auth, $timeout, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, PadronElectoral, Destinies, globalCompany, Modal) {
        var vm = this;
        vm.residents = [];
        vm.vehicules = [];
        vm.visitorsInvited = [];
        vm.state = 1;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        $rootScope.mainTitle = "Información de filiales";

        $rootScope.$watchGroup(['houses', 'residents', 'vehicules', 'invitedList'], function () {
            if ($rootScope.houses.length > 0) {
                vm.house = $rootScope.houses[0];
                $rootScope.selectHouse("all");
                vm.selectState(1)
            }
        });

        vm.showKeys = function () {
            var emergencyKey, securityKey;
            emergencyKey = vm.house.emergencyKey == null ? "No defninida" : vm.house.emergencyKey;
            securityKey = vm.house.securityKey == null ? "No defninida" : vm.house.securityKey;
            Modal.customDialog("<md-dialog>" +
                "<md-dialog-content class='md-dialog-content text-center'>" +
                "<h1 class='md-title'>Claves filial <b>" + vm.house.housenumber + "</b></h1>" +
                "<div class='md-dialog-content-body'>" +
                "<p>Emergencia: <b style='font-size: 20px'>" + emergencyKey + "</b></p>" +
                "<p>Seguridad: <b style='font-size: 20px'>" + securityKey + "</b></p>" +
                "</div>" +
                "</md-dialog-content>" +
                "</md-dialog>")
        }
        vm.selectState = function (state) {
            vm.state = state;
            vm.query = "";
        };
        $rootScope.selectHouse = function (house) {
            if (house == "all") {
                vm.house = "all";
                vm.residents = $rootScope.residents;
                vm.vehicules = $rootScope.vehicules;
                vm.visitorsInvited = $rootScope.invitedList;
            } else {
                $timeout(function () {
                    var tmpResidents = [];
                    var tmpVehicules = [];
                    var tmpInvitedList = [];
                    for (var i = 0; i < $rootScope.residents.length; i++) {
                        var resident = $rootScope.residents[i];
                        if (resident.houseId == house.id) {
                            tmpResidents.push(resident);
                        }
                    }
                    for (var e = 0; e < $rootScope.vehicules.length; e++) {
                        var vehicule = $rootScope.vehicules[e];
                        if (vehicule.houseId == house.id) {
                            tmpVehicules.push(vehicule);
                        }
                    }
                    for (var j = 0; j < $rootScope.invitedList.length; j++) {
                        var visitantInvited = $rootScope.invitedList[j];
                        if (visitantInvited.houseId == house.id && visitantInvited.onTime == true) {
                            tmpInvitedList.push(visitantInvited);
                        }
                    }
                    vm.residents = tmpResidents;
                    vm.vehicules = tmpVehicules;
                    vm.visitorsInvited = tmpInvitedList;
                    tmpResidents = undefined;
                    tmpVehicules = undefined;
                    tmpInvitedList = undefined;
                }, 2)
            }

        };

        vm.areOnTime = function () {
            var count = 0;
            for (var i = 0; i < vm.visitorsInvited.length; i++) {
                if (vm.visitorsInvited[i].onTime == true) {
                    count++;
                }
            }
            return count;
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

        function hasCaracterEspecial(s) {
            var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i) == val) {
                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                return false;
            } else {
                return true;
            }
        }

        vm.registerVisitantFromVisitantsList = function (visitant) {
            vm.visitantToInsert = visitant;

            Modal.confirmDialog("¿Está seguro que desea registrar la visita de " + visitant.name + " " + visitant.lastname + "?", "", function () {
                vm.insertingVisitant = 1;
                var temporalLicense;
                console.log(vm.visitantToInsert.licenseplate)
                if (vm.visitantToInsert.licenseplate != undefined || vm.visitantToInsert.licenseplate != null) {
                    temporalLicense = vm.visitantToInsert.licenseplate.toUpperCase();
                }
                Modal.showLoadingBar();

                var visitante = {
                    name: vm.visitantToInsert.name,
                    lastname: vm.visitantToInsert.lastname,
                    secondlastname: vm.visitantToInsert.secondlastname,
                    identificationnumber: vm.visitantToInsert.identificationnumber.toUpperCase(),
                    licenseplate: temporalLicense,
                    companyId: globalCompany.getId(),
                    isinvited: 3,
                    arrivaltime: moment(new Date()).format(),
                    houseId: vm.visitantToInsert.houseId
                }
                Visitant.save(visitante, onSaveSuccess, onSaveError);

                function onSaveSuccess(result) {
                    if (visitant.hasLicense == false) {
                        temporalLicense = null;
                    }
                    if (visitant.hasLicense == false || visitant.hasLicense == true) {
                        var visitante2 = {
                            id: vm.visitantToInsert.id,
                            name: vm.visitantToInsert.name,
                            lastname: vm.visitantToInsert.lastname,
                            secondlastname: vm.visitantToInsert.secondlastname,
                            identificationnumber: vm.visitantToInsert.identificationnumber.toUpperCase(),
                            licenseplate: temporalLicense,
                            companyId: globalCompany.getId(),
                            isinvited: 1,
                            invitationstaringtime: vm.visitantToInsert.invitationstaringtime,
                            invitationlimittime: vm.visitantToInsert.invitationlimittime,
                            houseId: vm.visitantToInsert.houseId
                        };
                        Visitant.update(visitante2, onUpdateSuccess, onUpdateError);
                    }
                }

                function onUpdateSuccess(result) {
                    vm.insertingVisitant = 2;
                    Modal.hideLoadingBar();
                    toastr["success"]("Se registró la entrada del visitante correctamente.");

                }

                function onUpdateError(error) {
                    Modal.hideLoadingBar();

                    toastr["error"]("Ocurrio un error registrando la visita");
                }

                function onSaveError(error) {
                    toastr["info"]("Se registrará la visita una vez la conexión haya vuelto.", "No hay conexión a internet");
                    Modal.hideLoadingBar();
                }
            })
        };


        vm.verifyVisitantInivitedDate = function (visitant) {
            if (visitant.onTime === true && visitant !== undefined) {
                var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
                var initTime = new Date(visitant.invitationstaringtime).getTime();
                var finishTime = new Date(visitant.invitationlimittime).getTime();
                if (initTime <= currentTime && currentTime <= finishTime) {
                    return true;
                } else {
                    visitant.onTime = false;
                    CommonMethods.deleteFromArrayWithId(visitant, vm.visitorsInvited)
                    return false;
                }
            } else {
                return false;
                visitant = undefined;
            }
        }
    }
})();
