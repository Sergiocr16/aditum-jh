
(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsDetailController', CommonAreaReservationsDetailController);

    CommonAreaReservationsDetailController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CommonAreaReservations','Resident','House','CommonArea','Charge','$rootScope','CommonMethods','$state'];

    function CommonAreaReservationsDetailController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CommonAreaReservations,Resident,House,CommonArea,Charge,$rootScope,CommonMethods,$state) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.commonAreaReservations = entity;
        console.log(vm.commonAreaReservations);
        vm.minDate = new Date();
        vm.sendEmail = false;
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

        function loadInfo(){
            House.get({
                id:  vm.commonAreaReservations.houseId
            }, function(result) {
                vm.commonAreaReservations.houseNumber = result.housenumber;
                Resident.get({
                    id: vm.commonAreaReservations.residentId
                }, function(result) {
                    vm.commonAreaReservations.residentName = result.name + " " + result.lastname;
                    CommonArea.get({
                        id: vm.commonAreaReservations.commonAreaId
                    }, function(result) {
                        vm.commonAreaReservations.commonAreaName = result.name ;
                        vm.charge.concept = "Uso de " + vm.commonAreaReservations.commonAreaName;
                        vm.commonAreaReservations.schedule = formatScheduleTime(vm.commonAreaReservations.initialTime, vm.commonAreaReservations.finalTime);
                    })
                })
            })

        }

        vm.switchSendEmail = function(){
            vm.sendEmail = !vm.sendEmail;
        }

        function formatScheduleTime(initialTime, finalTime){
            var times = [];
            times.push(initialTime);
            times.push(finalTime);
            angular.forEach(times,function(value,key){
                if(value==0){
                    times[key] = "12:00AM"
                }else if(value<12){
                    times[key] = value + ":00AM"
                }else if(value>12){
                    times[key] = parseInt(value)-12 + ":00PM"
                }else if(value==12){
                    times[key] = value + ":00PM"
                }

            });
            return times[0] + " - " + times[1]
            console.log(times)
        }

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            console.log('ad')
            vm.isSaving = true;


        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:commonAreaScheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.chargeDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
        vm.picker = {
            datepickerOptions: {
                minDate: moment().subtract(1, 'days').startOf(new Date()),
                enableTime: false,
                showWeeks: false,
                daysOfWeekDisabled: [0,1,2,3,4,5,6],
                clearBtn: false,
                todayBtn: false
            }
        };
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
            CommonMethods.waitingMessage();
            vm.isSaving = true;
            vm.charge.houseId = vm.commonAreaReservations.houseId;
            vm.charge.companyId = $rootScope.companyId;
            if (vm.commonAreaReservations.reservationCharge == null) {
                vm.commonAreaReservations.status = 2;
                CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);

            } else {
                Charge.save(vm.charge, function (result) {
                    vm.commonAreaReservations.status = 2;
                    vm.commonAreaReservations.reservationCharge = vm.charge.ammount;
                    CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);


                })
            }

            function onSaveSuccess(result) {
                .go('common-area-administration.common-area-reservations')
                toastr["success"]("Se ha aprobado la reservación correctamente.")
                bootbox.hideAll();
            }
        }

        vm.cancelReservation = function() {
            bootbox.confirm({
                message: '<div class="text-center gray-font font-12"><h3 style="margin-bottom:10px;">¿Está seguro que desea <span class="bold">rechazar</span> la reservación?</h3></div>',
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
                callback: function (result) {
                    CommonMethods.waitingMessage();
                    if (result) {
                        vm.commonAreaReservations.status = 3;
                        CommonAreaReservations.update(vm.commonAreaReservations, onCancelSuccess, onSaveError);


                    } else {
                        vm.isSaving = false;

                    }
                }
            });
        };
        function onCancelSuccess(result) {
                bootbox.hideAll();
                toastr["success"]("Se ha generado la cuota correctamente.")
                $state.go('houseAdministration.chargePerHouse')

        }
        vm.acceptReservation = function() {
            bootbox.confirm({
                message: '<div class="text-center gray-font font-12"><h3 style="margin-bottom:10px;">¿Está seguro que desea <span class="bold">aprobar</span> la reservación?</h3></div>',
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
                callback: function (result) {

                    if (result) {
                        createCharge()

                    } else {
                        vm.isSaving = false;

                    }
                }
            });
        }
    }
})();
