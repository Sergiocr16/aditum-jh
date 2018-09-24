(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChangeWatchesController', ChangeWatchesController);

    ChangeWatchesController.$inject = ['$stateParams', '$rootScope', 'CommonMethods', 'AlertService', '$uibModalInstance', 'Principal', 'Officer', 'Watch'];

    function ChangeWatchesController($stateParams, $rootScope, CommonMethods, AlertService, $uibModalInstance, Principal, Officer, Watch) {

        var vm = this;

        vm.currentDate = new Date();
        vm.clear = clear;
        var companyId = CommonMethods.decryptIdUrl($stateParams.companyId);
        vm.isAuthenticated = Principal.isAuthenticated;

        console.log(vm.currentDate)
//        vm.minInitialTime = moment(new Date(1970, 0, 1,currentDate.getHours(), currentDate.getMinutes(), 0)).format('HH:mm')
        angular.element(document).ready(function () {

            vm.formatInitPickers();
        });
        vm.dates = {
            initial_time: new Date(),
            final_time: new Date()
        };
        vm.formatDate = function (date, time) {
            return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), 0, 0);
        }

        function isValidDates() {

            vm.dates.initial_date.setHours(vm.dates.initial_time.getHours())
            vm.dates.initial_date.setMinutes(vm.dates.initial_time.getMinutes())
            vm.dates.final_date.setHours(vm.dates.final_time.getHours())
            vm.dates.final_date.setMinutes(vm.dates.final_time.getMinutes())

            function invalidDates() {
                toastr["error"]("Tus fechas no tienen el formato adecuado, intenta nuevamente", "Ups!");
                vm.formatInitPickers()
                bootbox.hideAll();
                return false;
            }

            if (vm.dates.final_time == undefined || vm.dates.initial_time == undefined || vm.dates.initial_date == undefined || vm.dates.final_date == undefined) {
                return invalidDates();
            } else {
                if (vm.formatDate(vm.dates.initial_date, vm.dates.initial_time).getTime() >= vm.formatDate(vm.dates.final_date, vm.dates.final_time).getTime()) {
                    return invalidDates();
                }
            }
            return true;
        }

        vm.formatInitPickers = function () {

            var currentDate = new Date();
//            FECHAS
            vm.dates.initial_date = new Date();
            vm.dates.final_date = new Date();
            vm.minInitialDate = moment(currentDate).format("YYYY-MM-DD")

//            HORAS
            vm.dates.initial_time = new Date(1970, 0, 1, 6, 0, 0)
            vm.dates.final_time = new Date(1970, 0, 1, 18, 0, 0)
            setTimeout(function () {
                vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm')
                vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm')
                vm.visitor = {type: "9", found: 0, validIdentification: 1, validPlateNumber: 1};
            }, 300)
        }

        Officer.query({
            companyId: companyId
        }, onSuccess, onError);

        function onSuccess(data, headers) {

            vm.officers = data;
            $("#loadingIcon").fadeOut(0);
            setTimeout(function () {
                $("#officers_turn_container").fadeIn(300);
            }, 200)

        }

        function onError(error) {
            AlertService.error(error.data.message);
        }


        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.updateDatePicker = function () {
            vm.initialDateMinMax = moment(vm.dates.initial_date).format("YYYY-MM-DD")
            vm.finalDateMinMax = moment(vm.dates.final_date).format("YYYY-MM-DD")

        }

        vm.updateTimePicker = function () {
            vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm')
            vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm')

        }
        vm.officersLinked = []
        vm.moveToLink = function (officer) {
            var index = vm.officersLinked.indexOf(officer);
            vm.officersLinked.splice(index, 1);
            vm.officers.push(officer);
        }
        vm.moveToLinked = function (officer) {

            var index = vm.officers.indexOf(officer);
            vm.officers.splice(index, 1);
            var item = officer;
            vm.officersLinked.push(item);
        }

        vm.reportTurn = function () {
            if (isValidDates()) {
                vm.isSaving = true;
                if (vm.officersLinked.length == 0) {
                    console.log('dsaf');
                    toastr["error"]("Debe elegir al menos un oficial para reportar el turno");
                    vm.isSaving = false;
                } else {
                    console.log('dsaf');

                    vm.dates.initial_date.setHours(vm.dates.initial_time.getHours())
                    vm.dates.initial_date.setMinutes(vm.dates.initial_time.getMinutes())
                    vm.dates.final_date.setHours(vm.dates.final_time.getHours())
                    vm.dates.final_date.setMinutes(vm.dates.final_time.getMinutes())

                    var watch = {
                        initialtime: vm.dates.initial_date,
                        finaltime: vm.dates.final_date,
                        responsableofficer: serializeOfficers(vm.officersLinked),
                        companyId: companyId
                    }
//                    console.log(watch)
                    Watch.save(watch, onSaveSuccess, onSaveError);
                }
            }
        }

        function onSaveSuccess(result) {

            $uibModalInstance.close(result);
            vm.isSaving = false;
            toastr["success"]("Se registró el turno correctamente");
        }

        function onSaveError() {
            vm.isSaving = false;
        }

//               FUNCION PARA SERIALIZAR OFICIALES
        function serializeOfficers(officers) {
            var responsableofficers = "";

            angular.forEach(officers, function (officer, key) {
                responsableofficers += formatOfficer(officer) + "||";
            })

            function formatOfficer(officer) {
                var fullName = officer.name + " " + officer.lastname + " " + officer.secondlastname;
                return officer.id + ";" + officer.identificationnumber + ";" + fullName;
            }

            console.log(responsableofficers);
            return responsableofficers;
        }

    }
})();
