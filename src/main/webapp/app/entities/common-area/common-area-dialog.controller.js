(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDialogController', CommonAreaDialogController);

    CommonAreaDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'entity', 'CommonArea','CommonMethods','CommonAreaSchedule','$state','$rootScope','Principal'];

    function CommonAreaDialogController ($timeout, $scope, $stateParams, DataUtils, entity, CommonArea,CommonMethods,CommonAreaSchedule,$state,$rootScope,Principal) {
        var vm = this;
        $rootScope.active = "reservationAdministration";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.commonArea = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.paymentRequired=false;
        vm.commonArea.reservationChargeValida=true;
        vm.commonArea.devolutionAmmountValida=true;
        if (vm.commonArea.id == null) {
            vm.commonArea.devolutionAmmount = 0;
        }


        CommonMethods.validateNumbers();
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        vm.daysOfWeek = [{day:'Lunes',selected:false,initialTime:"",finalTime:""},{day:'Martes',selected:false,initialTime:"",finalTime:""},{day:'Miercoles',selected:false,initialTime:"",finalTime:""},{day:'Jueves',selected:false,initialTime:"",finalTime:""},{day:'Viernes',selected:false,initialTime:"",finalTime:""},{day:'Sábado',selected:false,initialTime:"",finalTime:""},{day:'Domingo',selected:false,initialTime:"",finalTime:""}];
        vm.bloques = [{value:1,hour:"1 hora"},{value:2,hour:"2 horas"},{value:3,hour:"3 horas"},{value:4,hour:"4 horas"},{value:5,hour:"5 horas"},{value:6,hour:"6 horas"},{value:7,hour:"7 horas"},{value:8,hour:"8 horas"}]
        vm.hours = [];
        addHourseToSelect();
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

            if (vm.commonArea.id !== null) {
                vm.button="Editar";
                var autorizadorStatus = vm.commonArea.chargeRequired;
                if( vm.commonArea.chargeRequired==1){

                    vm.paymentRequired=true;
                    vm.paymentRequiredUpdate=1;
                }else{
                    vm.paymentRequiredUpdate=2;
                }
                if( vm.commonArea.reservationWithDebt==1){
                    vm.reservationWithDebts = 1;

                }else{
                    vm.reservationWithDebts = 2;
                }
                loadSchedule();
            } else {
                vm.paymentRequiredUpdate=3;
                vm.reservationWithDebts = 3;
                vm.button="Registrar";
            }
        }



        function loadSchedule(){
            CommonAreaSchedule.findSchedulesByCommonArea({
                commonAreaId: vm.commonArea.id
            }, onSuccessSchedule);

        }
        function onSuccessSchedule(data) {

              vm.scheduleId = data[0].id;
            if (data[0].lunes !== "-") {
                vm.daysOfWeek[0].selected=true;
                vm.lunesSelected=true;
                var times = data[0].lunes.split("-");
                console.log(times)
                vm.daysOfWeek[0].initialTime = parseInt(times[0]);
                vm.daysOfWeek[0].finalTime =  parseInt(times[1]);

            }
            if (data[0].martes !== "-") {
                vm.daysOfWeek[1].selected=true;
                vm.martesSelected = true;
                var times = data[0].martes.split("-");
                vm.daysOfWeek[1].initialTime = parseInt(times[0]);
                vm.daysOfWeek[1].finalTime =  parseInt(times[1]);
            }
            if (data[0].miercoles !== "-") {
                vm.daysOfWeek[2].selected=true;
                vm.miercolesSelected = true;
                var times = data[0].miercoles.split("-");
                vm.daysOfWeek[2].initialTime = parseInt(times[0]);
                vm.daysOfWeek[2].finalTime =  parseInt(times[1]);
            }
            if (data[0].jueves !== "-") {
                vm.daysOfWeek[3].selected=true;
                vm.juevesSelected = true;
                var times = data[0].jueves.split("-");
                vm.daysOfWeek[3].initialTime = parseInt(times[0]);
                vm.daysOfWeek[3].finalTime =  parseInt(times[1]);

            }
            if (data[0].viernes !== "-") {
                vm.daysOfWeek[4].selected=true;
                vm.viernesSelected = true;
                var times = data[0].viernes.split("-");
                vm.daysOfWeek[4].initialTime = parseInt(times[0]);
                vm.daysOfWeek[4].finalTime =  parseInt(times[1]);
            }
            if (data[0].sabado !== "-") {
                vm.daysOfWeek[5].selected=true;
                vm.sabadoSelected = true;
                var times = data[0].sabado.split("-");
                vm.daysOfWeek[5].initialTime = parseInt(times[0]);
                vm.daysOfWeek[5].finalTime =  parseInt(times[1]);
            }
            if (data[0].domingo !== "-") {
                vm.daysOfWeek[6].selected=true;
                vm.domingoSelected = true;
                var times = data[0].domingo.split("-");
                vm.daysOfWeek[6].initialTime = parseInt(times[0]);
                vm.daysOfWeek[6].finalTime =  parseInt(times[1]);
            }
        }

        vm.selectDay = function(index){
            vm.spaceInvalid3=false;
            vm.daysOfWeek[index].selected = !vm.daysOfWeek[index].selected;
            if(vm.daysOfWeek[index].selected==false){
                vm.daysOfWeek[index].initialTime = "";
                vm.daysOfWeek[index].finalTime = "";
            }
        }
        vm.validateDaysHours =function (index,item) {

            if(item.initialTime!==undefined && item.finalTime!==undefined){
                if(parseInt(item.initialTime)>=parseInt(item.finalTime)){
                    setTimeout(function () {
                        $scope.$apply(function () {
                            item.isValid=false;
                        });
                    }, 200);
                        toastr["error"]("Debe seleccionar una hora final posterior a la hora anterior");


                }else{
                      item.isValid=true;

                }
            }



        };

        function validateForm () {
            if(vm.commonArea.reservationChargeValida && vm.commonArea.devolutionAmmountValida){
                if(vm.commonArea.chargeRequired==null || vm.commonArea.reservationWithDebt==null){
                    if(vm.commonArea.chargeRequired==null){
                        setTimeout(function () {
                            $scope.$apply(function () {
                                vm.spaceInvalid1=true;
                            });
                        }, 200);

                    }
                    if(vm.commonArea.reservationWithDebt==null){
                        setTimeout(function () {
                            $scope.$apply(function () {
                                vm.spaceInvalid2=true;
                            });
                        }, 200);

                    }
                    toastr["error"]("Debe completar los campos obligatorios");

                }else if(!vm.isAnyDaySelected()){
                    setTimeout(function () {
                        $scope.$apply(function () {
                            vm.spaceInvalid3=true;
                        });
                    }, 200);

                    toastr["error"]("Debe seleccionar al menos un día permitido para reservar");
                }else{
                    if(vm.isAllHoursValid()==false){
                        toastr["error"]("Debe corregir las horas permitidas para reservar");{}

                    }else{
                        save();
                    }
                }
            }else{
                toastr["error"]("No se permite agregar carácteres especiales");

            }

        }

        function save() {
            CommonMethods.waitingMessage();
            if (vm.commonArea.id !== null) {
                if(vm.commonArea.maximunHours==null ||vm.commonArea.maximunHours===""){
                    vm.commonArea.maximunHours = 0;
                }
                console.log(vm.commonArea)
                CommonArea.update(vm.commonArea, onSaveSuccess, onSaveError);
            } else {
                if(vm.commonArea.maximunHours==null ||vm.commonArea.maximunHours===""){
                    vm.commonArea.maximunHours = 0;
                }
                vm.commonArea.companyId = $rootScope.companyId;
                vm.commonArea.deleted = 0;
                CommonArea.save(vm.commonArea, onSaveSuccess, onSaveError);


            }

            }

        vm.isAnyDaySelected = function () {

            var selectedDays = 0;
            angular.forEach(vm.daysOfWeek, function (item, key) {
                if(item.selected){
                    selectedDays++;
                }

            })
            if(selectedDays>0){

                return true;
            }else{

                return false;
            }
        }
        vm.isAllHoursValid = function () {

            var invalid = 0;
            angular.forEach(vm.daysOfWeek, function (item, key) {
                if(item.isValid==false){
                    invalid++;
                }


            })
            if(invalid>0){

                return false;
            }else{

                return true;
            }
        }

        function onSaveSuccess (result) {
            var commonAreaScheadule ={};
            commonAreaScheadule.lunes = vm.daysOfWeek[0].initialTime + "-" + vm.daysOfWeek[0].finalTime;
            commonAreaScheadule.martes = vm.daysOfWeek[1].initialTime + "-" + vm.daysOfWeek[1].finalTime;
            commonAreaScheadule.miercoles = vm.daysOfWeek[2].initialTime + "-" + vm.daysOfWeek[2].finalTime;
            commonAreaScheadule.jueves = vm.daysOfWeek[3].initialTime + "-" + vm.daysOfWeek[3].finalTime;
            commonAreaScheadule.viernes = vm.daysOfWeek[4].initialTime + "-" + vm.daysOfWeek[4].finalTime;
            commonAreaScheadule.sabado = vm.daysOfWeek[5].initialTime + "-" + vm.daysOfWeek[5].finalTime;
            commonAreaScheadule.domingo = vm.daysOfWeek[6].initialTime + "-" + vm.daysOfWeek[6].finalTime;
            commonAreaScheadule.commonAreaId = result.id;
            if (vm.commonArea.id !== null) {
                commonAreaScheadule.id = vm.scheduleId;
                console.log(commonAreaScheadule.id)
                CommonAreaSchedule.update(commonAreaScheadule, onSaveScheduleSuccess, onSaveError);
            } else {
                CommonAreaSchedule.save(commonAreaScheadule, onSaveScheduleSuccess, onSaveError);
            }



        }
        function onSaveScheduleSuccess (result) {
            bootbox.hideAll();
            $state.go('common-area-administration.common-area');
            toastr["success"]("Se ha gestionado el área común correctamente.")

            vm.isSaving = false;
        }
        function onSaveError () {
            bootbox.hideAll();
            vm.isSaving = false;
        }

        vm.setPicture = function ($file, commonArea) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        commonArea.picture = base64Data;
                        commonArea.pictureContentType = $file.type;
                    });
                });
            }
        };

        vm.changeChargeRequired = function(option){
            vm.spaceInvalid1=false;
            if(option){
                vm.paymentRequired=true; vm.commonArea.chargeRequired=1
            }else{
                vm.paymentRequired=false;  vm.commonArea.chargeRequired=0
            }


        }

        vm.validateReservationCharge = function(commonArea) {
            var s = commonArea.reservationCharge;
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
            })
            if(invalido==0) {
                commonArea.reservationChargeValida = true;
            } else {
                commonArea.reservationChargeValida = false
            }
        }

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
            })
            if (invalido == 0) {
                commonArea.devolutionAmmountValida = true;
            } else {
                commonArea.devolutionAmmountValida = false
            }
        }
        vm.confirmMessage = function() {
            bootbox.confirm({
                message: '<div class="text-center gray-font font-15"><h3 style="margin-bottom:10px;">¿Está seguro que desea registrar el área común?</h3></div>',
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
                        validateForm()

                    } else {
                        vm.isSaving = false;

                    }
                }
            });
        }

    }
})();
