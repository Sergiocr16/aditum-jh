
(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsDetailController', CommonAreaReservationsDetailController);

    CommonAreaReservationsDetailController.$inject = ['BitacoraAcciones','globalCompany','Principal','$timeout', '$scope', '$stateParams', 'entity', 'CommonAreaReservations','Resident','House','CommonArea','Charge','$rootScope','CommonMethods','$state','Modal'];

    function CommonAreaReservationsDetailController (BitacoraAcciones,globalCompany,Principal,$timeout, $scope, $stateParams, entity, CommonAreaReservations,Resident,House,CommonArea,Charge,$rootScope,CommonMethods,$state,Modal) {
        var vm = this;
        vm.commonAreaReservations = entity;
        vm.isAuthenticated = Principal.isAuthenticated;
        $rootScope.mainTitle = 'Detalle de reservación';
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        vm.minDate = new Date();
        vm.sendEmail = true;
        vm.isReady = false;
        vm.charge = {
            type: "3",
            concept: "",
            ammount: vm.commonAreaReservations.reservationCharge,
            devolution: vm.commonAreaReservations.devolutionAmmount,
            date: new Date(),
            valida: true,
            state: 1,
            deleted: 0
        };
        loadInfo();


        var data = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        if (data.hasContability == 1) {
            vm.hasContability = true;
        } else {
            vm.hasContability = false;
        }

        function loadInfo(){

            House.get({
                id:  vm.commonAreaReservations.houseId
            }, function(result) {
                vm.commonAreaReservations.houseNumber = result.housenumber;
                vm.houseNumber = result.housenumber;
                Resident.get({
                    id: vm.commonAreaReservations.residentId
                }, function(result) {
                    vm.commonAreaReservations.residentName = result.name + " " + result.lastname;
                    vm.commonAreaReservations.phone = result.phonenumber;
                    vm.commonAreaReservations.chargeEmail = result.email;
                    console.log("dafadf")
                    console.log(result.email)
                    CommonArea.get({
                        id: vm.commonAreaReservations.commonAreaId
                    }, function(result) {
                        vm.commonAreaReservations.commonAreaName = result.name ;
                        vm.charge.concept = "Uso de " + vm.commonAreaReservations.commonAreaName;
                        vm.commonAreaReservations.schedule = formatScheduleTime(vm.commonAreaReservations.initialTime, vm.commonAreaReservations.finalTime);
                        vm.isReady = true;
                    })
                })
            })

        }



        function formatScheduleTime(initialTime, finalTime) {
            var times = [];
            times.push(initialTime);
            times.push(finalTime);
            angular.forEach(times, function (value, key) {
                if (value == 0) {
                    times[key] = "12:00AM"
                } else if (value < 12) {
                    if (esEntero(parseFloat(value))) {
                        times[key] = value + ":00AM"
                    } else {
                        var time = value.split(".")[0];
                        var min = value.split(".")[1];
                        if (min == 75) {
                            min = 45;
                        }
                        if(min == 5){
                            min = 30;
                        }
                        times[key] = time + ":"+min+"AM";
                    }
                } else if (value > 12) {
                    if (esEntero(parseFloat(value))) {
                        times[key] = value - 12 + ":00PM"
                    } else {
                        var time = value.split(".")[0];
                        var min = value.split(".")[1];
                        if (min == 75) {
                            min = 45;
                        }
                        if(min == 5){
                            min = 30;
                        }
                        times[key] = time + ":"+min+"PM";
                    }
                } else if (value == 12) {
                    times[key] = value + ":00PM"
                }
            });
            return times[0] + " - " + times[1]
        }

        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function save () {

            vm.isSaving = true;


        }

        function onSaveSuccess (result) {

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.validateReservationCharge = function(cuota) {
            var s = cuota.ammount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function(val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase() || s == undefined) {
                            invalido++;
                        }
                    }
                }
            });
            if(invalido==0) {
                cuota.reservationChargeValida = true;
            } else {
                cuota.reservationChargeValida = false
            }
        };

        vm.validateDevolutionAmmount = function(commonArea) {
            var s = commonArea.devolutionAmmount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function(val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase() || s == undefined) {
                            invalido++;
                        }
                    }
                }
            });
            if (invalido == 0) {
                commonArea.devolutionAmmountValida = true;
            } else {
                commonArea.devolutionAmmountValida = false
            }
        };
        function createCharge () {
            Modal.showLoadingBar()
            vm.isSaving = true;
            vm.charge.houseId = vm.commonAreaReservations.houseId;
            vm.charge.companyId = globalCompany.getId();
            vm.commonAreaReservations.initalDate = new Date(vm.commonAreaReservations.initalDate)
            vm.commonAreaReservations.initalDate.setHours(0);
            vm.commonAreaReservations.initalDate.setMinutes(0);
            if(vm.sendEmail){
                vm.commonAreaReservations.sendPendingEmail = true ;
            }else{
                vm.commonAreaReservations.chargeEmail = null;
                vm.commonAreaReservations.sendPendingEmail = false ;
            }
            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

            if (companyConfig.hasContability == 0) {
                vm.commonAreaReservations.status = 2;
                CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);
            } else {
                if (vm.commonAreaReservations.reservationCharge == null || vm.commonAreaReservations.reservationCharge ===0) {
                    vm.commonAreaReservations.status = 2;
                    CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);
                } else {
                    vm.charge.companyId = $rootScope.companyId;
                    Charge.save(vm.charge, function (result) {
                        var concept = "Creación de cuota de áreas comunes" + ": " + vm.charge.concept + ", "+ " a la filial " + vm.houseNumber + " por " + vm.formatearNumero(vm.charge.ammount+"") + " colones";
                        BitacoraAcciones.save(mapBitacoraAcciones(concept), function () {});
                        vm.commonAreaReservations.status = 2;
                        vm.commonAreaReservations.reservationCharge = vm.charge.ammount;
                        vm.commonAreaReservations.chargeIdId = result.id;
                        CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);
                    })
                }
            }


            function onSaveSuccess(result) {
                $state.go('common-area-administration.common-area-reservations')
                Modal.toast("Se ha aprobado la reservación correctamente.")
                Modal.hideLoadingBar()
            }
        }


        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }


        function mapBitacoraAcciones (concept){
            vm.bitacoraAcciones = {};
            vm.bitacoraAcciones.concept = concept;
            vm.bitacoraAcciones.type = 6;
            vm.bitacoraAcciones.ejecutionDate = new Date();
            vm.bitacoraAcciones.category = "Cuotas";
            vm.bitacoraAcciones.idReference = 1;
            vm.bitacoraAcciones.idResponsable = globalCompany.getUser().id;
            vm.bitacoraAcciones.companyId = globalCompany.getId();
            return vm.bitacoraAcciones;

        }

        vm.cancelReservation = function() {

            Modal.confirmDialog("¿Está seguro que desea rechazar la reservación?", "Una vez registrada esta información no se podrá editar",
                function () {
                    Modal.showLoadingBar()
                    if(vm.sendEmail){
                        vm.commonAreaReservations.sendPendingEmail = true ;
                    }else{
                        vm.commonAreaReservations.sendPendingEmail = false ;
                    }
                    vm.commonAreaReservations.status = 3;
                    vm.commonAreaReservations.initalDate = new Date(vm.commonAreaReservations.initalDate)
                    vm.commonAreaReservations.initalDate.setHours(0);
                    vm.commonAreaReservations.initalDate.setMinutes(0);
                    CommonAreaReservations.update(vm.commonAreaReservations, onCancelSuccess, onSaveError);

                });


        };
        function onCancelSuccess(result) {
                Modal.hideLoadingBar()
               Modal.toast("Se ha rechazado la reservación correctamente.")
                $state.go('common-area-administration.common-area-reservations')

        }
        vm.acceptReservation = function() {

            Modal.confirmDialog("¿Está seguro que desea aceptar la reservación?", "Una vez registrada esta información no se podrá editar",
                function () {
                    createCharge()
            });


        }
    }
})();
