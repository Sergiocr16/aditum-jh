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
        angular.element(document).ready(function() {
            $("#all").fadeIn("slow");
            var currentDate = new Date();
            vm.dates.initial_time = new Date();
            currentDate.setHours(currentDate.getHours() + 1);
            vm.dates.final_time = currentDate;
            vm.updatePicker();
            $('.dating').keydown(function() {
                return false;
            });
        });

        $timeout(function() {
            angular.element('#focusMe').focus();
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
            function invalidDates() {
                toastr["error"]("Tus fechas no tienen el formato adecuado, intenta nuevamente","Ups!");
                var currentDate = new Date();
                vm.dates.initial_time = new Date();
                currentDate.setHours(currentDate.getHours() + 1);
                vm.dates.final_time = currentDate;
                return false;
            }
            if (vm.dates.final_time != undefined && vm.dates.initial_time != undefined) {
                if (vm.dates.final_time.getTime() <= vm.dates.initial_time.getTime()) {
                    return invalidDates();
                }
            }
            if (vm.dates.final_time == undefined || vm.dates.initial_time == undefined) {
                return invalidDates();
            }
            return true;
        }

        function formatVisitor() {
            vm.visitor.isinvited = 1;
            vm.visitor.houseId = $rootScope.companyUser.houseId;
            vm.visitor.invitationstaringtime = vm.dates.initial_time;
            vm.visitor.invitationlimittime = vm.dates.final_time;
            vm.visitor.companyId = $rootScope.companyId;
            if (vm.visitor.licenseplate != undefined) {
                vm.visitor.licenseplate = vm.visitor.licenseplate.toUpperCase();
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

        vm.updatePicker = function() {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.dates.final_time,
                    minDate: new Date(),
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.dates.initial_time == new Date() ? new Date() : vm.dates.initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }
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
