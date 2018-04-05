(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressGenerateReportController', EgressGenerateReportController);

    EgressGenerateReportController.$inject = ['$scope','$state', 'Egress', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonMethods','Proveedor','$rootScope'];

    function EgressGenerateReportController($scope,$state, Egress, ParseLinks, AlertService, paginationConstants, pagingParams,CommonMethods,Proveedor,$rootScope) {

        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.propertyName = 'id';
        vm.reverse = true;

        vm.dates = {
            initial_time: undefined,
            final_time: undefined
        };
         vm.updatePicker = function() {
            vm.picker1 = {
                datepickerOptions: {
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
         vm.updatePicker();

        setTimeout(function(){loadProveedors();},500)
        function loadProveedors() {
            Proveedor.query({companyId: $rootScope.companyId}).$promise.then(onSuccessProveedores);
            function onSuccessProveedores(data, headers) {
                vm.proveedores = data;
                loadAll();
            }
        }
        function loadAll () {

        }
        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
