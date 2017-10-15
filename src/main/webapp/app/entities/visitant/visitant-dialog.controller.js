(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDialogController', VisitantDialogController);

    VisitantDialogController.$inject = ['$state','$timeout', '$interval', '$scope', '$stateParams', 'Visitant', 'House', 'Company', 'Principal', '$rootScope', 'CommonMethods','WSVisitor','WSDeleteEntity'];

    function VisitantDialogController($state,$timeout, $interval, $scope, $stateParams, Visitant, House, Company, Principal, $rootScope, CommonMethods,WSVisitor,WSDeleteEntity) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        //        vm.visitant = entity;
        vm.clear = clear;
        $rootScope.active = "reportInvitation";
        vm.datePickerOpenStatus = {};
        vm.openCalendarInit = openCalendarInit;
        vm.openCalendarFinal = openCalendarFinal;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();
        vm.dates = {
            initial_time: new Date(),
            final_time: new Date()
        };
        angular.element(document).ready(function() {
            $("#all").fadeIn("slow");
             vm.formatInitPickers();
        });

         vm.formatInitPickers = function(){

            var currentDate = new Date();
//            FECHAS
            vm.dates.initial_date = new Date();
            vm.dates.final_date = new Date();
            vm.minInitialDate = moment(currentDate).format("YYYY-MM-DD")

//            HORAS
            vm.dates.initial_time = new Date(1970, 0, 1,currentDate.getHours(), currentDate.getMinutes(), 0)
            vm.dates.final_time = new Date(1970, 0, 1,currentDate.getHours(), currentDate.getMinutes()+30, 0)
            vm.minInitialTime = moment(new Date(1970, 0, 1,currentDate.getHours(), currentDate.getMinutes(), 0)).format('HH:mm')
            setTimeout(function(){
                vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm')
                vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm')
            },300)
         }

        vm.updateDatePicker = function() {
            vm.initialDateMinMax = moment(vm.dates.initial_date).format("YYYY-MM-DD")
            vm.finalDateMinMax = moment(vm.dates.final_date).format("YYYY-MM-DD")
            console.log(vm.dates.initial_date)
        }

        vm.updateTimePicker = function(){
         vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm')
         vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm')
         console.log(moment("Init " +vm.dates.final_time).format('HH:mm'))
        }

               vm.validate = function(){
                 var invalido = 0;
                function hasWhiteSpace(s) {
                 function tiene(s) {
                       return /\s/g.test(s);
                    }
                    if(tiene(s)||s==undefined){
                     return true
                    }
                   return false;
                 }
               function haswhiteCedula(s){
                return /\s/g.test(s);
               }
                 function hasCaracterEspecial(s){
                 var caracteres = [",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿"]
                 var invalido = 0;
                  angular.forEach(caracteres,function(val,index){
                  if (s!=undefined){
                   for(var i=0;i<s.length;i++){
                   if(s.charAt(i)==val){
                   invalido++;
                   }
                   }
                   }
                  })
                  if(invalido==0){
                  return false;
                  }else{
                  return true;
                  }
                 }
                 if(vm.visitor.name == undefined || vm.visitor.lastname == undefined || vm.visitor.secondlastname == undefined || hasWhiteSpace(vm.visitor.identificationnumber) ||  haswhiteCedula(vm.visitor.licenseplate)){
                    toastr["error"]("No puede ingresar espacios en blanco.");
                    invalido++;
                 }else if(hasCaracterEspecial(vm.visitor.name)|| hasCaracterEspecial(vm.visitor.lastname)|| hasCaracterEspecial(vm.visitor.secondlastname)||hasCaracterEspecial(vm.visitor.identificationnumber) || hasCaracterEspecial(vm.visitor.licenseplate)){
                    invalido++;
                      toastr["error"]("No puede ingresar ningún caracter especial.");
                 }
                  if(invalido==0){
                  return true;
                  }else{
                  return false;
                  }
                }














        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();


        $timeout(function() {
//            angular.element('#focusMe').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.showMessageIntitial = function(){
         toastr["info"]("La fecha y hora inicial debe ser en el futuro, y no puede ser mayor a la fecha final","Toma en consideración");
        }
         vm.showMessageFinal = function(){
         toastr["info"]("La fecha y hora final debe de ser en el futuro, y no puede ser menor a la fecha inicial","Toma en consideración");
         }
        function isValidDates() {
        console.log(vm.dates.final_date)
            function invalidDates() {
                toastr["error"]("Tus fechas no tienen el formato adecuado, intenta nuevamente","Ups!");
                 vm.formatInitPickers()
                 bootbox.hideAll();
                return false;
            }

            if (vm.dates.final_time == undefined || vm.dates.initial_time == undefined || vm.dates.initial_date ==undefined || vm.dates.final_date == undefined) {
                return invalidDates();
            }else{
             if (vm.formatDate(vm.dates.initial_date,vm.dates.initial_time).getTime() >=  vm.formatDate(vm.dates.final_date,vm.dates.final_time).getTime()) {
                     return invalidDates();
              }
            }
            return true;
        }

        vm.formatDate = function(date,time){
         return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), 0, 0);
        }

        function formatVisitor() {
            vm.visitor.isinvited = 1;
            vm.visitor.houseId = $rootScope.companyUser.houseId;
            console.log(vm.dates.initial_date);
            vm.visitor.invitationstaringtime = vm.formatDate(vm.dates.initial_date,vm.dates.initial_time);
            vm.visitor.invitationlimittime = vm.formatDate(vm.dates.final_date,vm.dates.final_time);
            vm.visitor.companyId = $rootScope.companyId;
            if (vm.visitor.licenseplate != undefined) {
                vm.visitor.licenseplate = vm.visitor.licenseplate.toUpperCase();
            }
            if (vm.visitor.identificationnumber != undefined) {
                vm.visitor.identificationnumber = vm.visitor.identificationnumber.toUpperCase();
            }
            if(vm.visitor.licenseplate == ""){
            vm.visitor.licenseplate = undefined;
            }
        }

        function save() {
        if(vm.validate()){
               CommonMethods.waitingMessage();
            if (isValidDates()) {
                Visitant.findInvitedByHouseAndIdentificationNumber({
                    identificationNumber: vm.visitor.identificationnumber,
                    houseId: $rootScope.companyUser.houseId,
                    companyId: $rootScope.companyId,
                }, success, error)
            }
            function success(data) {
                bootbox.confirm({
                    message: "Un visitante con la cédula " + vm.visitor.identificationnumber + " ya se ha invitado con anterioridad, ¿Desea renovar su invitación y actualizar sus datos?",
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
                            vm.visitor.id = data.id;
                            formatVisitor();
                            Visitant.update(vm.visitor, onSuccess, onSaveError);
                                bootbox.hideAll();
                        } else {
                            bootbox.hideAll();
                        }

                        function onSuccess(data) {
                            WSVisitor.sendActivity(data);
                                bootbox.hideAll();
                            $state.go('visitant-invited-user')
                            toastr["success"]("Se ha renovado la invitación de " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
                        }
                    }
                });
            }

            function error() {
                formatVisitor();
                vm.isSaving = true;
                console.log(vm.visitor)
                vm.visitor.name = CommonMethods.capitalizeFirstLetter(vm.visitor.name);
                 vm.visitor.lastname = CommonMethods.capitalizeFirstLetter(vm.visitor.lastname);
                  vm.visitor.secondlastname = CommonMethods.capitalizeFirstLetter(vm.visitor.secondlastname);
                Visitant.save(vm.visitor, onSaveSuccess, onSaveError);

            }
            }
        }


        function onSaveSuccess(result) {
        WSVisitor.sendActivity(result);
            $scope.$emit('aditumApp:visitantUpdate', result);
            $state.go('visitant-invited-user')
              bootbox.hideAll();
            toastr["success"]("Se ha reportado como visitante invitado a " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }



        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendarInit(date) {
          vm.showMessageIntitial();
            vm.datePickerOpenStatus[date] = true;
        }

         function openCalendarFinal(date) {
          vm.showMessageFinal();
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
