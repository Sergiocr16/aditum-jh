(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAllReservationsController', CommonAreaAllReservationsController);

    CommonAreaAllReservationsController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope','CommonMethods'];

    function CommonAreaAllReservationsController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope,CommonMethods) {

        var vm = this;
        vm.reverse = true;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.finalListReservations = [];
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        setTimeout(function(){loadAll();},1000)

        function onError(error) {
            AlertService.error(error.data.message);
        }
        function loadAll () {
            CommonAreaReservations.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: $rootScope.companyId
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                console.log(data)
                vm.finalListReservations = [];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.finalListReservations = data;
                vm.page = pagingParams.page;

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
                setTimeout(function() {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#tableDatas").fadeIn(300);
                },900 )
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
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

            bootbox.confirm({

                message: "¿Está seguro que desea eliminar la solicitud de reservación?",

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
                        commonArea.initalDate = new Date(commonArea.initalDate)
                        commonArea.initalDate.setHours(0);
                        commonArea.initalDate.setMinutes(0);
                        CommonMethods.waitingMessage();
                        commonArea.status = 4;
                        CommonAreaReservations.update(commonArea, onDeleteSuccess, onSaveError);

                    }
                }
            });
        };

        function onDeleteSuccess (result) {

            loadAll();
            toastr["success"]("Se eliminó la solicitud de reservación correctamente");
            bootbox.hideAll()
            // $state.go('common-area-administration.common-area-all-reservations');
            //
        }
        function onSaveError(error) {
            bootbox.hideAll()
            toastr["error"]("Un error inesperado ocurrió");
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
