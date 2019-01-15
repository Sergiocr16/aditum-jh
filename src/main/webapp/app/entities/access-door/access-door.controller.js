(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['$mdToast','$timeout', 'Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal','Officer'];

    function AccessDoorController($mdToast,$timeout, Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, PadronElectoral, Destinies, globalCompany, Modal,Officer) {
        var vm = this;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();

        var residents, vehicules, emergencyList, visitorsList, invitedList;
        $rootScope.houses = [];
        $rootScope.residents = [];
        $rootScope.vehicules = [];
        $rootScope.invitedList = [];
        $rootScope.emergencyList = [];
        $rootScope.officers = [];
        vm.resident = undefined;
        vm.residentFound = 0;
        vm.id_vehicule = '';
        vm.id_number = '';
        vm.emergencyInProgress = false;
        vm.isReady = false;
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-bottom-full-width",
            "preventDuplicates": true,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "slideDown",
            "hideMethod": "fadeOut"
        }
        loadAll();
        loadHouses();
        loadDestinies();
        loadEmergencies();
        loadOfficers();
        subscribe();


        function loadDestinies() {
            Destinies.query(function (destinies) {
                formatDestinies(destinies);
            });

            function formatDestinies(destinies) {
                $rootScope.destinies = [];
                angular.forEach(destinies, function (val, i) {
                    $rootScope.destinies.push({housenumber: val.name, companyId: globalCompany.getId()})
                });
            }
        }
        function loadOfficers() {
            Officer.query({
                companyId: globalCompany.getId()
            }, onSuccessOfficer, onError);

            function onSuccessOfficer(officers, headers) {
                $rootScope.officers = officers;
                console.log($rootScope.officers)
            }
        }
        function loadHouses() {
            House.query({
                companyId: globalCompany.getId()
            }, onSuccessHouse, onError);

            function onSuccessHouse(houses, headers) {
                angular.forEach(houses, function (value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                $rootScope.houses = houses;
            }
        }

        function loadAll() {
            loadResidents();
        }

        function loadResidents() {
            Resident.query({
                companyId: globalCompany.getId()
            }, onSuccessResident, onError);

            function onSuccessResident(residents, headers) {
                $rootScope.residents = residents;
                loadVehicules()
            }
        }

        function loadVehicules() {
            Vehicule.query({
                companyId: globalCompany.getId()
            }, onSuccessVehicule, onError);

            function onSuccessVehicule(vehicules, headers) {
                $rootScope.vehicules = vehicules;
                loadVisitors()
            }
        }

        function loadVisitors() {
            Visitant.query({
                companyId: globalCompany.getId()
            }, onSuccessVisitor, onError);

            function onSuccessVisitor(visitors, headers) {
                $rootScope.visitorsList = visitors;
                loadInvited();
            }
        }

        function loadInvited() {
            Visitant.findAllInvited({
                companyId: globalCompany.getId()
            }, onSuccessInvited, onError);

            function onSuccessInvited(visitors, headers) {
                $rootScope.invitedList = [];
                for (var i = 0; i < visitors.length; i++) {
                    var visitor = formatVisitantInvited(visitors[i]);
                    if (visitor != null && visitor.isinvited == 1) {
                        visitor.onTime = true;
                        $rootScope.invitedList.push(visitor);
                    }
                }
                loadNotes();
            }
        }

        function loadNotes() {
            moment.locale('es');
            Note.findAll({
                companyId: globalCompany.getId()
            }, onSuccessNotes, onError);

            function onSuccessNotes(notes, headers) {
                vm.isReady = true;
                $rootScope.countNotes = notes.length;
                for (var i = 0; i < notes.length; i++) {
                    notes[i].sinceDate = moment(notes[i].creationdate).fromNow();
                }
                $rootScope.notes = notes;
            }
        }

        function loadEmergencies() {
            Emergency.findAll({
                companyId: globalCompany.getId()
            }, onSuccessEmergencies, onError);

            function onSuccessEmergencies(emergencies, headers) {
                if (emergencies.length > 0) {
                    vm.emergency = undefined;
                    console.log(vm.emergency)
                    receiveEmergency(emergencies[0]);
                    $rootScope.emergencyList = emergencies;
                } else {
                    $rootScope.emergencyList = []
                }
            }

            function onError() {
                vm.show = 4;
                vm.hideRegisterForm = 2;
                vm.hideLoadingForm = 1;
                $rootScope.emergencyList = []
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.deleteVehiculeSpots = function () {
            vm.id_vehicule = '';
            vm.residentFound = 0;
        };
        vm.deleteResidentSpots = function () {
            vm.id_number = '';
            vm.residentFound = 0;
        };
        vm.getResident = function () {
            vm.id_vehicule = "";
            vm.resident = undefined;
            vm.vehicule = undefined;
            vm.residentFound = 0;
            vm.visitorFound = 0;
            if (vm.id_number != "" && vm.id_number.length >= 6) {
                for (var i = 0; i < $rootScope.residents.length; i++) {
                    var resident = $rootScope.residents[i];
                    if (resident.identificationnumber.toUpperCase() === vm.id_number.toUpperCase()) {
                        if (resident.enabled == 0) {
                            vm.residentRegisteredTitle = "Residente no habilitado para ingresar";
                            vm.colorResidentRegistered = "material-red-font";
                        } else {
                            vm.residentRegisteredTitle = "Residente habilitado";
                            vm.colorResidentRegistered = "material-green-font";
                        }
                        if (resident.house.housenumber == 9999) {
                            resident.house.housenumber = 'Oficina';
                        }
                        vm.residentFound = 1;
                        vm.resident = resident;
                    }
                }
                if (vm.id_number.length >= 6 && vm.residentFound == 0) {
                    for (var e = 0; e < $rootScope.invitedList.length; e++) {
                        var visitor = $rootScope.invitedList[e];
                        if (visitor.identificationnumber.toUpperCase() == vm.id_number.toUpperCase() && visitor.isinvited == 1) {
                            if (verifyVisitantInivitedDate(visitor)) {
                                vm.resident = visitor;
                                vm.resident.house = {};
                                vm.resident.house.housenumber = visitor.houseNumber;
                                vm.resident.image_url = null;
                                if (visitor.licenseplate == null || visitor.licenseplate == undefined || visitor.licenseplate == "") {
                                    vm.resident.licenseplate = "NINGUNA";
                                } else {
                                    vm.resident.licenseplate = visitor.licenseplate;
                                }
                                vm.visitorFound = 1;
                                vm.residentRegisteredTitle = "Visitante invitado";
                                vm.colorResidentRegistered = "material-green-font";
                            }
                        }
                    }
                }
            }
        };
        vm.getVehicule = function () {
            vm.id_number = "";
            vm.vehicule = undefined;
            vm.resident = undefined;
            vm.vehiculeFound = 0;
            vm.visitorFound = 0;
            if (vm.id_vehicule != '' && vm.id_vehicule.length >= 1) {
                for (var i = 0; i < $rootScope.vehicules.length; i++) {
                    var vehicule = $rootScope.vehicules[i];
                    if (vehicule.licenseplate.toUpperCase() === vm.id_vehicule.toUpperCase()) {
                        if (vehicule.enabled == 0) {
                            vm.vehiculeRegisteredTitle = "Vehículo no habilitado para ingresar";
                            vm.colorVehiculeRegistered = "material-red-font";
                        } else {
                            vm.vehiculeRegisteredTitle = "Vehículo habilitado";
                            vm.colorVehiculeRegistered = "material-green-font";
                        }
                        if (vehicule.house.housenumber == 9999) {
                            vehicule.house.housenumber = 'Oficina';
                        }
                        vm.vehiculeFound = 1;
                        vm.vehicule = vehicule;
                    }
                }
                if (vm.id_vehicule.length >= 1 && vm.vehiculeFound == 0) {
                    for (var e = 0; e < $rootScope.invitedList.length; e++) {
                        var visitor = $rootScope.invitedList[e];

                        if (visitor.licenseplate != null && visitor.licenseplate.toUpperCase() == vm.id_vehicule.toUpperCase() && visitor.isinvited == 1) {
                            if (verifyVisitantInivitedDate(visitor)) {
                                vm.resident = visitor;
                                vm.resident.house = {};
                                vm.resident.house.housenumber = visitor.houseNumber;
                                vm.resident.image_url = null;
                                if (visitor.licenseplate == null || visitor.licenseplate == undefined || visitor.licenseplate == "") {
                                    vm.resident.licenseplate = "NINGUNA";
                                } else {
                                    vm.resident.licenseplate = visitor.licenseplate;
                                }
                                vm.visitorFound = 1;
                                vm.residentRegisteredTitle = "Visitante invitado";
                                vm.colorResidentRegistered = "material-green-font";
                            }
                        }
                    }
                }
            }
        };

        vm.insertVisitantInvited = function () {
            Modal.confirmDialog("¿Está seguro que desea realizar esta acción?", "Registrará la visita de "
                + vm.resident.name + " " + vm.resident.lastname +
                " a la filial " + vm.resident.house.housenumber, function () {
                vm.savingVisitor = true;
                var visitant = {
                    name: vm.resident.name,
                    lastname: vm.resident.lastname,
                    secondlastname: vm.resident.secondlastname,
                    identificationnumber: vm.resident.identificationnumber,
                    licenseplate: vm.resident.licenseplate,
                    companyId: globalCompany.getId(),
                    isinvited: 3,
                    arrivaltime: moment(new Date()).format(),
                    houseId: vm.resident.houseId
                };

                Visitant.save(visitant, onSaveSuccess, onSaveError);
                Modal.showLoadingBar();

                function onSaveSuccess(result) {
                    toastr["success"]("Se registró la entrada del visitante correctamente.")

                    // Modal.toast("Se registró la entrada del visitante correctamente.");
                    clearAll();
                    vm.savingVisitor = false;
                    Modal.hideLoadingBar();
                }

                function onSaveError(result) {
                    toastr["error"]("Ocurrio un error registrando el visitante.")
                    // Modal.toast("Ocurrio un error registrando el visitante.");
                    vm.savingVisitor = false;
                    Modal.hideLoadingBar();
                }
            });
        };

        vm.showKeys = function (data) {
            var emergencyKey, securityKey;
            emergencyKey = data.house.emergencyKey == null ? "No defninida" : data.house.emergencyKey;
            securityKey = data.house.securityKey == null ? "No defninida" : data.house.securityKey;
            Modal.customDialog("<md-dialog>" +
                "<md-dialog-content class='md-dialog-content text-center'>" +
                "<h1 class='md-title'>Claves filial <b>" + data.house.housenumber + "</b></h1>" +
                "<div class='md-dialog-content-body'>" +
                "<p>Emergencia: <b style='font-size: 20px'>" + emergencyKey + "</b></p>" +
                "<p>Seguridad: <b style='font-size: 20px'>" + securityKey + "</b></p>" +
                "</div>" +
                "</md-dialog-content>" +
                "</md-dialog>")
        }

        function clearAll() {
            vm.id_vehicule = '';
            vm.id_number = '';
            vm.visitorFound = 0;
            vm.residentFound = 0;
        }

        function verifyVisitantInivitedDate(visitant) {
            var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
            var initTime = new Date(visitant.invitationstaringtime).getTime();
            var finishTime = new Date(visitant.invitationlimittime).getTime();
            if (initTime <= currentTime && currentTime <= finishTime) {
                return true;
            } else {
                visitant.isinvited = 2;
                Visitant.update(visitant, function () {
                })
                if ($rootScope.invitedList !== undefined) {
                    CommonMethods.deleteFromArray(visitant, $rootScope.invitedList);
                }
                return false;
            }
        }

        function formatVisitantInvited(itemVisitor) {
            if (verifyVisitantInivitedDate(itemVisitor)) {
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
            }
            return null;
        }

        function subscribe() {
            console.log("IGUAL CORRE")
            $timeout(function () {
                WSEmergency.subscribe(globalCompany.getId());
                WSHouse.subscribe(globalCompany.getId());
                WSResident.subscribe(globalCompany.getId());
                WSVehicle.subscribe(globalCompany.getId());
                WSNote.subscribe(globalCompany.getId());
                WSVisitor.subscribe(globalCompany.getId());
                WSDeleteEntity.subscribe(globalCompany.getId());
                WSDeleteEntity.receive().then(null, null, receiveDeletedEntity);
                WSEmergency.receive().then(null, null, receiveEmergency);
                WSHouse.receive().then(null, null, receiveHouse);
                WSResident.receive().then(null, null, receiveResident);
                WSVehicle.receive().then(null, null, receiveVehicle);
                WSNote.receive().then(null, null, receiveHomeService);
                WSVisitor.receive().then(null, null, receiveVisitor);
            }, 3000);
        }


        function hasExistance(array, id) {
            var index = undefined;
            var founded = false;
            angular.forEach(array, function (item, i) {
                if (parseInt(item.id) === parseInt(id)) {
                    index = i;
                    founded = true;
                } else {
                    if (founded == false) {
                        index = -1;
                    }
                }
            })
            return index;
        };

        function existItem(array, id) {
            var index = undefined;
            var founded = false;
            angular.forEach(array, function (item, i) {
                if (parseInt(item.id) === parseInt(id)) {
                    index = item;
                    founded = true;
                } else {
                    if (founded == false) {
                        index = undefined;
                    }
                }
            })
            return index;
        };

        function receiveResident(resident) {
            if ($rootScope.residents !== undefined) {
                var result = hasExistance($rootScope.residents, resident.id)
                if (result != -1) {
                    $rootScope.residents[result] = resident;
                } else {
                    $rootScope.residents.push(resident);
                }
            }
            if ($rootScope.houses.length > 0) {
                $rootScope.selectHouse("all");
            }
        }

        function receiveVehicle(vehicle) {
            if ($rootScope.vehicules !== undefined) {
                var result = hasExistance($rootScope.vehicules, vehicle.id)
                if (result != -1) {
                    $rootScope.vehicules[result] = vehicle;
                } else {
                    $rootScope.vehicules.push(vehicle);
                }
            }
            if ($rootScope.houses.length > 0) {
                $rootScope.selectHouse("all");
            }
        }

        function receiveHouse(house) {
            loadAll();
            loadHouses();
            if ($rootScope.houses.length > 0) {
                $rootScope.selectHouse("all");
            }
        }

        function receiveVisitor(visitor) {
            var visitor = formatVisitantInvited(visitor);

            if ($rootScope.invitedList !== undefined) {
                if (visitor.isinvited == 1) {
                    toastr["info"]("¡Se ha invitado un visitante en la filial " + visitor.houseNumber + "!")
                }
                var result = hasExistance($rootScope.invitedList, visitor.id);
                if (result != -1 && result != undefined) {
                    if (visitor.isinvited == 1 && $rootScope.invitedList.length != 0) {
                        $rootScope.invitedList[result] = visitor;
                    } else {
                        CommonMethods.deleteFromArrayWithId(visitor, $rootScope.invitedList)
                    }
                } else {
                    $rootScope.invitedList.push(visitor);
                }
            } else {
                CommonMethods.deleteFromArrayWithId(visitor, $rootScope.invitedList)
            }
            if ($rootScope.houses.length > 0) {
                $rootScope.selectHouse("all");
            }
            console.log($rootScope.invitedList)
        }

        function receiveHomeService(note) {
            if ($rootScope.notes !== undefined) {
                note.sinceDate = moment(note.creationdate).fromNow();
                $rootScope.notes.push(note);
                toastr["info"]("¡Se ha recibido una nueva nota de la filial " + note.house.housenumber + "!")
                vm.countNotes = $rootScope.notes.length;
            }
        }

        $rootScope.selectHouse = function (house) {
        }

        function receiveDeletedEntity(entity) {
            switch (entity.type) {
                case 'resident':
                    var result = existItem($rootScope.residents, entity.id)
                    if (result !== undefined) {
                        CommonMethods.deleteFromArray(result, $rootScope.residents)
                    }
                    break;
                case 'vehicle':
                    var result = existItem($rootScope.vehicules, entity.id)
                    if (result !== undefined) {
                        CommonMethods.deleteFromArray(result, $rootScope.vehicules)
                    }
                    break;
                case 'visitor':
                    var result = existItem($rootScope.invitedList, entity.id)
                    if (result !== undefined) {
                        CommonMethods.deleteFromArray(result, $rootScope.invitedList)
                    }
                    break;
            }
        }

        function receiveEmergency(emergency) {
            if (emergency.isAttended == 0) {
                vm.emergency = emergency;
                vm.emergencyInProgress = true;
                $rootScope.emergencyList.push(emergency)
            }
        }

        vm.attendEmergency = function () {
            var codeEmegency = globalCompany.getId() + "" + vm.emergency.houseId;
            vm.emergency.isAttended = 1;
            toastr["success"]("Se ha reportado al residente que se atenderá la emergencia");
            Emergency.update(vm.emergency, function (result) {
                WSEmergency.sendActivityAttended(codeEmegency, vm.emergency);
                $timeout(function () {
                    vm.emergency = undefined;
                    vm.emergencyInProgress = false;
                    console.log(vm.emergency)
                    loadEmergencies();
                }, 5)

            })
        };

        var delay = 1000;
       $rootScope.online = true;
        var toastOffline;
        function unsubscribe() {
            WSDeleteEntity.unsubscribe(globalCompany.getId());
            WSEmergency.unsubscribe(globalCompany.getId());
            WSHouse.unsubscribe(globalCompany.getId());
            WSResident.unsubscribe(globalCompany.getId());
            WSVehicle.unsubscribe(globalCompany.getId());
            WSNote.unsubscribe(globalCompany.getId());
            WSVisitor.unsubscribe(globalCompany.getId());
        }
        Offline.on('confirmed-down', function () {
            if($rootScope.online){
                 toastOffline = $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo perdió conexión a internet.")
                        .hideDelay(0)
                        .position("top center")
                );
                $rootScope.online = false;
            }
        });

        Offline.on('confirmed-up', function () {
            if(!$rootScope.online){
                $mdToast.hide();
                $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo está conectado a internet nuevamente.")
                        .position("top center")
                );
                $rootScope.online = true;
                loadAll();
                loadHouses();
                loadDestinies();
                loadEmergencies();
                loadOfficers();
                unsubscribe();
                subscribe();
            }
        });

        setTimeout(function retry() {
            Offline.check();
            setTimeout(retry, delay);
        }, delay);
    }
})();
