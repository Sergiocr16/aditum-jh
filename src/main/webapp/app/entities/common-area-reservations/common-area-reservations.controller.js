(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaReservationsController', CommonAreaReservationsController);

    CommonAreaReservationsController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope'];

    function CommonAreaReservationsController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope) {

        var vm = this;
        $rootScope.active = "reservationAdministration";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        setTimeout(function(){loadAll();},1000)

        function onError(error) {
            AlertService.error(error.data.message);
        }
        function loadAll () {
            CommonAreaReservations.getPendingReservations({
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
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.commonAreaReservations = data;
                vm.page = pagingParams.page;
                angular.forEach(data,function(value){
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
                    })
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

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();