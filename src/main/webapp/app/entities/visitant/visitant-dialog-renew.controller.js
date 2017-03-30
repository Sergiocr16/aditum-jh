(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDialogRenewController', VisitantDialogRenewController);

    VisitantDialogRenewController.$inject = ['$timeout', '$interval', '$scope', '$stateParams', 'Visitant', '$state', 'Principal', '$rootScope', 'CommonMethods','entity','$uibModalInstance'];

    function VisitantDialogRenewController($timeout, $interval, $scope, $stateParams, Visitant, $state, Principal, $rootScope, CommonMethods,entity,$uibModalInstance) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.visitor = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendarInit = openCalendarInit;
        vm.openCalendarFinal = openCalendarFinal;
        vm.save = save;
        vm.dates = {
            initial_time: new Date(),
            final_time: new Date()
        };

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
            vm.visitor.isinvited=1;
                  vm.visitor.invitationstaringtime = vm.dates.initial_time;
                         vm.visitor.invitationlimittime = vm.dates.final_time;
            if (vm.visitor.licenseplate != undefined) {
                vm.visitor.licenseplate = vm.visitor.licenseplate.toUpperCase();
            }
        }

        function save() {
            if (isValidDates()) {
                formatVisitor();
                Visitant.update(vm.visitor, onSuccess, onSaveError);
                 function onSuccess(result) {
                        toastr["success"]("Se ha renovado la invitación de " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
                       $scope.$emit('aditumApp:visitantUpdate', result);
                       $state.reload();
                       $uibModalInstance.close(result);
                  }

            }
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
