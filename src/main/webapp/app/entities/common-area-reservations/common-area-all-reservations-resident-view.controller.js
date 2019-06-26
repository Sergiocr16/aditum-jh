(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAllReservationsResidentViewController', CommonAreaAllReservationsResidentViewController);

    CommonAreaAllReservationsResidentViewController.$inject = ['Modal','$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope','CommonMethods','companyUser'];

    function CommonAreaAllReservationsResidentViewController(Modal,$state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope,CommonMethods,companyUser) {

        var vm = this;
        $rootScope.active = "allReservationsResidentsView";
        vm.reverse = true;
        vm.loadPage = loadPage;
        $rootScope.mainTitle = "Mis reservaciones";
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.finalListReservations = [];
        vm.consult = consult;
        vm.isReady = false;
        vm.isConsulting = false;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();


        function loadAll () {
            CommonAreaReservations.findByHouseId({
                page: pagingParams.page - 1,
                size: 1000,
                sort: sort(),
                houseId: companyUser.houseId
            }, onSuccess, onError);
            function sort() {
                var result = [];
                if (vm.predicate !== 'initalDate') {
                    result.push('initalDate,desc');
                }
                return result;
            }

        }
        function consult() {
            vm.isConsulting = true;
            vm.isReady = false;
            CommonAreaReservations.findBetweenDatesByHouse({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                houseId: companyUser.houseId,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
            }, onSuccess, onError);


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
        }
        function onSuccess(data, headers) {

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
        function loadInfoByReservation(data){
            angular.forEach(data,function(value){
                value.schedule = formatScheduleTime(value.initialTime, value.finalTime);

            });
            vm.isReady = true;


        }

        vm.cancelReservation = function(reservation) {

            Modal.confirmDialog("¿Está seguro que desea cancelar la reservación?", "Una vez registrada esta información no se podrá editar",
                function () {
                    Modal.showLoadingBar()

                    reservation.sendPendingEmail = false ;
                    reservation.status = 10;
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
