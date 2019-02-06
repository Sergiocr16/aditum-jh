(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationDevolutionDoneController', ReservationDevolutionDoneController);

    ReservationDevolutionDoneController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonArea', 'House', 'Resident', '$rootScope', 'CommonMethods', 'globalCompany', 'Modal'];

    function ReservationDevolutionDoneController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams, CommonArea, House, Resident, $rootScope, CommonMethods, globalCompany, Modal) {

        var vm = this;
        $rootScope.active = "devolutions";
        vm.reverse = true;
        vm.loadPage = loadPage;
        vm.isReady = false;
        vm.isConsulting = false;
        $rootScope.mainTitle = "Devoluciones de depósito";
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.consult = consult;
        vm.finalListReservations = [];
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.consult = consult;
        loadAll();


        function onError(error) {
            AlertService.error(error.data.message);
        }

        function loadAll() {
            CommonAreaReservations.getDevolutionDoneReservations({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [];
                if (vm.predicate !== 'initalDate') {
                    result.push('initalDate,desc');
                }
                return result;
            }

        }

        vm.stopConsulting = function () {
            vm.isReady = false;
            vm.dates = {
                initial_time: undefined,
                final_time: undefined
            };
            pagingParams.page = 1;
            pagingParams.search = null;
            vm.isConsulting = false;
            loadAll();
        };

        function consult() {
            vm.isReady = false;
            vm.isConsulting = true;
            CommonAreaReservations.findDevolutionDoneBetweenDates({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            console.log(data)
            vm.queryCount = vm.totalItems;
            vm.finalListReservations = data;
            vm.page = pagingParams.page;
            loadInfoByReservation(data);

        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        };

        vm.stopConsulting = function () {
            vm.isReady = false;
            vm.dates = {
                initial_time: undefined,
                final_time: undefined
            };
            pagingParams.page = 1;
            pagingParams.search = null;
            vm.isConsulting = false;
            loadAll();
        };


        function loadInfoByReservation(data) {
            angular.forEach(data, function (value) {
                value.schedule = formatScheduleTime(value.initialTime, value.finalTime);

            });
            vm.isReady = true;


        }

        function formatScheduleTime(initialTime, finalTime) {
            var times = [];
            times.push(initialTime);
            times.push(finalTime);
            angular.forEach(times, function (value, key) {
                if (value == 0) {
                    times[key] = "12:00AM"
                } else if (value < 12) {
                    times[key] = value + ":00AM"
                } else if (value > 12) {
                    times[key] = parseInt(value) - 12 + ":00PM"
                } else if (value == 12) {
                    times[key] = value + ":00PM"
                }

            });
            return times[0] + " - " + times[1]
            console.log(times)
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        vm.deleteReservation = function (commonArea) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la solicitud de reservación?", "",
                function () {
                    commonArea.initalDate = new Date(commonArea.initalDate)
                    commonArea.initalDate.setHours(0);
                    commonArea.initalDate.setMinutes(0);
                    Modal.showLoadingBar();
                    commonArea.status = 4;
                    CommonAreaReservations.update(commonArea, onDeleteSuccess, onSaveError);

                });

        };

        function onDeleteSuccess(result) {

            loadAll();
            Modal.toast("Se eliminó la solicitud de reservación correctamente");
            Modal.hideLoadingBar();
            // $state.go('common-area-administration.common-area-all-reservations');
            //
        }

        function onSaveError(error) {
            Modal.hideLoadingBar();
            Modal.toast("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
