(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsDialogResidentViewController', CommonAreaReservationsDialogResidentViewController);

    CommonAreaReservationsDialogResidentViewController.$inject = ['AditumStorageService', 'PaymentProof', '$timeout', '$scope', '$stateParams', 'entity', 'CommonAreaReservations', 'CommonArea', '$rootScope', 'House', 'Resident', 'CommonAreaSchedule', 'AlertService', '$state', 'CommonMethods', 'Principal', 'Modal', 'CompanyConfiguration', 'globalCompany'];

    function CommonAreaReservationsDialogResidentViewController(AditumStorageService, PaymentProof, $timeout, $scope, $stateParams, entity, CommonAreaReservations, CommonArea, $rootScope, House, Resident, CommonAreaSchedule, AlertService, $state, CommonMethods, Principal, Modal, CompanyConfiguration, globalCompany) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
        vm.commonarea = {};
        $rootScope.active = "reservationDialogResidentView";
        vm.commonArea = entity;
        var initialDateTemporal;
        vm.datePickerOpenStatus = {};
        vm.today = new Date();
        vm.openCalendar = openCalendar;
        vm.reservationTitle = "Crear"
        $rootScope.mainTitle = "Crear reservación";
        vm.confirmMessage = confirmMessage;
        Modal.enteringForm(confirmMessage);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.minimunDate = new Date();

        vm.required = 1;
        vm.hours = [];
        vm.isReady = false;
        vm.schedule = [];
        vm.allDaySchedule = 1;
        vm.scheduleIsAvailable = false;
        vm.diasDeLaSemana = ['domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado', ''];
        vm.mesesDelAnno = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'];
        vm.timeSelected = {};
        vm.dateNotPermited = false;
        vm.commonarea.devolutionAmmount = 0;
        vm.commonAreaReservations = {};
        setTimeout(function () {
            CommonArea.query({
                companyId: globalCompany.getId()
            }, onSuccessCommonAreas, onError);
        }, 1500);

        CompanyConfiguration.get({id: globalCompany.getId()}, function (data) {
            if (data.hasContability == 1) {
                vm.hasContability = true;
            } else {
                vm.hasContability = false;
            }
        });

        var file = null;

        function upload() {
            vm.isSaving = true;
            var today = new Date();
            moment.locale("es");
            vm.direction = globalCompany.getId() + '/payment-proof/' + moment(today).format("YYYY") + '/' + moment(today).format("MMMM") + '/' + globalCompany.getHouseId() + '/';
            var uploadTask = AditumStorageService.ref().child(vm.direction + file.name).put(file);
            uploadTask.on('state_changed', function (snapshot) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    })
                }, 1)
                switch (snapshot.state) {
                    case firebase.storage.TaskState.PAUSED: // or 'paused'
                        console.log('Upload is paused');
                        break;
                    case firebase.storage.TaskState.RUNNING: // or 'running'
                        console.log('Upload is running');
                        break;
                }
            }, function (error) {
                // Handle unsuccessful uploads
            }, function () {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    vm.paymentProof = {};
                    vm.paymentProof.imageUrl = downloadURL;
                    vm.paymentProof.houseId = globalCompany.getHouseId();
                    vm.paymentProof.status = 1;
                    vm.paymentProof.companyId = globalCompany.getId();
                    vm.paymentProof.registerDate = moment(new Date());
                    vm.paymentProof.subject = "Comprobante de pago reservación de " + vm.commonarea.name + "."
                    vm.commonAreaReservations.initalDate.setHours(0);
                    vm.commonAreaReservations.initalDate.setMinutes(0);
                    vm.paymentProof.description = "Pago cuota de reservación de " + vm.commonarea.name + " para la fecha " + moment(vm.commonAreaReservations.initalDate).format("MM/DD/YYYY") + "."
                    PaymentProof.save(vm.paymentProof, onSaveSuccessPayment, onSaveError);
                });
            });
        }

        function onSaveSuccessPayment(data) {
            createReservation(data.id)
        }


        function onSuccessCommonAreas(data) {
            vm.commonareas = data;

            vm.isReady = true;
            loadInfo();

        }

        function loadInfo() {
            vm.residentsByHouse();
            if (entity.id !== null) {
                vm.commonarea.id = entity.id;
                vm.loadSchedule();
                if ($state.params.date !== undefined) {
                    vm.commonAreaReservations.initalDate = new Date($state.params.date)
                }
            }


        }

        vm.loadSchedule = function () {
            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;
            CommonArea.get({
                id: vm.commonarea.id
            }, function (result) {
                console.log('adfad')
                vm.commonarea = result;

                $("#scheduleDiv").fadeOut(50);
                $("#loadingSchedule").fadeIn('0');
                if (vm.commonarea === undefined) {
                    $("#loadingSchedule").fadeOut('20');
                } else {
                    CommonAreaSchedule.findSchedulesByCommonArea({
                        commonAreaId: result.id
                    }, onSuccessSchedule, onErrorSchedule);
                }
            });
        };

        function onSuccessSchedule(data, headers) {

            vm.schedule = [];
            if (data[0].lunes !== "-") {
                formatScheduleTime("Lunes", data[0].lunes, 1)
            }
            if (data[0].martes !== "-") {
                formatScheduleTime("Martes", data[0].martes, 2)
            }
            if (data[0].miercoles !== "-") {
                formatScheduleTime("Miércoles", data[0].miercoles, 3)
            }
            if (data[0].jueves !== "-") {
                formatScheduleTime("Jueves", data[0].jueves, 4)
            }
            if (data[0].viernes !== "-") {
                formatScheduleTime("Viernes", data[0].viernes, 5)
            }
            if (data[0].sabado !== "-") {

                formatScheduleTime("Sábado", data[0].sabado, 6)
            }
            if (data[0].domingo !== "-") {
                formatScheduleTime("Domingo", data[0].domingo, 0)
            }
            if (vm.commonarea.maximunHours == 0) {
                vm.maximunHoursTitle = "Tiempo de uso:";
                vm.maximunHours = " Bloque completo";
                vm.allDaySchedule = 2;
            } else {
                vm.maximunHoursTitle = "Tiempo máximo de uso: ";
                vm.maximunHours = vm.commonarea.maximunHours + " horas";
                vm.allDaySchedule = 1;
            }
            $("#scheduleDiv").fadeIn('50');
            $("#loadingSchedule").fadeOut('20');
            $("#commonAreaImage").fadeIn('300');
            if (vm.commonAreaReservations.id != null || $state.params.commonAreaId !== undefined || $state.params.date !== undefined) {
                if (isTheDayInSchedule(vm.commonAreaReservations.initalDate.getDay())) {
                    vm.commonAreaReservations.initalDate.setHours(0);
                    vm.commonAreaReservations.initalDate.setMinutes(0);
                    if (vm.commonarea.maximunHours === 0) {
                        $("#loadingAvailability").fadeIn('50');
                        var initialTime = "0";
                        var finalTime = "0";

                        if (vm.commonAreaReservations.id != null || vm.commonAreaReservations.id != undefined) {

                            CommonAreaReservations.isAvailableToReserveNotNull({
                                maximun_hours: vm.commonarea.maximunHours,
                                reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                                initial_time: initialTime,
                                final_time: finalTime,
                                common_area_id: vm.commonarea.id,
                                house_id: globalCompany.houseId(),
                                reservation_id: vm.commonAreaReservations.id
                            }, onSuccessIsAvailable, onError);
                        } else {
                            CommonAreaReservations.isAvailableToReserve({
                                maximun_hours: vm.commonarea.maximunHours,
                                reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                                initial_time: initialTime,
                                final_time: finalTime,
                                house_id: globalCompany.houseId(),
                                common_area_id: vm.commonarea.id

                            }, onSuccessIsAvailable, onError);
                        }

                    } else {
                        addHoursToSelect()
                    }
                } else {
                    $("#loadingAvailability").fadeOut('50');
                    vm.dateNotPermited = true;
                    Modal.toast("No se permite reservar el día " + vm.diasDeLaSemana[vm.commonAreaReservations.initalDate.getDay()] + " en esta área común")
                }
            }
        }

        function formatScheduleTime(day, time, number) {
            var item = {};
            item.day = day;
            item.numberDay = number;
            if (vm.commonarea.hasBlocks == 0) {
                var times = time.split("-");
                item.initialValue = times[0];
                item.finalValue = times[1];
                if (times[0] > 12) {
                    item.initialTime = parseInt(times[0]) - 12 + ":00PM"
                } else {
                    if (times[0] == 0) {
                        item.initialTime = "12:00AM"
                    } else {
                        item.initialTime = parseInt(times[0]) + ":00AM"
                    }

                }
                if (times[1] > 12) {
                    item.finalTime = parseInt(times[1]) - 12 + ":00PM"
                } else {
                    item.finalTime = parseInt(times[1]) + ":00AM"
                }
                item.time = item.initialTime + " - " + item.finalTime;
                vm.schedule.push(item);
            } else {
                var allTimes = time.split(",");
                item.times = [];
                for (var i = 0; i < allTimes.length; i++) {
                    var times = allTimes[i].split("-");

                    var initialValue = times[0];
                    var finalValue = times[1];
                    if (times[0] > 12) {
                        var initialTime = parseInt(times[0]) - 12 + ":00PM"
                    } else {
                        if (times[0] == 0) {
                            var initialTime = "12:00AM"
                        } else {
                            var initialTime = parseInt(times[0]) + ":00AM"
                        }

                    }
                    if (times[1] > 12) {
                        var finalTime = parseInt(times[1]) - 12 + ":00PM"
                    } else {
                        var finalTime = parseInt(times[1]) + ":00AM"
                    }
                    item.times.push({
                        initialValue: initialValue,
                        finalValue: finalValue,
                        time: initialTime + " - " + finalTime
                    })
                }
                vm.schedule.push(item);
                console.log(vm.schedule)

            }
        }

        function showErrorMessage(available) {
            vm.errorMessage = "";
            switch (available) {
                case 1 :
                    vm.errorMessage = "No es posible porque ha llegado al limite de reservaciones por periodo.";
                    break;
                case 2 :
                    vm.errorMessage = "No es posible porque para reservar esta amenidad se necesita hacer la reservación con " + vm.commonarea.daysBeforeToReserve + " dias de antelación.";
                    break;
                case 3 :
                    vm.errorMessage = "No es posible porque esta amenidad solo se puede reservar con una separación mínima de " + vm.commonarea.distanceBetweenReservations + " meses entre cada reservación.";
                    break;
                case 4:
                    vm.errorMessage = "Las horas seleccionadas se encuentran ocupadas para reservar.";
                    break;
            }
        }

        vm.validateBlocksHours = function (hour, index) {
            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;
            vm.timeSelected.initialTime = parseInt(hour.initialTime);
            vm.timeSelected.finalTime = parseInt(hour.finalTime);

            vm.checkAvailabilityBlocks();

        };

        var temporalFinalTime;
        vm.validateDaysInitialHours = function (hour, index) {
            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;
            angular.forEach(vm.hours, function (item, index) {
                if (item.value == hour.value) {
                    if (vm.hours[index + vm.commonarea.maximunHours] == undefined) {
                        vm.timeSelected.finalTime = vm.hours[vm.hours.length - 1];
                    } else {
                        vm.timeSelected.finalTime = vm.hours[index + vm.commonarea.maximunHours];

                    }
                    temporalFinalTime = vm.timeSelected.finalTime;
                }

            });
            vm.checkAvailabilityBetweenHours();
        };

        vm.validateDaysFinalHours = function (item) {

            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;

            if (vm.timeSelected.initialTime !== undefined && vm.timeSelected.finalTime !== undefined) {

                if (parseInt(vm.timeSelected.initialTime.value) >= parseInt(vm.timeSelected.finalTime.value)) {
                    vm.timeSelected.finalTime.isValid = false;
                    item.isValid = false;
                    Modal.toast("Debe seleccionar una hora final posterior a la hora anterior");
                } else {

                    if (vm.timeSelected.finalTime.value - vm.timeSelected.initialTime.value > vm.commonarea.maximunHours) {
                        vm.timeSelected.finalTime.isValid = false;
                        item.isValid = false;
                        Modal.toast("No puede seleccionar más horas del máximo establecido");
                        vm.timeSelected.finalTime = temporalFinalTime;


                    } else {
                        vm.timeSelected.finalTime.isValid = false;
                        item.isValid = false;
                        $("#loadingAvailability").fadeIn('50');

                    }
                    vm.timeSelected.finalTime.isValid = true;
                    vm.checkAvailabilityBetweenHours();

                }
            }

        };

        vm.checkAvailabilityBetweenHours = function () {

            $("#loadingAvailability").fadeIn('0');
            if (parseInt(vm.timeSelected.initialTime.value) == parseInt(vm.timeSelected.finalTime.value)) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.timeSelected.finalTime.isValid = false;
                    });
                }, 100);

                Modal.toast("Debe seleccionar al menos una hora de diferencia");


            } else {
                if (vm.commonAreaReservations.id != null || vm.commonAreaReservations.id != undefined) {

                    vm.commonAreaReservations.initalDate.setHours(0);
                    vm.commonAreaReservations.initalDate.setMinutes(0);
                    CommonAreaReservations.isAvailableToReserveNotNull({
                        maximun_hours: vm.commonarea.maximunHours,
                        reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                        initial_time: vm.timeSelected.initialTime.value,
                        final_time: vm.timeSelected.finalTime.value,
                        common_area_id: vm.commonarea.id,
                        house_id: globalCompany.getHouseId(),
                        reservation_id: vm.commonAreaReservations.id
                    }, onSuccessIsAvailable, onError);
                } else {
                    console.log("antes de f")
                    console.log(vm.timeSelected)
                    var a = {
                        maximun_hours: vm.commonarea.maximunHours,
                        reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                        initial_time: vm.timeSelected.initialTime.value,
                        final_time: vm.timeSelected.finalTime.value,
                        common_area_id: vm.commonarea.id,
                        house_id: globalCompany.getHouseId(),
                    }
                    console.log(a)
                    CommonAreaReservations.isAvailableToReserve(a, onSuccessIsAvailable, onError);


                }

            }

        };


        vm.checkAvailabilityBlocks = function () {

            $("#loadingAvailability").fadeIn('0');
            if (vm.commonAreaReservations.id != null) {
                vm.commonAreaReservations.initalDate.setHours(0);
                vm.commonAreaReservations.initalDate.setMinutes(0);
                CommonAreaReservations.isAvailableToReserveNotNull({
                    maximun_hours: vm.commonarea.maximunHours,
                    reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                    initial_time: vm.timeSelected.initialTime.value,
                    final_time: vm.timeSelected.finalTime.value,
                    common_area_id: vm.commonarea.id,
                    house_id: globalCompany.getHouseId(),
                    reservation_id: vm.commonAreaReservations.id
                }, onSuccessIsAvailable, onError);
            } else {
                CommonAreaReservations.isAvailableToReserve({
                    maximun_hours: vm.commonarea.maximunHours,
                    reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                    initial_time: vm.timeSelected.initialValue,
                    final_time: vm.timeSelected.finalValue,
                    common_area_id: vm.commonarea.id,
                    house_id: globalCompany.getHouseId(),
                }, onSuccessIsAvailable, onError);


            }

        };

        function isTheDayInSchedule(day) {
            var isContained = false;

            angular.forEach(vm.schedule, function (item, key) {

                if (item.numberDay == day) {
                    isContained = true;
                    vm.daySelected = item;
                }
            });
            if (isContained) {
                return true;
            } else {
                return false;
            }

        }

        vm.checkAvailability = function () {
            vm.dateNotPermited = false;
            vm.hours = [];
            vm.timeSelected = null;
            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;
            vm.commonAreaReservations.initalDate.setHours(0);
            vm.commonAreaReservations.initalDate.setMinutes(0);
            if (isTheDayInSchedule(vm.commonAreaReservations.initalDate.getDay())) {
                if (vm.commonarea.maximunHours === 0 && vm.commonarea.hasBlocks == 0) {
                    $("#loadingAvailability").fadeIn('50');
                    var initialTime = "0";
                    var finalTime = "0";
                    if (vm.commonarea.maximunHours !== 0) {
                        initialTime = vm.timeSelected.initialTime.value;
                        finalTime = vm.timeSelected.finalTime.value;
                    }
                    if (vm.commonAreaReservations.id != null) {
                        CommonAreaReservations.isAvailableToReserveNotNull({
                            maximun_hours: vm.commonarea.maximunHours,
                            reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                            initial_time: initialTime,
                            final_time: finalTime,
                            common_area_id: vm.commonarea.id,
                            house_id: globalCompany.getHouseId(),
                            reservation_id: vm.commonAreaReservations.id
                        }, onSuccessIsAvailable, onError);
                    } else {
                        CommonAreaReservations.isAvailableToReserve({
                            maximun_hours: vm.commonarea.maximunHours,
                            reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                            initial_time: initialTime,
                            final_time: finalTime,
                            common_area_id: vm.commonarea.id,
                            house_id: globalCompany.getHouseId(),
                        }, onSuccessIsAvailable, onError);
                    }


                } else if (vm.commonarea.maximunHours > 0 && vm.commonarea.hasBlocks == 0) {
                    addHoursToSelect()
                } else if (vm.commonarea.hasBlocks == 1) {
                    addBlocksToSelect()
                }

            } else {

                $("#loadingAvailability").fadeOut('50');
                vm.dateNotPermited = true;
                Modal.toast("No se permite reservar el día " + vm.diasDeLaSemana[vm.commonAreaReservations.initalDate.getDay()] + " en esta área común")
            }


        };
        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        };

        function onSuccessIsAvailable(data) {
            vm.dateNotPermited = false;
            $("#loadingAvailability").fadeOut('50');
            console.log(data.availability);
            vm.availability = data.availability;
            showErrorMessage(data.availability);
            if (data.availability == 0) {
                vm.scheduleIsAvailable = true;
                vm.scheduleNotAvailable = false;
            } else {
                if (vm.commonAreaReservations.id != null || vm.commonAreaReservations.id != undefined) {
                    if (vm.commonarea.maximunHours == 0 && initialDateTemporal.getMonth() == vm.commonAreaReservations.initalDate.getMonth() && initialDateTemporal.getFullYear() == vm.commonAreaReservations.initalDate.getFullYear() && initialDateTemporal.getDate() == vm.commonAreaReservations.initalDate.getDate()) {
                        vm.scheduleIsAvailable = true;
                        vm.scheduleNotAvailable = false;

                    } else {

                        vm.scheduleIsAvailable = false;
                        vm.scheduleNotAvailable = true;
                    }
                } else {
                    vm.scheduleIsAvailable = false;
                    vm.scheduleNotAvailable = true;
                }

            }
        }

        function addBlocksToSelect() {
            vm.hours = [];

            vm.commonAreaReservations.initalDate.setHours(0);
            vm.commonAreaReservations.initalDate.setMinutes(0);
            var initialTime = vm.commonAreaReservations.initalDate;
            vm.commonAreaReservations.initalDate.setHours(23);
            vm.commonAreaReservations.initalDate.setMinutes(59);
            var finalTime = vm.commonAreaReservations.initalDate;
            console.log("1341")
            console.log(initialTime)
            console.log(finalTime)
            CommonAreaReservations.findBetweenDatesByCommonArea({
                initial_time: moment(initialTime).format(),
                final_time: moment(finalTime).format(),
                commonAreaId: vm.commonarea.id,
                page: 0,
                size: 500
            }, onSuccess, onError);

            function onSuccess(data) {
                var arreglo = vm.daySelected.times;
                angular.forEach(arreglo, function (block, index) {
                    block.isAvailable = "";
                    block.disabled = false;
                });
                angular.forEach(arreglo, function (block, index) {
                    console.log(block)
                    angular.forEach(data, function (reservation, index) {
                        if (reservation.initialTime == block.initialValue) {
                            block.isAvailable = " - RESERVADO";
                            block.disabled = true;
                        }
                        console.log(reservation)
                    });
                    vm.hours.push(block)

                });
            }


        }

        function addHoursToSelect() {
            vm.hours = [];
            var id = 0;
            for (var i = parseInt(vm.daySelected.initialValue); i <= parseInt(vm.daySelected.finalValue); i++) {
                if (i == 0) {
                    var item = {
                        value: 0,
                        time: '12:00AM',
                        id: id + 1
                    };
                    vm.hours.push(item);
                } else if (i < 12) {
                    var item = {
                        value: i,
                        time: i + ':00AM',
                        id: id + 1
                    };
                    vm.hours.push(item);
                } else if (i == 12) {
                    var item = {
                        value: 12,
                        time: '12:00PM',
                        id: id + 1
                    };
                    vm.hours.push(item);
                } else if (i > 12) {
                    var item = {
                        value: i,
                        time: i - 12 + ':00PM',
                        id: id + 1
                    };
                    vm.hours.push(item);
                }

            }

            vm.commonAreaReservations.initalDate.setHours(0);
            vm.commonAreaReservations.initalDate.setMinutes(0);
            var initialTime = vm.commonAreaReservations.initalDate;
            vm.commonAreaReservations.initalDate.setHours(23);
            vm.commonAreaReservations.initalDate.setMinutes(59);
            var finalTime = vm.commonAreaReservations.initalDate;
            CommonAreaReservations.findBetweenDatesByCommonArea({
                initial_time: moment(initialTime).format(),
                final_time: moment(finalTime).format(),
                commonAreaId: vm.commonarea.id,
                page: 0,
                size: 500
            }, onSuccess, onError);

            function onSuccess(data) {
                angular.forEach(vm.hours, function (block, index) {
                    angular.forEach(data, function (reservation, index) {
                        if (parseInt(reservation.finalTime) > block.value && parseInt(reservation.initialTime) <= block.value) {
                            block.isAvailable = " - RESERVADO";
                            block.disabled = true;
                        }
                    });


                });
            }


            if (vm.commonAreaReservations.id != null || vm.commonAreaReservations.id != undefined) {
                angular.forEach(vm.hours, function (item, index) {

                    if (item.value == vm.commonAreaReservations.initialTime) {
                        vm.timeSelected.initialTime = vm.hours[index];
                        vm.validateDaysInitialHours(vm.timeSelected.initialTime, index);
                    }
                    if (item.value == vm.commonAreaReservations.finalTime) {
                        vm.timeSelected.finalTime = vm.hours[index];

                    }


                });

            }

        }


        vm.residentsByHouse = function () {
            Resident.findResidentesEnabledByHouseId({
                houseId: globalCompany.getHouseId()
            }).$promise.then(onSuccessResidents, onError);

        };

        function onSuccessResidents(data) {

            angular.forEach(data, function (value, key) {
                value.name = value.name + " " + value.lastname + " " + value.secondlastname;
            });
            vm.residents = data;
            House.get({
                id: globalCompany.getHouseId()
            }, function (result) {
                vm.houseSelected = result;

            })
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function onError(error) {
            AlertService.error(error.data.message);
            Modal.toast("Ocurrio un error inesperado.")
        }

        function onErrorSchedule(error) {
            AlertService.error(error.data.message);

        }


        function createReservation(commonAreaReservations) {
            Modal.showLoadingBar()
            vm.isSaving = true;
            vm.commonAreaReservations.sendPendingEmail = true;
            vm.commonAreaReservations.reservationCharge = vm.commonarea.reservationCharge;
            vm.commonAreaReservations.devolutionAmmount = vm.commonarea.devolutionAmmount;
            vm.commonAreaReservations.commonAreaId = vm.commonarea.id;
            vm.commonAreaReservations.paymentProof = commonAreaReservations;
            vm.commonAreaReservations.houseId = globalCompany.getHouseId();
            vm.commonAreaReservations.companyId = globalCompany.getId();
            vm.commonAreaReservations.status = 1;
            if (vm.commonarea.maximunHours == 0 && vm.commonarea.hasBlocks == 0) {
                vm.commonAreaReservations.initialTime = vm.daySelected.initialValue;
                vm.commonAreaReservations.finalTime = vm.daySelected.finalValue;
            } else if (vm.commonarea.maximunHours > 0 && vm.commonarea.hasBlocks == 0) {
                vm.commonAreaReservations.initialTime = vm.timeSelected.initialTime.value;
                vm.commonAreaReservations.finalTime = vm.timeSelected.finalTime.value;
                vm.commonAreaReservations.initalDate.setHours(0);
                vm.commonAreaReservations.initalDate.setMinutes(0);
            } else if (vm.commonarea.hasBlocks == 1) {
                vm.commonAreaReservations.initialTime = vm.timeSelected.initialValue;
                vm.commonAreaReservations.finalTime = vm.timeSelected.finalValue;
                vm.commonAreaReservations.initalDate.setHours(0);
                vm.commonAreaReservations.initalDate.setMinutes(0);
            }
            if (vm.commonAreaReservations.id !== null) {
                vm.commonAreaReservations.initalDate = new Date(vm.commonAreaReservations.initalDate)
                vm.commonAreaReservations.initalDate.setHours(0);
                vm.commonAreaReservations.initalDate.setMinutes(0);
                vm.commonAreaReservations.sendPendingEmail = false;
                CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);
            } else {

                vm.commonAreaReservations.status = 1;
                vm.commonAreaReservations.houseId = globalCompany.getHouseId();
                vm.commonAreaReservations.companyId = globalCompany.getId();
                if (vm.commonarea.chargeRequired == 0) {
                    vm.commonAreaReservations.reservationCharge = null;
                }
                CommonAreaReservations.save(vm.commonAreaReservations, onSaveSuccess, onSaveError);
            }


        }


        function onSaveSuccess(result) {
            Modal.hideLoadingBar()

            $state.go('common-area-resident-account');
            Modal.toast("Se ha enviado la reservación correctamente para su respectiva aprobación")
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
            Modal.hideLoadingBar()
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.picker = {
            datepickerOptions: {
                minDate: moment().subtract(1, 'days').startOf(new Date()),
                enableTime: false,
                showWeeks: false,
                daysOfWeekDisabled: [0, 1, 2, 3, 4, 5, 6],
                clearBtn: false,
                todayBtn: false
            }
        }

        function confirmMessage() {

            if (vm.scheduleIsAvailable) {
                if (vm.commonarea.maximunHours == 0) {
                    vm.time = "Todo el día"
                } else if (vm.commonarea.maximunHours > 0 && vm.commonarea.hasBlocks == 0) {
                    vm.time = vm.timeSelected.initialTime.time + " - " + vm.timeSelected.finalTime.time;
                } else if (vm.commonarea.hasBlocks == 1) {
                    console.log(vm.timeSelected)
                    vm.time = vm.timeSelected.time;
                }

                bootbox.confirm({
                    message: '<div class="text-center gray-font font-15"><h3 style="margin-bottom:30px;">¿Está seguro que desea enviar la solicitud de reservación?</h3><h4>Área común: <span class="bold" id="commonArea"></span> </h4><h4>Día: <span class="bold" id="reservationDate"></span> </h4><h4>Hora: <span class="bold" id="time"></span> </h4></div>',
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
                            if (vm.houseSelected.balance.total < 0 && vm.commonarea.reservationWithDebt == 2) {
                                vm.houseWithDebts = true;
                                Modal.toast("Esta filial cuenta con deudas pendientes por lo que no puede crear reservaciones.")
                            } else {
                                vm.paymentProofId = null
                                if (vm.file) {
                                    upload();
                                } else {
                                    createReservation(null)
                                }
                            }
                        } else {
                            vm.isSaving = false;

                        }
                    }
                });
                document.getElementById("commonArea").innerHTML = vm.commonarea.name;
                document.getElementById("reservationDate").innerHTML = vm.diasDeLaSemana[vm.commonAreaReservations.initalDate.getDay()] + " " + vm.commonAreaReservations.initalDate.getDate() + " de " + vm.mesesDelAnno[vm.commonAreaReservations.initalDate.getMonth()] + " de " + vm.commonAreaReservations.initalDate.getFullYear();
                document.getElementById("time").innerHTML = vm.time;

            } else {

                if (vm.commonarea.maximunHours !== 0) {
                    if (vm.timeSelected.finalTime.isValid == false) {
                        Modal.toast("Debe seleccionar una hora final posterior a la hora anterior");
                    } else {
                        Modal.toast("Las horas seleccionadas se encuentran ocupadas para reservar.")
                    }

                } else {
                    Modal.toast("Las horas seleccionadas se encuentran ocupadas para reservar.")
                }

            }

        };
        vm.validateReservationCharge = function (commonArea) {
            var s = commonArea.reservationCharge;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase() || s == undefined) {
                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                commonArea.reservationChargeValida = true;
            } else {
                commonArea.reservationChargeValida = false
            }
        }

        vm.validateDevolutionAmmount = function (commonArea) {
            var s = commonArea.devolutionAmmount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase() || s == undefined) {
                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                commonArea.devolutionAmmountValida = true;
            } else {
                commonArea.devolutionAmmountValida = false
            }
        }
        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.paymentDate = false;
        vm.datePickerOpenStatus.expirationDate = false;
        vm.setFile = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                vm.file = $file;
                vm.fileName = vm.file.name;
                file = $file;
            }
        };

    }
})();
