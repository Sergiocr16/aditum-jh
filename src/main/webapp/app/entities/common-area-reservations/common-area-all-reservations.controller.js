(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAllReservationsController', CommonAreaAllReservationsController);

    CommonAreaAllReservationsController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonArea', 'House', 'Resident', '$rootScope', 'CommonMethods', 'globalCompany', 'Modal'];

    function CommonAreaAllReservationsController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams, CommonArea, House, Resident, $rootScope, CommonMethods, globalCompany, Modal) {
        var vm = this;
        $rootScope.active = "reservationAdministration";
        vm.reverse = true;
        vm.loadPage = loadPage;
        vm.isReady = false;
        vm.isConsulting = false;
        vm.showFilterDiv = true;
        $rootScope.mainTitle = "Reservaciones";
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.consult = consult;
        vm.finalListReservations = [];
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.dates = {};
        vm.page = 0;
        vm.links = {
            last: 0
        };
        loadAll();
        vm.detailProof = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-proof-detail', {
                id: encryptedId
            })
        };

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function loadAll() {
            var d = new Date; // get current date
            if(vm.isConsulting == false){
                vm.dates.initial_time= new Date()
                vm.dates.final_time = new Date()
                vm.dates.final_time.setDate(d.getDate() + 7);
            }
            CommonAreaReservations.findBetweenDatesByCompany({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                page: vm.page,
                size: 20,
            }, onSuccess, onError);
            function sort() {
                var result = [];
                if (vm.predicate !== 'initalDate') {
                    // result.push('initalDate,desc');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                for (var i = 0; i < data.length; i++) {
                    data[i].schedule = formatScheduleTime(data[i].initialTime, data[i].finalTime);
                        vm.finalListReservations.push(data[i])
                }
                vm.isReady = true;
            }

            function onError(error) {
                // AlertService.error(error.data.message);
            }
        }

        vm.stopConsulting = function () {
            vm.isReady = false;
            vm.dates = {
                initial_time: undefined,
                final_time: undefined
            };
            vm.page = 0;
            pagingParams.search = null;
            vm.isConsulting = false;
            loadAll();
        }

        function consult() {
            vm.isReady = false;
            CommonAreaReservations.findBetweenDatesByCompany({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.isConsulting = true;
                vm.finalListReservations = [];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.finalListReservations = data;
                vm.page = pagingParams.page;
                loadInfoByReservation(data);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

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
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        vm.denyReservation = function (reservation) {
            Modal.confirmDialog("¿Está seguro que desea rechazar la reservación?", "Una vez registrada esta información no se podrá editar",
                function () {
                    Modal.showLoadingBar()
                    reservation.sendPendingEmail = true;
                    reservation.status = 3;
                    reservation.initalDate = new Date(reservation.initalDate)
                    reservation.initalDate.setHours(0);
                    reservation.initalDate.setMinutes(0);
                    CommonAreaReservations.update(reservation, onDenySuccess, onSaveError);

                });


        };
        vm.aprobeReservation = function () {
            Modal.toast("Para aprobar una solicitud debe ingresar al detalle de la reservación.")

        };
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

        vm.cancelReservation = function (reservation) {
            Modal.confirmDialog("¿Está seguro que desea cancelar la reservación?", "Una vez registrada esta información no se podrá editar",
                function () {
                    Modal.showLoadingBar()
                    reservation.sendPendingEmail = true;
                    reservation.status = 11;
                    reservation.initalDate = new Date(reservation.initalDate)
                    reservation.initalDate.setHours(0);
                    reservation.initalDate.setMinutes(0);
                    CommonAreaReservations.update(reservation, onCancelSuccess);
                });
        };

        function onCancelSuccess(result) {
            Modal.hideLoadingBar();
            vm.isReady = false;
            Modal.toast("Se ha cancelado la reservación correctamente.")
            loadAll();

        }

        function onDenySuccess(result) {

            loadAll();
            Modal.toast("Se ha rechazado la reservación correctamente.")
            Modal.hideLoadingBar();

        }

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
            // $state.transitionTo($state.$current, {
            //     page: vm.page,
            //     sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
            //     search: vm.currentSearch
            // });
            loadAll();
        }
    }
})();
