(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAllReservationsController', CommonAreaAllReservationsController);

    CommonAreaAllReservationsController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope','CommonMethods','globalCompany','Modal'];

    function CommonAreaAllReservationsController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope,CommonMethods,globalCompany,Modal) {

        var vm = this
        $rootScope.active = "reservationAdministration";
        vm.reverse = true;
        vm.loadPage = loadPage;
        vm.isReady = false;
        $rootScope.mainTitle = "Administrar reservaciones";
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
        function loadAll () {
            CommonAreaReservations.query({
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

        function loadInfoByReservation(data){
            angular.forEach(data,function(value){
                // if(value.status!==4){
                House.get({
                    id: value.houseId
                }, function(result) {
                    value.houseNumber = result.housenumber;
                    Resident.get({
                        id: value.residentId
                    }, function(result) {
                        value.residentName = result.name + " " + result.lastname;
                        CommonArea.get({
                            id: value.commonAreaId
                        }, function(result) {
                            value.commonAreaName = result.name ;
                            value.schedule = formatScheduleTime(value.initialTime, value.finalTime);

                        })
                    })
                });
                //     vm.finalListReservations.push(value)
                // }
            });
            vm.isReady = true;
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
        vm.deleteReservation = function(commonArea) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la solicitud de reservación?","",
                function(){
                    commonArea.initalDate = new Date(commonArea.initalDate)
                    commonArea.initalDate.setHours(0);
                    commonArea.initalDate.setMinutes(0);
                    Modal.showLoadingBar();
                    commonArea.status = 4;
                    CommonAreaReservations.update(commonArea, onDeleteSuccess, onSaveError);

                });

        };

        function onDeleteSuccess (result) {

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
