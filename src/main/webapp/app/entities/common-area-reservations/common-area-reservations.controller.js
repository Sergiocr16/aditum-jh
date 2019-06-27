(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsController', CommonAreaReservationsController);

    CommonAreaReservationsController.$inject = ['Modal','$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope','globalCompany'];

    function CommonAreaReservationsController(Modal,$state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope,globalCompany) {

        var vm = this;
        $rootScope.active = "reservations";
        vm.loadPage = loadPage;
        vm.isReady = false;
        $rootScope.mainTitle = "Reservaciones";
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function onError(error) {
            AlertService.error(error.data.message);
        }
        function loadAll () {
            CommonAreaReservations.getPendingReservations({
                page: pagingParams.page - 1,
                size: 1000,
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.commonAreaReservations = [];
                vm.page = pagingParams.page;
                angular.forEach(data,function(value){
                    value.schedule = formatScheduleTime(value.initialTime, value.finalTime);
                    if(value.status!==9){
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
        vm.denyReservation = function(reservation) {

            Modal.confirmDialog("¿Está seguro que desea rechazar la reservación?", "Una vez registrada esta información no se podrá editar",
                function () {
                    Modal.showLoadingBar()
                    reservation.sendPendingEmail = true ;
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

        vm.deleteReservation = function(commonArea) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la solicitud de reservación?","",
                function(){
                    commonArea.initalDate = new Date(commonArea.initalDate)
                    commonArea.initalDate.setHours(0);
                    commonArea.initalDate.setMinutes(0);
                    Modal.showLoadingBar();
                    commonArea.status = 4;
                    CommonAreaReservations.update(commonArea, onDeleteSuccess, onError);

                });

        };

        function onDeleteSuccess (result) {

            loadAll();
            Modal.toast("Se eliminó la solicitud de reservación correctamente");
            Modal.hideLoadingBar();
            // $state.go('common-area-administration.common-area-all-reservations');
            //
        }
        function formatScheduleTime(initialTime, finalTime){
            var times = [];
            times.push(initialTime);
            times.push(finalTime);
            angular.forEach(times,function(value,key){
                if(value==0){
                    times[key] = "12:00AM"
                }else if(value<12){
                   times[key] = value + ":00AM"
               }else if(value>12){
                   times[key] = parseInt(value)-12 + ":00PM"
               }else if(value==12){
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

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
