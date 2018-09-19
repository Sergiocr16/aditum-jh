(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAllReservationsResidentViewController', CommonAreaAllReservationsResidentViewController);

    CommonAreaAllReservationsResidentViewController.$inject = ['$state', 'CommonAreaReservations', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','CommonArea','House','Resident','$rootScope','CommonMethods','companyUser'];

    function CommonAreaAllReservationsResidentViewController($state, CommonAreaReservations, ParseLinks, AlertService, paginationConstants, pagingParams,CommonArea,House,Resident,$rootScope,CommonMethods,companyUser) {

        var vm = this;
        $rootScope.active = "allReservationsResidentsView";
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
            CommonAreaReservations.findByHouseId({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                houseId: companyUser.houseId
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
