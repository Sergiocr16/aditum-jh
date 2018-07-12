(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualReportController', MensualReportController);

    MensualReportController.$inject = ['AlertService','$rootScope','Principal','MensualAndAnualReport'];

    function MensualReportController(AlertService,$rootScope,Principal,MensualAndAnualReport) {

        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.isShowingMaintenanceDetail = false;
        vm.isShowingExtrardinaryDetail = false;
        vm.isShowingCommonAreasDetail = false;
         vm.isShowingOtherIngressDetail = false;

        setTimeout(function(){loadAll();},1000)

	    function loadAll() {
		     var date = new Date(), y = date.getFullYear(), m = date.getMonth();
             var firstDay = new Date(y, m, 1);
             var lastDay = new Date(y, m + 1, 0);
             vm.dates = {
                    initial_time: firstDay,
                    final_time: lastDay
             };
            MensualAndAnualReport.query({
                initial_time: moment(vm.dates.initial_time).format(),
                 final_time: moment(vm.dates.final_time).format(),
                 companyId: $rootScope.companyId
             },onSuccess,onError);

		}
        function onSuccess(data, headers) {
             vm.report = data;
             vm.allEgressTotalQuantity = data.mensualEgressReport.fixedCostsTotal + data.mensualEgressReport.variableCostsTotal + data.mensualEgressReport.otherCostsTotal;
             vm.allEgressPercentageQuantity = data.mensualEgressReport.fixedCostsPercentage + data.mensualEgressReport.variableCostsPercentage + data.mensualEgressReport.otherCostsPercentage
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
        function openCalendar(date) {
             vm.datePickerOpenStatus[date] = true;
        }
        vm.showBodyTableEgress = function(cost){
              if(cost.showDetail){
               cost.showDetail = false;
              } else{
               cost.showDetail = true;
              }
            console.log(cost);
        }

        vm.showBodyTableIngress = function(type,variable){
            if(variable){
                switch(type){
                    case 1:
                         vm.isShowingMaintenanceDetail = false;
                    break;
                    case 2:
                        vm.isShowingExtrardinaryDetail = false;
                        break;
                    case 3:
                       vm.isShowingCommonAreasDetail = false;
                       break;
                    case 4:
                        vm.isShowingOtherIngressDetail = false;
                        break;
                }
            }else{
               switch(type){
                    case 1:
                       vm.isShowingMaintenanceDetail = true;
                        break;
                    case 2:
                       vm.isShowingExtrardinaryDetail = true;
                        break;
                    case 3:
                       vm.isShowingCommonAreasDetail = true;
                        break;
                    case 4:
                       vm.isShowingOtherIngressDetail = true;
                       break;
                }


            }
        };
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
    }
})();
