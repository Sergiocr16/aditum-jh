(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorContainerController', AccessDoorContainerController);

    AccessDoorContainerController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'House', 'globalCompany', 'Destinies', 'Emergency', 'WSEmergency', 'WSNote', 'WSHouse', 'WSVisitorInvitation', 'Modal', 'CommonMethods'];

    function AccessDoorContainerController($mdToast, $timeout, Auth, $state, $scope, $rootScope, House, globalCompany, Destinies, Emergency, WSEmergency, WSNote, WSHouse, WSVisitorInvitation, Modal, CommonMethods) {
        var vm = this;
        $rootScope.mainTitle = "Puerta de Acceso";
        $rootScope.emergencyList = [];
        $rootScope.notes = [];
        $rootScope.houseNoteNotification = undefined;
        $rootScope.visitorHouseNotification = undefined;
        $rootScope.houseSelectedNote = -1;
        $rootScope.deletedStatusNote = 0;

        $rootScope.visitorInvited = [];
        $rootScope.notes = [];
        House.getAllHousesClean({companyId: globalCompany.getId()}, function (data) {
            $rootScope.houses = data;
            subscribe();
        });
        Destinies.query(function (destinies) {
            $rootScope.destinies = destinies;
        });
        // $scope.$on("$destroy", function () {
        //     unsubscribe();
        // });
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-bottom-center",
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }

        vm.showKeys = function () {
            Modal.customDialog("<md-dialog>" +
                "<md-dialog-content class='md-dialog-content text-center'>" +
                "<h1 class='md-title'>Número de soporte </h1>" +
                "<div class='md-dialog-content-body'>" +
                "<p>En caso de necesitar ayuda o el sistema le presenta un problema, favor comunicarse al <b>8624-5504</b> o <b>6002-3372</b></p>" +

                "</div>" +
                "</md-dialog-content>" +
                "</md-dialog>")
        };
        loadEmergencies();
        function existItem(array, item) {
            var founded = false;
            angular.forEach(array, function (item, i) {
                if (parseInt(item.id) === parseInt(item.id)) {
                    founded = true;
                }
            })
            return founded;
        };
        function loadEmergencies() {
            Emergency.findAll({
                companyId: globalCompany.getId()
            }, onSuccessEmergencies, onError);

            function onSuccessEmergencies(emergencies, headers) {
                if (emergencies.length > 0) {
                    vm.emergency = undefined;
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

        $rootScope.houseSelected = -1;
        var delay = 1000;
        $rootScope.online = true;
        var toastOffline;
        vm.emergencyInProgress = false;

        function receiveHomeService(note) {
            vm.showNotificationNote = true;
            note.sinceDate = moment(note.creationdate).fromNow();
            if($rootScope.deletedStatusNote==0){
                if($rootScope.houseSelectedNote==-1 && $rootScope.noteCreatedBy!=2){
                    if(existItem($rootScope.notes,note)){
                        $rootScope.notes.push(note);
                    }
                }
                if($rootScope.houseSelectedNote==note.houseId && $rootScope.noteCreatedBy!=2){
                    if(existItem($rootScope.notes,note)){
                        $rootScope.notes.push(note);
                    }
                }
            }
            Modal.actionToastGiant("¡Se ha recibido una nueva nota de la filial " + note.house.housenumber + "!", "Ver detalle", function () {
                $rootScope.houseNoteNotification = note.house.id;
                vm.showNotificationNote = false;
                $state.go("access-door.notes");
            });
        }

        function receiveEmergency(emergency) {
            if (emergency.isAttended == 0) {
                vm.emergency = emergency;
                vm.emergencyInProgress = true;
            }
        }

        function unsubscribe() {
            // WSDeleteEntity.unsubscribe(globalCompany.getId());
            WSEmergency.unsubscribe(globalCompany.getId());
            WSHouse.unsubscribe(globalCompany.getId());
            // WSResident.unsubscribe(globalCompany.getId());
            // WSVehicle.unsubscribe(globalCompany.getId());
            WSNote.unsubscribe(globalCompany.getId());
            WSVisitorInvitation.unsubscribe(globalCompany.getId());
            // WSVisitor.unsubscribe(globalCompany.getId());
            // WSOfficer.unsubscribe(globalCompany.getId());
            console.log("Probando")
        }

        function subscribe() {
            unsubscribe();
            setTimeout(function(){
                WSEmergency.subscribe(globalCompany.getId());
                WSHouse.subscribe(globalCompany.getId());
                // WSResident.subscribe(globalCompany.getId());
                // WSVehicle.subscribe(globalCompany.getId());
                WSVisitorInvitation.subscribe(globalCompany.getId());
                WSNote.subscribe(globalCompany.getId());
                // WSOfficer.subscribe(globalCompany.getId());
                // WSDeleteEntity.subscribe(globalCompany.getId());
                // WSDeleteEntity.receive().then(null, null, receiveDeletedEntity);
                WSEmergency.receive().then(null, null, receiveEmergency);
                WSHouse.receive().then(null, null, receiveHouse);
                // WSResident.receive().then(null, null, receiveResident);
                // WSVehicle.receive().then(null, null, receiveVehicle);
                WSNote.receive().then(null, null, receiveHomeService);
                // WSOfficer.receive().then(null, null, receiveOfficer);
                WSVisitorInvitation.receive().then(null, null, receiveVisitorInvitation);
            },3000)
        }
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
        function receiveVisitorInvitation(visitor) {
            if (visitor.status == 1) {
                        var title = "";
                var visitorI = formatVisitantInvited(visitor);

                if(visitor.houseNumber==null){
                                title = "¡Se ha invitado un visitante en la oficina del administrador!";
                        }else{
                                title = "¡Se ha invitado un visitante en la filial " + visitor.houseNumber + "!";
                        }
                        if(visitor.houseNumber==null){
                            if($rootScope.houseSelected == -1 ) {
                                var visitorI = formatVisitantInvited(visitor);
                                // if(!existItem($rootScope.visitorInvited,visitorI)){
                                    $rootScope.visitorInvited.push(visitorI);
                                // }
                            }
                        }else{
                            var visitorI = formatVisitantInvited(visitor);
                            // if(!existItem($rootScope.visitorInvited,visitorI)){
                                $rootScope.visitorInvited.push(visitorI);
                            // }
                        }
                        Modal.actionToastGiant(title, "Ver detalle", function () {
                            if(visitor.houseNumber==null) {
                                $rootScope.visitorHouseNotification = -1;
                            }else {
                                $rootScope.visitorHouseNotification = visitor.houseId;
                            }
                            $state.go("access-door.houses");
                        });
            } else {
                setTimeout(function () {
                    $scope.$apply(function () {
                        CommonMethods.deleteFromArrayWithId(visitor, $rootScope.visitorInvited);
                    })
                }, 10)
            }
        }

        function receiveHouse(house) {
            House.getAllHousesClean({companyId: globalCompany.getId()}, function (data) {
                $rootScope.houses = data;
            });
        }

        vm.attendEmergency = function () {
            var codeEmegency = globalCompany.getId() + "" + vm.emergency.houseId;
            vm.emergency.isAttended = 1;
            Modal.toastGiant("Se ha reportado al residente que se atenderá la emergencia");
            Emergency.update(vm.emergency, function (result) {
                WSEmergency.sendActivityAttended(codeEmegency, vm.emergency);
                $timeout(function () {
                    vm.emergency = undefined;
                    vm.emergencyInProgress = false;
                    loadEmergencies();
                }, 5)

            })
        };


        Offline.on('confirmed-down', function () {
            if ($rootScope.online) {
                toastOffline = $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo perdió conexión a internet.")
                        .hideDelay(0)
                        .position("top center")
                );
                unsubscribe();
                $rootScope.online = false;
            }
        });

        Offline.on('confirmed-up', function () {
            if (!$rootScope.online) {
                $mdToast.hide();
                $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo está conectado a internet nuevamente.")
                        .position("top center")
                );
                $rootScope.online = true;
                subscribe();
            }
        });
        $rootScope.timerAd = $timeout(function retry() {
            Offline.check();
            $timeout(retry, delay);
        }, delay);
    }
})();
