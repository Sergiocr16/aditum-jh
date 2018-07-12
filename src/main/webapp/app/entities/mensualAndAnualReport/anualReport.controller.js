

(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnualReportController', AnualReportController);

    AnualReportController.$inject = ['AlertService','$rootScope','Principal','MensualAndAnualReport'];

    function AnualReportController(AlertService,$rootScope,Principal,MensualAndAnualReport) {
        var vm = this;
        vm.datePickerOpenStatus = {};

        vm.openCalendar = openCalendar;
         var dateMonthDay = new Date(), y1 = dateMonthDay.getFullYear(), m1 = dateMonthDay.getMonth();
         vm.actualMonth = new Date(y1, m1, 1);
         vm.month = m1+1;
         vm.rowsQuantity = vm.month + 4;
        setTimeout(function(){vm.loadAll();},1000)

        vm.loadAll = function() {
             MensualAndAnualReport.getAnualReport({
                  actual_month: moment(vm.actualMonth).format(),
                  companyId: $rootScope.companyId,
                  withPresupuesto: 1,
              },onSuccess,onError);

        }
        function onSuccess(data, headers) {
             vm.report = data;
             vm.totalFlujo = vm.report.allIngressAcumulado - vm.report.allEgressAcumulado;
             console.log(vm.report)
                 $("#loadingIcon2").fadeOut(0);
                           setTimeout(function() {
                               $("#reportResults").fadeIn(500);
                           }, 200)
//              angular.forEach(vm.report.anualEgressByMonth,function(value,key){
//               console.log(value)
//              })


        }

        function openCalendar(date) {
             vm.datePickerOpenStatus[date] = true;
        }

        vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
             var rgx = /(\d+)(\d{3})/;
             while (rgx.test(x1)) {
                     x1 = x1.replace(rgx, '$1' + ',' + '$2');
             }
             return x1 + x2;
        }
         vm.updatePicker = function() {
            vm.picker1 = {
                datepickerOptions: {
                   maxDate: vm.dates.final_time,
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.dates.initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }
          }

            function onError(error) {
                      AlertService.error(error.data.message);
                  }
          vm.datePickerOpenStatus.initialtime = false;
             vm.datePickerOpenStatus.finaltime = false;

             function openCalendar(date) {
                  vm.datePickerOpenStatus[date] = true;
             }

    }
})();
