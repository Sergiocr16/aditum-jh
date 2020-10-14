(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsController', CommonAreaReservationsController);

    CommonAreaReservationsController.$inject = ['CommonMethods', 'Modal', '$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonArea', 'House', 'Resident', '$rootScope', 'globalCompany'];

    function CommonAreaReservationsController(CommonMethods, Modal, $state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams, CommonArea, House, Resident, $rootScope, globalCompany) {

        var vm = this;
        $rootScope.active = "reservations";
        vm.loadPage = loadPage;
        vm.isReady = false;
        $rootScope.mainTitle = "Reservaciones";
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.commonAreaReservations = [];
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
            CommonAreaReservations.getPendingReservations({
                page:  vm.page,
                size: 20,
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [];
                // result.push('initialDate,asc');
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function (value) {
                    value.schedule = formatScheduleTime(value.initialTime, value.finalTime);
                    if (value.status !== 9) {
                        vm.commonAreaReservations.push(value);
                    }
                });
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function onSaveError(error) {
            Modal.hideLoadingBar();
            Modal.toast("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
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

        function onDenySuccess(result) {

            loadAll();
            Modal.toast("Se ha rechazado la reservación correctamente.")
            Modal.hideLoadingBar();

        }

        vm.deleteReservation = function (commonArea) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la solicitud de reservación?", "",
                function () {
                    commonArea.initalDate = new Date(commonArea.initalDate)
                    commonArea.initalDate.setHours(0);
                    commonArea.initalDate.setMinutes(0);
                    Modal.showLoadingBar();
                    commonArea.status = 4;
                    CommonAreaReservations.update(commonArea, onDeleteSuccess, onError);

                });

        };

        function onDeleteSuccess(result) {

            loadAll();
            Modal.toast("Se eliminó la solicitud de reservación correctamente");
            Modal.hideLoadingBar();
            // $state.go('common-area-administration.common-area-all-reservations');
            //
        }
        function esEntero(numero) {
            if (numero % 1 == 0) {
                return true;
            } else {
                return false;
            }
        }
        function formatScheduleTime(initialTime, finalTime) {
            var times = [];
            times.push(initialTime);
            times.push(finalTime);
            angular.forEach(times, function (value, key) {
                if (value == 0) {
                    times[key] = "12:00AM"
                } else if (value < 12) {
                    if (esEntero(parseFloat(value))) {
                        times[key] = value + ":00AM"
                    } else {
                        times[key] = value - 0.5 + ":30AM"
                    }
                } else if (value > 12) {
                    if (esEntero(parseFloat(value))) {
                        times[key] = value -12 + ":00PM"
                    } else {
                        times[key] = value - 12-  0.5 + ":30PM"
                    }
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

        function transition() {
            loadAll();
        }
    }
})();
