(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'PadronElectoral','Destinies','ngNotify'];

    function AccessDoorController(Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, PadronElectoral,Destinies,ngNotify) {
        var vm = this;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();

        CommonMethods.validateSpecialCharactersAndVocals();

        vm.showLock = false;

        var residentsList, vehiculesList, housesList, emergencyList, visitorsList, invitedList;
        vm.destityTitle = "Número de casa:";
        vm.destityPlaceHolder = "Seleccione una casa";
        vm.radiostatus = true;
        vm.consultingPadron = false;
        vm.founded=false;

        vm.loadDestinies = function(){
        vm.destinies = Destinies.query(function(destinies){
          vm.formatDestinies(destinies);
        });
         vm.formatDestinies = function(destinies){
        vm.maintenance = [];
         angular.forEach(destinies,function(val,i){
         vm.maintenance.push({housenumber:val.name,companyId:$rootScope.companyId})

         })
         }
         }
//         [{
//                housenumber: "Mantenimiento de piscina",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Mantenimiento de instalaciones",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Áreas verdes",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Planta de tratamiento",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Seguridad",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Reciclaje",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Vendedores",
//                companyId: $rootScope.companyId
//            },
//            {
//                housenumber: "Autorizado por administración",
//                companyId: $rootScope.companyId
//            },
//        ];

        vm.changeDestinoCasa = function() {
            vm.radiostatus = true;
            vm.destityTitle = "Número de casa:";
            vm.destityPlaceHolder = "Seleccione una casa";
            vm.housesToShow = vm.houses;
            $("#radio_14").prop("checked", "checked")
        }
        vm.unlocklock = function() {
//            vm.iconLock = "fa fa-unlock";
//            vm.textLock = "Bloquear";
            vm.found = false;
            vm.showLockCed = false;
        }
        vm.changeDestinoProveedor = function() {
            vm.destityTitle = "Destino:";
            vm.destityPlaceHolder = "Seleccione un destino";
            vm.housesToShow = vm.maintenance;
            $("#radio_14").prop("checked", "checked")
        }
        var securityKey, emergencyKey, housenumber;
        vm.hideEmergencyForm = 1;
        vm.hideLoadingForm = 2;
        vm.hideRegisterForm = 1;
        vm.show = 20;
        vm.logout = logout;

        vm.isAuthenticated = Principal.isAuthenticated;

        vm.reloadAll = function() {
            vm.hideEmergencyForm = 1;
            vm.hideLoadingForm = 2;
            vm.hideRegisterForm = 1;
            vm.show = 20;
            loadResidents();

        }

        function logout() {

            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.showLogin = true;
            unsubscribe();
        console.log(ngNotify)
                if(ngNotify!=null){
                  ngNotify.dismiss()
                }
        }

        setTimeout(function() {

            Principal.identity().then(function(account) {
                if (account != undefined) {
                    if (account.authorities[0] == "ROLE_OFFICER") {
                        loadResidents();
                    }
                }
            })

        }, 800)

        function unsubscribe() {
            WSDeleteEntity.unsubscribe($rootScope.companyId);
            WSEmergency.unsubscribe($rootScope.companyId);
            WSHouse.unsubscribe($rootScope.companyId);
            WSResident.unsubscribe($rootScope.companyId);
            WSVehicle.unsubscribe($rootScope.companyId);
            WSNote.unsubscribe($rootScope.companyId);
            WSVisitor.unsubscribe($rootScope.companyId);
        }

        function loadResidents() {
            Resident.query({
                companyId: $rootScope.companyId
            }, onSuccessResident, onError);

            function onSuccessResident(residents, headers) {
                residentsList = residents;
                loadHouses();
                vm.loadDestinies();
            }
        }

        function loadHouses() {
            House.query({
                companyId: $rootScope.companyId
            }, onSuccessHouse, onError);

            function onSuccessHouse(houses, headers) {
                angular.forEach(houses, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                housesList = houses;
                vm.housesToShow = houses;
                loadVehicules()
            }

        }

        function loadVehicules() {
            Vehicule.query({
                companyId: $rootScope.companyId
            }, onSuccessVehicule, onError);

            function onSuccessVehicule(vehicules, headers) {
                vehiculesList = vehicules;
                loadVisitors()
            }
        }

        function loadVisitors() {
            Visitant.query({
                companyId: $rootScope.companyId
            }, onSuccessVisitor, onError);

            function onSuccessVisitor(visitors, headers) {
                visitorsList = visitors;
                subscribe();
                loadInvited();
            }
        }

        function loadInvited() {
            Visitant.findAllInvited({
                companyId: $rootScope.companyId
            }, onSuccessInvited, onError);

            function onSuccessInvited(visitors, headers) {
                invitedList = visitors;
                subscribe();
                loadNotes();
            }
        }

        function loadNotes() {
            moment.locale('es');
            Note.findAll({
                companyId: $rootScope.companyId
            }, onSuccessNotes, onError);

            function onSuccessNotes(notes, headers) {
                angular.forEach(notes, function(key, note) {
                    key.sinceDate = moment(key.creationdate).fromNow();
                })

                angular.forEach(notes, function(note, index) {
                    angular.forEach(housesList, function(house, index) {
                        if (house.id == note.houseId) {
                            note.housenumber = house.housenumber;
                        }
                    })
                })
                vm.notes = notes;
                vm.countNotes = vm.notes.length;
                subscribe();
                loadEmergencies();
            }
        }

        function loadEmergencies() {
            Emergency.findAll({
                companyId: $rootScope.companyId
            }, onSuccessNotes, onError);

            function onSuccessNotes(emergencies, headers) {
                if (emergencies.length > 0) {
                    vm.emergency = undefined;
                    receiveEmergency(emergencies[0]);
                    emergencyList = emergencies;
                } else {
                    vm.show = 4;
                    vm.hideRegisterForm = 2;
                    vm.hideLoadingForm = 1;
                    emergencyList = []
                }

                subscribe();
            }

            function onError() {
                vm.show = 4;
                vm.hideRegisterForm = 2;
                vm.hideLoadingForm = 1;
                emergencyList = []
                subscribe();
            }
        }

        function subscribe() {
            if ($state.current.name === "main-access-door") {
                WSDeleteEntity.subscribe($rootScope.companyId);
                WSEmergency.subscribe($rootScope.companyId);
                WSHouse.subscribe($rootScope.companyId);
                WSResident.subscribe($rootScope.companyId);
                WSVehicle.subscribe($rootScope.companyId);
                WSNote.subscribe($rootScope.companyId);
                WSVisitor.subscribe($rootScope.companyId);
                WSDeleteEntity.receive().then(null, null, receiveDeletedEntity);
                WSEmergency.receive().then(null, null, receiveEmergency);
                WSHouse.receive().then(null, null, receiveHouse);
                WSResident.receive().then(null, null, receiveResident);
                WSVehicle.receive().then(null, null, receiveVehicle);
                WSNote.receive().then(null, null, receiveHomeService);
                WSVisitor.receive().then(null, null, receiveVisitor);
            }

        }

        function onError(error) {
            AlertService.error(error.data.message);
        }


        vm.deleteDomicilioReports = function(note) {
            Note.delete({
                id: note.id
            }, onSuccessDelete);
        }

        function onSuccessDelete(result) {
            toastr["success"]("Eliminado");
            loadNotes();
            WSDeleteEntity.sendDeletedEntity({
                type: 'resident',
                id: result.id
            })
        }
        vm.getVehicule = function() {
            vm.id_number = "";
            $("#id_license_number").css("text-transform", "none");
            $("#id_license_number").attr("placeholder", "Cédula");
            if (vm.id_vehicule == "") {
                $("#vehicule_license_plate").css("text-transform", "none");
                $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                $("#id_license_number").attr("placeholder", "Cédula");
                vm.show = 4;
            } else {
                $("#vehicule_license_plate").css("text-transform", "uppercase");
                vm.show = 3;
                 vm.searchTextPadron = "Consultando en visitas anteriores";
                 vm.showLock = false;
                 vm.consultingPadron = true;
                angular.forEach(vehiculesList, function(item, index) {
                    if (item.licenseplate.toUpperCase() == vm.id_vehicule.toUpperCase()) {
                        if (item.type == 'Automóvil') {
                            vm.vehiculeRegisteredTitle = "Automóvil registrado";
                        } else {
                            vm.vehiculeRegisteredTitle = "Motocicleta registrada";
                        }

                        vm.colorVehiculeRegistered = "green-font";
                        vm.imageVehiculeState = "success-car-image";
                        $("#vehiculeAccess").fadeIn(100);
                        vm.show = 2;
                        vm.vehicule = item;
                        var house = vm.setHouse(item.houseId);
                        vm.SelectedHouse = house.housenumber;
                        housenumber = house.housenumber;
                        securityKey = house.securityKey;
                        emergencyKey = house.emergencyKey;
                        if (house.housenumber == 9999) {
                            vm.SelectedHouse = 'Oficina';
                            housenumber = 'Oficina';
                        }
                        if (item.enabled == 0) {
                            vm.vehiculeRegisteredTitle = "Vehículo no habilitado para ingresar";
                            vm.colorVehiculeRegistered = "red-font";
                            vm.imageVehiculeState = "disabled-car-image";
                        }
                    }
                });
                if (vm.id_vehicule.length >= 2 && vm.show !== 2) {

                    angular.forEach(invitedList, function(itemVisitor, index) {

                        if (itemVisitor.licenseplate != null || itemVisitor.licenseplate != undefined) {
                            if (itemVisitor.licenseplate.toUpperCase() == vm.id_vehicule.toUpperCase() && itemVisitor.isinvited == 1) {
                                if (vm.verifyVisitantInivitedDate(itemVisitor)) {
                                    vm.invited_visitant_name = itemVisitor.name;
                                    vm.invited_visitant_last_name = itemVisitor.lastname;
                                    vm.invited_visitant_second_last_name = itemVisitor.secondlastname;
                                    if (itemVisitor.licenseplate == null || itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
                                        vm.invited_visitant_license_plate = "Ninguna";

                                    } else {
                                        vm.invited_visitant_license_plate = itemVisitor.licenseplate;
                                    }
                                    vm.invited_visitant_indentification = itemVisitor.identificationnumber;
                                    var house = vm.setHouse(itemVisitor.houseId);
                                    vm.invited_visitant_house_number = house.housenumber;
                                    vm.show = 10;
                                    $("#visitantInvitedtAccess").fadeIn(100);
                                }
                            }
                        }
                    });

                }
            }

        }


       function findHouse(id) {
            angular.forEach(visitorsList, function(itemVisitor, index) {
                if (itemVisitor.identificationnumber == id && itemVisitor.isinvited == 3) {

                if(itemVisitor.licenseplate!=undefined){
                    vm.visitor_license_plate = itemVisitor.licenseplate;
                    }
                    setHouse(itemVisitor.houseId);
                }
            });
        }
        vm.getVisitor = function() {
         vm.founded = false;
         vm.showLock = false;
            if (vm.visitor_id_number == "" || vm.visitor_licenseplate == "") {
                clearInputs();
            } else {
            if(vm.visitor_id_number!=undefined){
             if(vm.visitor_id_number.length>=9){
                vm.searchTextPadron = "Consultando en padrón electoral";
                vm.showLock = false;
                vm.consultingPadron = true;
                PadronElectoral.find(vm.visitor_id_number, personFinded, personNotFinded)
                function personFinded(itemVisitor) {
                    setTimeout(function() {
                        $scope.$apply(function() {
                            vm.showLock = true;
                            vm.visitor_name = itemVisitor.nombre.split(",")[0] + "";
                            vm.visitor_last_name = itemVisitor.nombre.split(",")[1] + "";
                            vm.visitor_second_last_name = itemVisitor.nombre.split(",")[2] + "";
                            vm.founded = true;
                            findHouse(vm.visitor_id_number)
                            vm.consultingPadron = false;
                            vm.showLockCed = true;
                        });
                    }, 200);
                }

                function personNotFinded() {
                    vm.encontrado = 0;
                    setTimeout(function() {
                        $scope.$apply(function() {
                            vm.searchTextPadron = "Consultando en visitas anteriores";
                            setTimeout(function() {
                                $scope.$apply(function() {
                                    angular.forEach(visitorsList, function(itemVisitor, index) {
                                        if (itemVisitor.identificationnumber == vm.visitor_id_number && itemVisitor.isinvited == 3) {
                                            vm.visitor_name = itemVisitor.name;
                                            vm.visitor_last_name = itemVisitor.lastname;
                                            vm.visitor_second_last_name = itemVisitor.secondlastname
                                            vm.visitor_license_plate = itemVisitor.licenseplate;
                                            setHouse(itemVisitor.identificationnumber);
                                            vm.encontrado = vm.encontrado +1;
                                        }
                                    });

                                    if (vm.encontrado > 0) {
                                        vm.consultingPadron = false;
                                        vm.founded = true;
                                        vm.showLock = true;
                                    } else {
                                        toastr["error"]("Los datos del visitante no se han encontrado, por favor ingresarlos manualmente")
                                        vm.consultingPadron = false;
                                        var id = vm.visitor_id_number;
                                        clearInputs();
                                        vm.visitor_id_number = id;
                                         vm.founded = false;
                                        vm.showLock = false;
                                    }
                                });
                            }, 500)
                        });
                    }, 1);
                }
            }
            }
          }
        }


        vm.getKeyInformation = function() {
            var existe = 0;
            angular.forEach(housesList, function(item, index) {
                if (item.securityKey == vm.security_key) {
                    existe = 1;
                    vm.show = 9;
                    vm.emergencySecurityKeyTitle = "Clave de seguridad";
                    vm.emergency_security_key = item.emergencyKey;
                    vm.key_house_number = item.housenumber;
                    var houseId = item.id;
                    Resident.findResidentesEnabledByHouseId({
                        houseId: houseId
                    }).$promise.then(onSuccessResidents, onError);
                } else if (item.emergencyKey == vm.security_key) {
                    existe = 1;
                    vm.show = 9;
                    vm.emergencySecurityKeyTitle = "Clave de emergencia";
                    vm.emergency_security_key = item.securityKey;
                    var houseId = item.id;
                    Resident.findResidentesEnabledByHouseId({
                        houseId: houseId
                    }).$promise.then(onSuccessResidents, onError);

                }

                function onSuccessResidents(data) {
                    vm.residents = data;
                    Vehicule.findVehiculesEnabledByHouseId({
                        houseId: item.id
                    }).$promise.then(onSuccessVehicules, onError);
                }

                function onSuccessVehicules(data) {
                    vm.vehicules = data;
                }

            });

            if (existe == 0) {
                toastr["error"]("No existe la clave de seguridad o emergencia que ingresó");
            }

        }

        vm.reportarTurno = function(){
         $state.go("main-access-door.newWatch", null,{ reload: false, notify: false })
        }
        vm.getInvitedVisitantsByHouse = function() {
//            vm.show = 13;
//            loadAll();
//            var visitantByHouseList = [];
//            vm.loadingVisitantByHouseIndex = 1
            var encryptedId = CommonMethods.encryptIdUrl(vm.houseForVisitantsInformation.id)
            $state.go("access-door-invited-by-house",{id:encryptedId}, { reload: false, notify: false })
//            function loadAll() {
//                Visitant.findInvitedByHouse({
//                    companyId: $rootScope.companyId,
//                    houseId: vm.houseForVisitantsInformation.id
//                }).$promise.then(onSuccess);
//
//
//                function onSuccess(data) {
//                    $("#loandingVisitantByHouseIndex").fadeOut(0);
//                    $("#visitantByHouseIndex").fadeIn(400);
//
//                    var visitantByHouseList = [];
//                    angular.forEach(data, function(itemVisitor, key) {
//                        if (itemVisitor.isinvited == 1) {
//                            var visitantInvited = {}
//                            if (vm.verifyVisitantInivitedDate(itemVisitor)) {
//                                visitantInvited.id = itemVisitor.id;
//                                visitantInvited.name = itemVisitor.name;
//                                visitantInvited.last_name = itemVisitor.lastname;
//                                visitantInvited.second_last_name = itemVisitor.secondlastname;
//                                visitantInvited.invitation_staring_time = itemVisitor.invitationstaringtime;
//                                visitantInvited.invitation_limit_time = itemVisitor.invitationlimittime;
//                                if (itemVisitor.licenseplate == null || itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
//
//                                    visitantInvited.hasLicense = false;
//
//                                } else {
//                                    visitantInvited.license_plate = itemVisitor.licenseplate;
//                                    visitantInvited.hasLicense = true;
//                                }
//                                if (itemVisitor.identificationnumber == null || itemVisitor.identificationnumber == undefined || itemVisitor.identificationnumber == "") {
//
//
//                                    visitantInvited.hasIdentification = false;
//
//                                } else {
//                                    visitantInvited.indentification = itemVisitor.identificationnumber;
//                                    visitantInvited.hasIdentification = true;
//                                }
//
//                                var house = vm.setHouse(itemVisitor.houseId);
//
//                                vm.visitantListHouse = house;
//                                if (house.housenumber == 9999) {
//                                    vm.visitantListHouse = "Oficina";
//                                }
//                                visitantInvited.house_number = house.housenumber;
//                                visitantByHouseList.push(visitantInvited);
//
//                            }
//
//                        }
//
//                    })
//
//                    vm.visitantByHouseList = visitantByHouseList;
//
//                }
//
//                function onError(error) {
//                    AlertService.error(error.data.message);
//                }
//            }

        }

        vm.quitRedInput = function(visitant) {

            $("#" + visitant.id).css({
                "border-color": "#E0E0E0",
                "border-weight": "1px",
                "border-style": "solid"
            });

        }

        vm.registerVisitantFromVisitantsList = function(visitant) {



            if (visitant.indentification == "" && visitant.hasIdentification == false || visitant.indentification == null && visitant.hasIdentification == false || visitant.indentification == undefined && visitant.hasIdentification == false) {
                toastr["error"]("Debe ingresar el número de cédula para registrar la visita.");
                $("#" + visitant.id).css({
                    "border-color": "red",
                    "border-weight": "2px",
                    "border-style": "solid"
                });
            } else {



                vm.visitantToInsert = visitant;
                bootbox.confirm({
                    message: "¿Está seguro que desea registrar la visita de " + visitant.name + " " + visitant.last_name + "?",
                    buttons: {
                        confirm: {
                            label: 'Aceptar',
                            className: 'btn-success'
                        },
                        cancel: {
                            label: 'Cancelar',
                            className: 'btn-danger'
                        }
                    },
                    callback: function(result) {

                        if (result) {
                            vm.insertingVisitant = 1;
                            var temporalLicense;

                            if (vm.visitantToInsert.license_plate !== undefined) {
                                temporalLicense = vm.visitantToInsert.license_plate.toUpperCase();
                            }
                            var visitante = {
                                name: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.name),
                                lastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.last_name),
                                secondlastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.second_last_name),
                                identificationnumber: vm.visitantToInsert.indentification.toUpperCase(),
                                licenseplate: temporalLicense,
                                companyId: $rootScope.companyId,
                                isinvited: 3,
                                arrivaltime: moment(new Date()).format(),
                                houseId: vm.visitantListHouse.id
                            }

                            Visitant.save(visitante, onSaveSuccess, onSaveError);

                            function onSaveSuccess(result) {
                                if (visitant.hasLicense == false) {
                                    temporalLicense = null;
                                }
                                if (visitant.hasLicense == false || visitant.hasLicense == true) {

                                    var visitante2 = {
                                        id: vm.visitantToInsert.id,
                                        name: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.name),
                                        lastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.last_name),
                                        secondlastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.second_last_name),
                                        identificationnumber: vm.visitantToInsert.indentification.toUpperCase(),
                                        licenseplate: temporalLicense,
                                        companyId: $rootScope.companyId,
                                        isinvited: 1,
                                        invitationstaringtime: vm.visitantToInsert.invitation_staring_time,
                                        invitationlimittime: vm.visitantToInsert.invitation_limit_time,
                                        houseId: vm.visitantListHouse.id
                                    }
                                    visitorsList.push(result);
                                    Visitant.update(visitante2, onUpdateSuccess, onUpdateError);
                                }

                            }

                            function onUpdateSuccess(result) {
                                vm.insertingVisitant = 2;
                                toastr["success"]("Se registró la entrada del visitante correctamente.");
                                vm.getInvitedVisitantsByHouse();
                            }

                            function onUpdateError(error) {
                                AlertService.error(error.data.message);
                            }
                        }
                    }
                });

            }

        }


        vm.getHouseInformation = function() {

            Resident.findResidentesEnabledByHouseId({
                houseId: vm.houseForInformation.id
            }).$promise.then(onSuccessResidents, onError);

            function onSuccessResidents(data) {
                vm.residents = data;
                vm.show = 12;

                Vehicule.findVehiculesEnabledByHouseId({
                    houseId: vm.houseForInformation.id
                }).$promise.then(onSuccessVehicules, onError);
            }

            function onSuccessVehicules(data) {
                vm.vehicules = data;
                House.get({
                    id: vm.houseForInformation.id
                }).$promise.then(onSuccessHouse, onError);
            }

            function onSuccessHouse(data) {

                vm.houseInformationTitle = "Información de la casa" + " " + data.housenumber;
                if (data.housenumber == 9999) {
                    vm.houseInformationTitle = "Información de la casa" + " oficina";
                }
                vm.house = data;
                if (data.securityKey == undefined) {
                    vm.houseInfoSecurityKey = "No tiene";
                } else {
                    vm.houseInfoSecurityKey = data.securityKey;
                }
                if (data.emergencyKey == undefined) {
                    vm.houseInfoEmergencyKey = "No tiene";
                } else {
                    vm.houseInfoEmergencyKey = data.emergencyKey;
                }
                if (data.extension == null) {
                    vm.houseInfoExtension = "No tiene";
                } else {
                    vm.houseInfoExtension = data.extension;
                }


            }


        }
        vm.verifyVisitantInivitedDate = function(visitant) {
            var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
            var initTime = new Date(visitant.invitationstaringtime).getTime();
            var finishTime = new Date(visitant.invitationlimittime).getTime();


            if (initTime <= currentTime && currentTime <= finishTime) {
                return true;
            } else {
                visitant.isinvited = 2;
                Visitant.update(visitant, function() {})
                if (visitorsList !== undefined) {
                    var result = hasExistance(visitorsList, visitant.id)
                    if (result !== -1) {
                        visitorsList[result] = {};
                    }
                }
                return false;
            }
        }
        vm.getResident = function() {
            vm.id_vehicule = "";
            $("#vehicule_license_plate").css("text-transform", "none");
            $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
            $("#id_license_number").attr("placeholder", "Cédula");

            if (vm.id_number == "") {
                $("#id_license_number").css("text-transform", "none");
                $("#id_license_number").attr("placeholder", "Cédula");
                vm.show = 4;
            } else {
                vm.show = 3;
                $("#id_license_number").css("text-transform", "uppercase");
                angular.forEach(residentsList, function(item, index) {

                    if (item.identificationnumber.toUpperCase() == vm.id_number.toUpperCase()) {

                        vm.residentRegisteredTitle = "Residente registrado"
                        vm.colorResidentRegistered = "green-font"
                        $("#residentAccess").fadeIn(100);
                        vm.show = 1;
                        vm.resident = item;
                        var house = vm.setHouse(item.houseId);
                        housenumber = house.housenumber;
                        vm.housenumber = house.housenumber;
                        securityKey = house.securityKey;
                        emergencyKey = house.emergencyKey;
                        if (house.housenumber == 9999) {
                            vm.housenumber = 'Oficina';
                            housenumber = 'Oficina';
                        }
                        if (item.enabled == 0) {
                            vm.residentRegisteredTitle = "Residente no habilitado para ingresar";
                            vm.colorResidentRegistered = "red-font";
                        }
                    }
                });

                if (vm.id_number.length >= 6) {
                    angular.forEach(invitedList, function(itemVisitor, index) {
                        if (itemVisitor.identificationnumber.toUpperCase() == vm.id_number.toUpperCase() && itemVisitor.isinvited == 1) {
                            if (vm.verifyVisitantInivitedDate(itemVisitor)) {
                                vm.invited_visitant_name = itemVisitor.name;
                                vm.invited_visitant_last_name = itemVisitor.lastname;
                                vm.invited_visitant_second_last_name = itemVisitor.secondlastname;
                                if (itemVisitor.licenseplate == null || itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
                                    vm.invited_visitant_license_plate = "Ninguna";

                                } else {
                                    vm.invited_visitant_license_plate = itemVisitor.licenseplate;
                                }
                                vm.invited_visitant_indentification = itemVisitor.identificationnumber;
                                var house = vm.setHouse(itemVisitor.houseId);
                                vm.invited_visitant_house_number = house.housenumber;
                                vm.show = 10;
                                $("#visitantInvitedtAccess").fadeIn(100);
                            }
                        }
                    });

                }
            }
        };

        vm.insert_visitant_invited = function() {
            var idHouse;
            angular.forEach(housesList, function(house, index) {

                if (house.housenumber == vm.invited_visitant_house_number) {
                    idHouse = house.id;
                }
            })

            var visitant = {
                name: CommonMethods.capitalizeFirstLetter(vm.invited_visitant_name),
                lastname: CommonMethods.capitalizeFirstLetter(vm.invited_visitant_last_name),
                secondlastname: CommonMethods.capitalizeFirstLetter(vm.invited_visitant_second_last_name),
                identificationnumber: vm.invited_visitant_indentification.toUpperCase(),
                licenseplate: vm.invited_visitant_license_plate.toUpperCase(),
                companyId: $rootScope.companyId,
                isinvited: 3,
                arrivaltime: moment(new Date()).format(),
                houseId: idHouse
            }

            Visitant.save(visitant, onSaveSuccess, onSaveError);

            function onSaveSuccess(result) {

                vm.id_number = "";
                vm.id_vehicule = "";
                $("#id_license_number").css("text-transform", "none");
                $("#id_license_number").attr("placeholder", "Cédula");
                $("#vehicule_license_plate").css("text-transform", "none");
                $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");

                vm.show = 4;
                vm.isSaving = false;
                visitorsList.push(result);
                toastr["success"]("Se registró la entrada del visitante correctamente.");
            }

        }
        vm.setHouse = function(id) {
            var house;
            angular.forEach(housesList, function(itemHouse, index) {
                if (itemHouse.id == id) {
                    house = itemHouse;
                }

            });
            return house;
        }
                vm.getVisitorByPlateNgChange = function() {
                    vm.visitorShowing = 0;
                    vm.visitorSelected = {}
                    vm.visitorsConsultedByPlate = [];
                     vm.searchTextPadron = "Consultando en visitas anteriores";
                     vm.showLock = false;
                    var encontrado = 0;
                    if(vm.founded==false){
                    if (vm.visitor_license_plate != undefined && vm.visitor_license_plate != "") {

                        vm.visitorsConsultedByPlate = [];
                        angular.forEach(visitorsList, function(itemVisitor, index) {
                            if (itemVisitor.licenseplate == vm.visitor_license_plate.toUpperCase() && itemVisitor.isinvited == 3) {
                                var existe = 0;
                                encontrado = encontrado + 1;
                                angular.forEach(vm.visitorsConsultedByPlate, function(visitor, index) {
                                    if (visitor.identificationnumber == itemVisitor.identificationnumber) {
                                        existe = existe +1;
                                    }
                                })
                                if (existe == 0) {
                                    vm.visitorsConsultedByPlate.push(itemVisitor)
                                }
                            }
                        });


                        if(encontrado>0){
                          vm.consultingPadron = false;
                                            vm.founded = true;
                                            vm.showLock = true;
                                            vm.showLockCed = true;
                                            vm.showLockCed = true;
                        }else{
                                        vm.consultingPadron = false;
                                        var id = vm.visitor_id_number;
                                        vm.visitor_id_number = id;
                                         vm.founded = false;
                                        vm.showLock = false;
                                         vm.showLockCed = false;
                        }
                        if (vm.visitorsConsultedByPlate.length > 0) {
                            vm.visitorsConsultedByPlate.reverse();
                            vm.setVisitorConsulted(vm.visitorShowing)

                        }

}
                    }

                }

        vm.getVisitorByPlate = function() {
            vm.visitorShowing = 0;
            vm.visitorSelected = {}
            vm.visitorsConsultedByPlate = [];
             vm.searchTextPadron = "Consultando en visitas anteriores";
             vm.showLock = false;
             vm.consultingPadron = true;
            var encontrado = 0
            if (vm.visitor_license_plate != undefined || vm.visitor_license_plate != "") {
            setTimeout(function(){
                vm.visitorsConsultedByPlate = [];
                angular.forEach(visitorsList, function(itemVisitor, index) {
                    if (itemVisitor.licenseplate == vm.visitor_license_plate.toUpperCase() && itemVisitor.isinvited == 3) {
                        var existe = 0;
                        encontrado = encontrado +1;
                        angular.forEach(vm.visitorsConsultedByPlate, function(visitor, index) {
                            if (visitor.identificationnumber == itemVisitor.identificationnumber) {
                                existe = existe+1;
                            }
                        })
                        if (existe == 0) {
                            vm.visitorsConsultedByPlate.push(itemVisitor)
                        }
                    }
                });

                $scope.$apply(function(){
                if(encontrado>0){
                  vm.consultingPadron = false;
                                    vm.founded = true;
                                    vm.showLock = true;
                                    vm.showLockCed = true;
                                    vm.showLockCed = true;
                }else{
                  toastr["error"]("Los datos del visitante no se han encontrado, por favor ingresarlos manualmente")
                                vm.consultingPadron = false;
                                var id = vm.visitor_id_number;
                                vm.visitor_id_number = id;
                                 vm.founded = false;
                                vm.showLock = false;
                                 vm.showLockCed = false;
                }
                if (vm.visitorsConsultedByPlate.length > 0) {
                    vm.visitorsConsultedByPlate.reverse();
                    vm.setVisitorConsulted(vm.visitorShowing)

                }
                })
                },500)
            }

        }


        vm.setVisitorConsulted = function(index) {
            vm.visitor_name = vm.visitorsConsultedByPlate[index].name;
            vm.visitor_last_name = vm.visitorsConsultedByPlate[vm.visitorShowing].lastname;
            vm.visitor_second_last_name = vm.visitorsConsultedByPlate[vm.visitorShowing].secondlastname;
            vm.visitor_license_plate = vm.visitorsConsultedByPlate[vm.visitorShowing].licenseplate;
            vm.visitor_id_number = vm.visitorsConsultedByPlate[vm.visitorShowing].identificationnumber;
            setHouse(vm.visitorsConsultedByPlate[vm.visitorShowing].houseId);
        }
        vm.setVisitorSelected = function() {
            if (vm.visitorSelected != undefined) {
                vm.visitor_name = vm.visitorSelected.name;
                vm.visitor_last_name = vm.visitorSelected.lastname;
                vm.visitor_second_last_name = vm.visitorSelected.secondlastname;
                vm.visitor_license_plate = vm.visitorSelected.licenseplate;
                vm.visitor_id_number = vm.visitorSelected.identificationnumber;
                setHouse(vm.visitorSelected.houseId);
            }
        }

        vm.capitalize = function() {
            if (vm.visitor_license_plate != "") {
                $("#license_plate").css("text-transform", "uppercase");
            } else {
                $("#license_plate").css("text-transform", "none");
                $("#license_plate").attr("placeholder", "Número placa (sin guiones)");
                $("#vm.id_number").attr("placeholder", "Cédula");
            }
            if (vm.visitor_license_plate !== " " || vm.visitor_license_plate !== undefined && vm.founded==false) {

                vm.getVisitorByPlateNgChange();
            }
        }



        vm.getKeys = function() {
            if (securityKey == null || emergencyKey == null) {
                toastr["error"]("Esta casa aún no tiene claves de seguridad asignadas");
            } else {
                bootbox.dialog({
                    message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div>\
                        <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de seguridad: <span class="font-20 bold" id="security_key">1134314</span></h1></div>\
                          <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de emergencia: <span class="font-20 bold" id="emergency_key">1134314</span></h1></div>',
                    closeButton: false,
                    buttons: {
                        confirm: {
                            label: 'Ocultar',
                            className: 'btn-success'
                        }
                    },
                })
                document.getElementById("key_id_house").innerHTML = "" + housenumber;
                document.getElementById("security_key").innerHTML = "" + securityKey;
                document.getElementById("emergency_key").innerHTML = "" + emergencyKey;
            }
        }

        function clearInputs() {
            vm.visitor_id_number = ""
            vm.visitor_name = ""
            vm.visitor_last_name = "";
            vm.visitor_second_last_name = "";
            vm.visitor_license_plate = "";
            vm.house = "";
        }
        vm.houseInformationInput = function() {
            vm.show = 11;
            $("#license_plate").css("text-transform", "none");
            $("#license_plate").attr("placeholder", "Número placa (sin guiones)");
            $("#id_license_number").attr("placeholder", "Cédula");
            clearInputs();
            vm.houses = housesList;
        }
        vm.searchVisitor = function() {
        if(vm.show!=5){
            vm.show = 5;
            vm.showLock = true;
            vm.visitorsConsultedByPlate = [];
            if (vm.id_vehicule == undefined) {
                $("#license_plate").css("text-transform", "none");
                $("#license_plate").attr("placeholder", "Número placa (sin guiones)");
                $("#id_license_number").attr("placeholder", "Cédula");
            } else {
                $("#license_plate").css("text-transform", "uppercase");
            }
            clearInputs();
            vm.houses = housesList;

            if (vm.id_number == undefined || vm.id_number == "") {
             vm.consultingPadron = false;
            } else {
                vm.visitor_id_number = vm.id_number;
                vm.getVisitor();
            }
            if (vm.id_vehicule == undefined || vm.id_vehicule == "") {
            vm.consultingPadron = false;
            } else {
                vm.visitor_license_plate = vm.id_vehicule;
                vm.getVisitorByPlate();
            }
}
        }

        function setHouse(house) {
            vm.houses = housesList;
            for (var i = 0; i < housesList.length; i++) {
                if (housesList[i].id == house) {
                    vm.house = vm.houses[i];
                }
            }
        }

        vm.insertVisitor = function() {
        if(vm.visitor_id_number.length < 9 ){
        toastr["error"]("El formato de la cédula no es correcto, debe de tener al menos 9 dígitos")
        }else{
            vm.isInsertingVisitor = true;
            var visitant = {
                name: vm.visitor_name.toUpperCase(),
                lastname: vm.visitor_last_name.toUpperCase(),
                secondlastname: vm.visitor_second_last_name.toUpperCase(),
                identificationnumber: vm.visitor_id_number.toUpperCase(),
                licenseplate: vm.visitor_license_plate.toUpperCase(),
                companyId: $rootScope.companyId,
                isinvited: 3,
                arrivaltime: moment(new Date()).format(),
                houseId: vm.house.id
            }
            if (vm.house.id == undefined) {
                visitant.responsableofficer = vm.house.housenumber;
            }

            Visitant.save(visitant, onSaveSuccess, onSaveError);
           }
        }

        function onSaveSuccess(result) {
            vm.show = 4;
            vm.id_number = "";
            vm.id_vehicule = "";
            $("#id_license_number").css("text-transform", "none");
            $("#id_license_number").attr("placeholder", "Cédula");
            $("#vehicule_license_plate").css("text-transform", "none");
            $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
            vm.isSaving = false;
            vm.isInsertingVisitor = false;
            visitorsList.push(result);
            toastr["success"]("Se registró la entrada del visitante correctamente.");
            vm.showLockCed = false;
            vm.founded = false;
            loadVisitors();
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.deleteResidentVehiculeSpots = function() {
            $("#vehicule_license_plate").css("text-transform", "none");
            $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
            $("#id_license_number").attr("placeholder", "Cédula");
            $("#id_license_number").css("text-transform", "none");
            vm.id_number = "";
            vm.id_vehicule = "";
            vm.show = 4;

        }

        vm.getEmergency = function(emergency) {
            var house = vm.setHouse(emergency.houseId);
            vm.house_number_emergency = house.housenumber;
            vm.hideRegisterForm = 1;
            vm.hideEmergencyForm = 2;
            vm.show = 7;
        }

        //BEGIN WEB SOCKETS

        //JhiTrackerService.receiveResident().then(null, null, receiveResident);
        //JhiTrackerService.receiveVehicle().then(null, null, receiveVehicle);
        //JhiTrackerService.receiveHouse().then(null, null, receiveHouse);
        //JhiTrackerService.receiveVisitor().then(null, null, receiveVisitor);
        //JhiTrackerService.receiveHomeService().then(null, null, receiveHomeService);
        //JhiTrackerService.receiveDeletedEntity().then(null, null, receiveDeletedEntity);


        vm.attendEmergency = function() {
            var codeEmegency = $rootScope.companyId + "" + vm.emergency.houseId;
            vm.emergency.isattended = 1;
            vm.hideRegisterForm = 2;
            vm.hideEmergencyForm = 1;
            toastr["success"]("Se ha reportado al residente que se atenderá la emergencia");
            WSEmergency.sendActivityAttended(codeEmegency, vm.emergency);
            vm.emergency = undefined;
            setTimeout(function() {
                loadEmergencies();
            }, 500)
        }


        function receiveEmergency(emergency) {
            if (vm.emergency == undefined) {
                vm.getEmergency(emergency);
                vm.emergency = emergency;
            }
        }

        var hasExistance = function(array, id) {
            var index = undefined;
            angular.forEach(array, function(item, i) {
                if (parseInt(item.id) === parseInt(id)) {
                    index = i;
                } else {
                    index = -1;
                }
            })
            return index;
        };


        function receiveVisitor(visitor) {
        ngNotify.config({
            theme: 'pure',
            position: 'bottom',
            duration: 3000000,
            type: 'warn',
            sticky: true,
            button: true,
            html: false
        });

            Visitant.findAllInvited({
                companyId: $rootScope.companyId
            }, onSuccessInvited, onError);

            function onSuccessInvited(visitors, headers) {
                if (invitedList !== undefined) {
                    var result = hasExistance(invitedList, visitor.id)
                    if (result !== -1) {
                        invitedList[result] = visitor;
                    } else {
                        invitedList.push(visitor);
                    }
                 if(visitor.isinvited!=2){
                 var houseNumber = 0;
                                     angular.forEach(visitors, function(visitor, index) {
                                         angular.forEach(housesList, function(house, index) {
                                             if (house.id == visitor.houseId) {
                                                 houseNumber= house.housenumber;
                                             }
                                         })
                                     })
                 ngNotify.set('Se ha reportado uno o más invitados en la casa '+houseNumber+'.' );
                 }
                }
                invitedList = visitors;
            }




            // if(invitedList!==undefined){
            //     var result = undefined;
            //     angular.forEach(invitedList,function(item,i){
            //         if (item.id === visitor.id) {
            //             result = i;
            //         }else{
            //             result = -1;
            //         }
            //     });
            // if(result!==-1){
            //     invitedList[result] = visitor;
            // }else{
            //     invitedList.push(visitor);
            // }
            // }
        }

        function receiveHomeService(homeService) {
              var houseNumber = 0;
            if (vm.notes !== undefined) {
                var result = hasExistance(vm.notes, homeService.id)

                if (result == undefined || result == -1) {

                    homeService.sinceDate = moment(homeService.creationdate).fromNow();
                    vm.notes.push(homeService);
                    angular.forEach(vm.notes, function(note, index) {
                        angular.forEach(housesList, function(house, index) {
                            if (house.id == note.houseId) {
                                note.housenumber = house.housenumber;
                                houseNumber= house.housenumber;
                            }
                        })
                    })
ngNotify.config({
    theme: 'pure',
    position: 'bottom',
    duration: 3000000,
    type: 'grimace',
    sticky: true,
    button: true,
    html: false
});
ngNotify.set('Se ha recibido un nuevo servicio a domicilio en la casa '+houseNumber+'.' );
                    vm.countNotes = vm.notes.length;
                }

            }
        }

        vm.updateDateNotes = function() {
            angular.forEach(vm.notes, function(note, index) {

                note.sinceDate = moment(note.creationdate).fromNow();
            })
        }

        function receiveResident(resident) {

            if (residentsList !== undefined) {
                var result = hasExistance(residentsList, resident.id)
                if (result !== -1) {
                    residentsList[result] = resident;
                } else {
                    residentsList.push(resident);
                }
            }
        }

        function receiveVehicle(vehicle) {
            if (vehiculesList !== undefined) {
                var result = hasExistance(vehiculesList, vehicle.id)

                if (result !== -1) {
                    vehiculesList[result] = vehicle;
                } else {
                    vehiculesList.push(vehicle);
                }
            }
        }

        function receiveHouse(house) {
            House.query({
                companyId: $rootScope.companyId
            }, onSuccessHouse, onError);

            function onSuccessHouse(houses, headers) {
                angular.forEach(houses, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                housesList = houses;
                vm.housesToShow = houses;
            }
        }

        function receiveDeletedEntity(entity) {
            switch (entity.type) {
                case 'resident':
                    Resident.query({
                        companyId: $rootScope.companyId
                    }, onSuccessResident, onError);

                    function onSuccessResident(residents, headers) {
                        residentsList = residents;

                    }
                    //        if(residentsList!==undefined){
                    //            var result = hasExistance(residentsList,entity.id)
                    //            if(result!==-1){
                    //                residentsList[result] = {};
                    //            }
                    //         }
                    break;
                case 'vehicle':

                    Vehicule.query({
                        companyId: $rootScope.companyId
                    }, onSuccessVehicule, onError);

                    function onSuccessVehicule(vehicules, headers) {
                        vehiculesList = vehicules;
                    }

                    //         if(vehiculesList!==undefined){
                    //             var result = hasExistance(vehiculesList,entity.id)
                    //             if(result!==-1){
                    //                 vehiculesList[result] = {};
                    //             }
                    //        }
                    break;
                case 'visitor':

                    Visitant.query({
                        companyId: $rootScope.companyId
                    }, onSuccessVisitor, onError);

                    function onSuccessVisitor(visitors, headers) {
                        visitorsList = visitors;
                        Visitant.findAllInvited({
                            companyId: $rootScope.companyId
                        }, onSuccessInvited, onError);

                        function onSuccessInvited(visitors, headers) {
                            invitedList = visitors;
                        }
                    }


                    //          if(visitorsList!==undefined){
                    //              var result = hasExistance(visitorsList,entity.id)
                    //              if(result!==-1){
                    //                  visitorsList[result] = {};
                    //              }
                    //        }
                    break;
            }
        }
        //END WEBSOCKETS
    }
})();
