(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsDialogController', CommonAreaReservationsDialogController);

    CommonAreaReservationsDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'CommonAreaReservations', 'CommonArea','$rootScope','House','Resident','CommonAreaSchedule','AlertService','$state'];

    function CommonAreaReservationsDialogController ($timeout, $scope, $stateParams, entity, CommonAreaReservations, CommonArea,$rootScope,House,Resident,CommonAreaSchedule,AlertService,$state) {
        var vm = this;

        vm.commonAreaReservations = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.required=1;
        vm.hours = [];
        vm.commonareas = CommonArea.query();
        vm.allDaySchedule = 1;
        vm.scheduleIsAvailable = false;
        vm.diasDeLaSemana = ['domingo','lunes','martes','miércoles','jueves','viernes','sábado',''];
        vm.mesesDelAnno = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Setiembre','Octubre','Noviembre','Diciembre'];
        vm.timeSelected = {};
        setTimeout(function(){
            loadHouses();
        },1500)

        function loadHouses(){
            House.query({
                companyId: $rootScope.companyId
            }, onSuccessHouses, onError);

        }
        function onSuccessHouses(data, headers) {
            angular.forEach(data,function(value,key){
                value.housenumber = parseInt(value.housenumber);
                if(value.housenumber==9999){
                    value.housenumber="Oficina"
                }
            });
            vm.houses = data;
            addHourseToSelect();
        }

        vm.loadSchedule = function () {
            vm.commonAreaReservations.initalDate = null;
            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;
            $("#scheduleDiv").fadeOut(50);
            $("#loadingSchedule").fadeIn('0');
            if(vm.commonarea===undefined){
                $("#loadingSchedule").fadeOut('20');
            }else{
                CommonAreaSchedule.findSchedulesByCommonArea({commonAreaId:vm.commonarea.id},onSuccessSchedule, onErrorSchedule);
            }

        }
        function onSuccessSchedule(data, headers) {
           vm.schedule = [];
           if(data[0].lunes!=="-"){
                formatScheduleTime("Lunes",data[0].lunes,1)
           }
           if(data[0].martes!=="-"){
               formatScheduleTime("Martes",data[0].martes,2)
           }
            if(data[0].miercoles!=="-"){
                formatScheduleTime("Miércoles",data[0].miercoles,3)
            }
            if(data[0].jueves!=="-"){
                formatScheduleTime("Jueves",data[0].jueves,4)
            }
            if(data[0].viernes!=="-"){
                formatScheduleTime("Viernes",data[0].viernes,5)
            }
            if(data[0].sabado!=="-"){

                formatScheduleTime("Sábado",data[0].sabado,6)
            }
            if(data[0].domingo!=="-"){
                formatScheduleTime("Domingo",data[0].domingo,0)
            }
            if(vm.commonarea.maximunHours==0){
                vm.maximunHoursTitle = "Tiempo de uso:";
                vm.maximunHours = " Todo el día";
                vm.allDaySchedule = 2;
            }else{
                vm.maximunHoursTitle =  "Tiempo máximo de uso: " ;
                vm.maximunHours = vm.commonarea.maximunHours + " horas";
                vm.allDaySchedule = 1;
            }

             $("#scheduleDiv").fadeIn('50');
            $("#loadingSchedule").fadeOut('20');
        }

        function formatScheduleTime(day,time,number){
            var item = {};
            item.day = day;
            item.numberDay = number;
            var times = time.split("-");
            item.initialValue = times[0];
            item.finalValue = times[1];
            if(times[0]>12){
              item.initialTime = parseInt(times[0])-12 +":00PM"
            }else{
                if(times[0]==0){
                    item.initialTime = "12:00AM"
                }else{
                    item.initialTime = parseInt(times[0]) +":00AM"
                }

            }
            if(times[1]>12){
                item.finalTime = parseInt(times[1])-12 +":00PM"
            }else{
                item.finalTime = parseInt(times[1]) +":00AM"
            }
            item.time = item.initialTime + " - " + item.finalTime;
            vm.schedule.push(item);

        }

        vm.validateDaysHours = function(index,hours){


        };


        function isTheDayInSchedule(day){
            var isContained = false;
            angular.forEach(vm.schedule,function(item,key){

                if(item.numberDay==day){
                    isContained = true;
                    vm.daySelected = item;
                }
            });
            if(isContained){
                return true;
            }else{
                return false;
            }

        }
        vm.checkAvailability = function () {
            vm.scheduleIsAvailable = false;
            vm.scheduleNotAvailable = false;
            if(vm.commonarea.maximunHours!==0 && vm.timeSelected.initialTime.value!==undefined &&  vm.timeSelected.finalTime.value!==undefined || vm.commonarea.maximunHours==0){
                $("#loadingAvailability").fadeIn('50');
                if(isTheDayInSchedule(vm.commonAreaReservations.initalDate.getDay())){
                    var initialTime = "0";
                    var finalTime = "0";
                    if(vm.commonarea.maximunHours!==0){
                        initialTime = vm.timeSelected.initialTime.value;
                        finalTime = vm.timeSelected.finalTime.value;
                    }
                    CommonAreaReservations.isAvailableToReserve({
                        maximun_hours: vm.commonarea.maximunHours,
                        reservation_date: moment(vm.commonAreaReservations.initalDate).format(),
                        initial_time: initialTime,
                        final_time: finalTime,
                        common_area_id: vm.commonarea.id

                    }).$promise.then(onSuccessIsAvailable, onError);


                }else{
                    $("#loadingAvailability").fadeOut('50');
                    toastr["error"]("No se permite reservar el día " + vm.diasDeLaSemana[vm.commonAreaReservations.initalDate.getDay()] + " en esta área común")
                }
            }



        };
        function onSuccessIsAvailable(data) {
            $("#loadingAvailability").fadeOut('50');

            if(data.available){
                vm.scheduleIsAvailable = true;
                vm.scheduleNotAvailable = false;
            }else{
                vm.scheduleIsAvailable = false;
                vm.scheduleNotAvailable = true;
            }

        }
        function addHourseToSelect(){
            var item = {value:'0',time:'12:00AM'};
            vm.hours.push(item);
            for (var i=1;i<12;i++){
                var item = {value:i,time:i+':00AM'};
                vm.hours.push(item);
            }
            var item2 = {value:'12',time:'12:00PM'};
            vm.hours.push(item2);
            for (var i=1;i<12;i++){
                var item = {value:i+12,time:i+':00PM'};
                vm.hours.push(item);
            }
        }

        vm.residentsByHouse = function(){
            Resident.findResidentesEnabledByHouseId({
                houseId: vm.commonAreaReservations.houseId
            }).$promise.then(onSuccessResidents, onError);

        }
        function onSuccessResidents(data) {
            angular.forEach(data,function(value,key){
              value.name = value.name + " " + value.lastname + " " + value.secondlastname;
            })
            vm.residents = data;

        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function onError(error) {
            AlertService.error(error.data.message);
            toastr["error"]("Ocurrio un error inesperado.")
        }
        function onErrorSchedule(error) {
            AlertService.error(error.data.message);

        }
        function save () {


        }
        function createReservation(){
            vm.isSaving = true;
            vm.commonAreaReservations.reservationCharge = vm.commonarea.reservationCharge;
            vm.commonAreaReservations.commonAreaId = vm.commonarea.id;
                if(vm.commonarea.maximunHours==0){
                    vm.commonAreaReservations.initialTime = vm.daySelected.initialValue;
                    vm.commonAreaReservations.finalTime = vm.daySelected.finalValue;
                }else{
                    vm.commonAreaReservations.initialTime = vm.timeSelected.initialTime.value;
                    vm.commonAreaReservations.finalTime = vm.timeSelected.finalTime.value;
                }
                if (vm.commonAreaReservations.id !== null) {
                    CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);
                } else {
                    vm.commonAreaReservations.status = 1;
                    CommonAreaReservations.save(vm.commonAreaReservations, onSaveSuccess, onSaveError);
                }


        }
        function onSaveSuccess (result) {
            $state.go('common-area-administration.common-area-reservations');
            toastr["success"]("Se ha enviado la reservación correctamente para su respectiva aprobación")
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
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
        }

        vm.confirmMessage = function() {
            if(vm.scheduleIsAvailable){
                if(vm.commonarea.maximunHours==0){
                    vm.time = "Todo el día"
                }else{
                    vm.time = vm.timeSelected.initialTime.time + " - " + vm.timeSelected.finalTime.time;
                }
                bootbox.confirm({
                    message: '<div class="text-center gray-font font-15"><h3 style="margin-bottom:30px;">¿Está seguro que desea registrar el área común?</h3><h4>Área común: <span class="bold" id="commonArea"></span> </h4><h4>Día: <span class="bold" id="reservationDate"></span> </h4><h4>Hora: <span class="bold" id="time"></span> </h4></div>',
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
                            createReservation()

                        } else {
                            vm.isSaving = false;

                        }
                    }
                });
                document.getElementById("commonArea").innerHTML = vm.commonarea.name;
                document.getElementById("reservationDate").innerHTML = vm.diasDeLaSemana[vm.commonAreaReservations.initalDate.getDay()] + " " + vm.commonAreaReservations.initalDate.getDate() + " de " + vm.mesesDelAnno[vm.commonAreaReservations.initalDate.getMonth()] + " de " + vm.commonAreaReservations.initalDate.getFullYear();
                document.getElementById("time").innerHTML = vm.time;

            }else{
                toastr["error"]("Las horas seleccionadas se encuentran ocupadas para reservar.")
            }
        }
        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.paymentDate = false;
        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
