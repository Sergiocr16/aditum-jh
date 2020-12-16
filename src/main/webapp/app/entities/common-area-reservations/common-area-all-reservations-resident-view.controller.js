(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAllReservationsResidentViewController', CommonAreaAllReservationsResidentViewController);

    CommonAreaAllReservationsResidentViewController.$inject = ['Modal','$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope','CommonMethods','globalCompany'];

    function CommonAreaAllReservationsResidentViewController(Modal,$state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope,CommonMethods,globalCompany) {

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
        vm.detailProof = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-proof-detail', {
                id: encryptedId
            })
        };


        function loadAll () {
            CommonAreaReservations.findByHouseId({
                page: pagingParams.page - 1,
                size: 1000,
                sort: sort(),
                houseId: CommonMethods.encryptS(globalCompany.getHouseId())
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
                houseId: globalCompany.getHouseId(),
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
        if(reservation.commonArea.chargeRequired==0){
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
        }else{
            Modal.toast("Para cancelar la reservación aprobada, comuniquese con su respectivo administrador.")
        }



        };


        function onCancelSuccess(result) {
            Modal.hideLoadingBar();
            vm.isReady = false;
            Modal.toast("Se ha cancelado la reservación correctamente.")
            loadAll();

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
                        var time = value.split(".")[0];
                        var min = value.split(".")[1];
                        if (min == 75) {
                            min = 45;
                        }
                        if(min == 5){
                            min = 30;
                        }
                        times[key] = time + ":"+min+"AM";
                    }
                } else if (value > 12) {
                    if (esEntero(parseFloat(value))) {
                        times[key] = value - 12 + ":00PM"
                    } else {
                        var time = value.split(".")[0];
                        var min = value.split(".")[1];
                        if (min == 75) {
                            min = 45;
                        }
                        if(min == 5){
                            min = 30;
                        }
                        times[key] = time -12 + ":"+min+"PM";
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
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
