(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseAbsenceController', HouseAbsenceController);

    HouseAbsenceController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'House','Principal'];

    function HouseAbsenceController($scope, $rootScope, $stateParams, previousState,House,Principal) {
           $rootScope.active = "house-absence";
        moment.locale('es');
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.consulting_initial_time = new Date();
        vm.consulting_final_time = new Date();
        House.get({id: globalCompany.getHouseId().houseId},onSuccess,onError);
        angular.element(document).ready(function () {
          $("#all").fadeIn("slow");
                $('.dating').keydown(function() {
                    return false;
                });
    });
        function onSuccess(house){
          vm.house=house;
          vm.house.finaltime = moment(house.desocupationfinaltime).format('LL');
          vm.house.initialtime = moment(house.desocupationinitialtime).format('LL');

        }
        function onError(){

        }

        vm.reportAbsence = function(){
                    bootbox.confirm({
                        message: "¿Está seguro que desea reportar su ausencia en su filial?",
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
                               vm.house.desocupationinitialtime = vm.consulting_initial_time;
                                vm.house.desocupationfinaltime = vm.consulting_final_time;
                                var house = vm.house;
                                house.isdesocupated = 1;
                                House.reportAbsence(house,onSuccessReport,onError);

                            }
                        }
                    });

            function onSuccessReport(house){
                toastr["success"]("Se ha reportado la ausencia en tu filial correctamente");
                vm.house = house;
                vm.house.finaltime = moment(house.desocupationfinaltime).format('LL');
                vm.house.initialtime = moment(house.desocupationinitialtime).format('LL');
            }
        }

        vm.cancelAbsence = function(){
                    bootbox.confirm({
                        message: "¿Está seguro que desea cancelar la ausencia en su filial?",
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
                                var house = vm.house;
                                house.isdesocupated = 0;

                                House.update(house,onSuccessCancel,onError);


                            }
                            function onSuccessCancel(house){
                                toastr["success"]("Se ha cancelado la ausencia en tu filial correctamente");
                                vm.house = house;
                            }
                        }

                    });
        }

        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('aditumApp:houseUpdate', function(event, result) {
            vm.house = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

vm.updatePicker = function() {

            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.consulting_final_time,
                    minDate: new Date(),
                    enableTime: false,
                    showWeeks: false,
                }
            };

            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.consulting_initial_time == new Date() ? new Date() : vm.consulting_initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }

            if(vm.consulting_initial_time==undefined){
              vm.consulting_initial_time = new Date();
            }
            if(vm.consulting_final_time==undefined){
              vm.consulting_final_time = new Date();
            }
        }



        vm.updatePicker();
        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
