(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorContainerController', AccessDoorContainerController);

    AccessDoorContainerController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'House', 'globalCompany','Destinies'];

    function AccessDoorContainerController($mdToast, $timeout, Auth, $state, $scope, $rootScope, House, globalCompany,Destinies) {
        var vm = this;
        $rootScope.mainTitle = "Puerta de Acceso";
        House.getAllHousesClean({companyId: globalCompany.getId()}, function (data) {
            $rootScope.houses = data;
        });
        Destinies.query(function (destinies) {
            $rootScope.destinies = destinies;
        });
        $rootScope.houseSelected = -1;
        var delay = 1000;
        $rootScope.online = true;
        var toastOffline;
        function receiveHouse(house) {
            House.getAllHousesClean({companyId: globalCompany.getId()}, function (data) {
                $rootScope.houses = data;
            });
        }
        function receiveHomeService(note) {
                vm.showNotificationNote = true;
                toastr["info"]("¡Se ha recibido una nueva nota de la filial " + note.house.housenumber + "!")
                vm.countNotes = $rootScope.notes.length;
        }
        function receiveEmergency(emergency) {
            if (emergency.isAttended == 0) {
                vm.emergency = emergency;
                vm.emergencyInProgress = true;
                $rootScope.emergencyList.push(emergency)
            }
        }
        // function unsubscribe() {
        //     // WSDeleteEntity.unsubscribe(globalCompany.getId());
        //     WSEmergency.unsubscribe(globalCompany.getId());
        //     WSHouse.unsubscribe(globalCompany.getId());
        //     // WSResident.unsubscribe(globalCompany.getId());
        //     // WSVehicle.unsubscribe(globalCompany.getId());
        //     WSNote.unsubscribe(globalCompany.getId());
        //     // WSVisitor.unsubscribe(globalCompany.getId());
        //     // WSOfficer.unsubscribe(globalCompany.getId());
        // }
        // function subscribe() {
        //     $timeout(function () {
        //         WSEmergency.subscribe(globalCompany.getId());
        //         WSHouse.subscribe(globalCompany.getId());
        //         // WSResident.subscribe(globalCompany.getId());
        //         // WSVehicle.subscribe(globalCompany.getId());
        //         WSNote.subscribe(globalCompany.getId());
        //         // WSVisitor.subscribe(globalCompany.getId());
        //         // WSOfficer.subscribe(globalCompany.getId());
        //         // WSDeleteEntity.subscribe(globalCompany.getId());
        //         // WSDeleteEntity.receive().then(null, null, receiveDeletedEntity);
        //         WSEmergency.receive().then(null, null, receiveEmergency);
        //         WSHouse.receive().then(null, null, receiveHouse);
        //         // WSResident.receive().then(null, null, receiveResident);
        //         // WSVehicle.receive().then(null, null, receiveVehicle);
        //         WSNote.receive().then(null, null, receiveHomeService);
        //         WSVisitor.receive().then(null, null, receiveVisitor);
        //         // WSOfficer.receive().then(null, null, receiveOfficer);
        //     }, 3000);
        // }
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


        Offline.on('confirmed-down', function () {
            if ($rootScope.online) {
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
            if (!$rootScope.online) {
                $mdToast.hide();
                $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo está conectado a internet nuevamente.")
                        .position("top center")
                );
                $rootScope.online = true;
                // unsubscribe();
                // subscribe();
            }
        });
        $rootScope.timerAd = $timeout(function retry() {
            Offline.check();
            $timeout(retry, delay);
        }, delay);
    }
})();
