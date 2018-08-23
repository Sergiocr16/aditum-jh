(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDialogController', CommonAreaDialogController);

    CommonAreaDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'entity', 'CommonArea','CommonMethods'];

    function CommonAreaDialogController ($timeout, $scope, $stateParams, DataUtils, entity, CommonArea,CommonMethods) {
        var vm = this;

        vm.commonArea = entity;

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.paymentRequired=false;
        vm.commonArea.reservationChargeValida=true;
        vm.commonArea.devolutionAmmountValida=true;
        vm.commonArea.devolutionAmmount = 0;
        CommonMethods.validateNumbers();
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        vm.daysOfWeek = [{day:'Lunes',selected:false},{day:'Martes',selected:false},{day:'Miercoles',selected:false},{day:'Jueves',selected:false},{day:'Viernes',selected:false},{day:'Sábado',selected:false},{day:'Domingo',selected:false}];
        vm.bloques = [{value:1,hour:"1 hora"},{value:2,hour:"2 horas"},{value:3,hour:"3 horas"},{value:4,hour:"4 horas"},{value:5,hour:"5 horas"},{value:6,hour:"6 horas"},{value:7,hour:"7 horas"},{value:8,hour:"8 horad"}]
        vm.hours = [];
        addHourseToSelect();
        function addHourseToSelect(){
            var item = {value:'0',time:'12:00AM'};
            vm.hours.push(item);
            for (var i=1;i<12;i++){
                var item = {value:i+'00',time:i+':00AM'};
                vm.hours.push(item);
            }
            var item2 = {value:'1200',time:'12:00PM'};
            vm.hours.push(item2);
            for (var i=1;i<12;i++){
                var item = {value:i+12+'00',time:i+':00PM'};
                vm.hours.push(item);
            }
        }

        if (vm.commonArea.id !== null) {
                vm.button="Editar";
        } else {
            vm.button="Registrar";
        }

        vm.selectDay = function(index){
            vm.spaceInvalid3=false;
            vm.daysOfWeek[index].selected = !vm.daysOfWeek[index].selected;
        }
        vm.validateDaysHours =function (index,item) {
            console.log(item)


            if(item.initialTime!==undefined && item.finalTime!==undefined){
                console.log('a333df')
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



        }
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
                    save();
                }
            }else{
                toastr["error"]("No se permite agregar carácteres especiales");

            }

        }

        function save() {
            if (vm.commonArea.id !== null) {
                CommonArea.update(vm.commonArea, onSaveSuccess, onSaveError);
            } else {
                if(vm.commonArea.maximunHours==null ||vm.commonArea.maximunHours===""){
                    vm.commonArea.maximunHours = 0;
                }
                vm.commonArea.daysOfWeek = vm.daysOfWeek;
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
        function onSaveSuccess (result) {

            vm.isSaving = false;
        }

        function onSaveError () {
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
                message: '<div class="text-center gray-font font-15"><h3 style="margin-bottom:30px;">¿Está seguro que desea registrar este egreso?</h3><h5 class="bold">Una vez registrada esta información no se podrá editar</h5></div>',
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
