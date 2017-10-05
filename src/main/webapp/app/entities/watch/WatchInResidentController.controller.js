(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchInResidentController', WatchInResidentController);

    WatchInResidentController.$inject = ['Watch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope'];

    function WatchInResidentController(Watch, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isConsulting = false;
        vm.showCleanBtn = false;
        vm.showBackBtn = false;
        vm.currentTurn = true;
        moment.locale('es');
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        $rootScope.active = "watches-in-residents";
        angular.element(document).ready(function() {
            $('.dating').keydown(function() {
                return false;
            });
        });

        function formatWatch(watch) {
            watch.initialtime = moment(vm.watch.initialtime).format('h:mm a');
            if (watch.finaltime === null) {
                watch.finaltime = 'Aún en progreso'
            } else {
                watch.finaltime = moment(vm.watch.finaltime).format('h:mm a');
            }
            watch.officers = getformatResponsableOfficers(vm.watch);
            return watch;
        }



        function setWatch(data) {
            vm.showTable = false;
            vm.watch = data;
            vm.watch = formatWatch(vm.watch);
        }


        vm.getCurrentWatch = function() {
            $("#data").fadeOut(0);
            setTimeout(function() {
                $("#loadingData").fadeIn(300);
            }, 200)
            Watch.getCurrent({
                companyId: $rootScope.companyId
            }, onSuccessCurrent, onErrorCurrent);

            function onSuccessCurrent(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.isData = true;
                $("#loadingData").fadeOut(0);
                setTimeout(function() {
                    $("#data").fadeIn(300);
                }, 200)
                setWatch(data);
                vm.consulting_initial_time = "";
                vm.consulting_final_time = "";
                $("#loadingIcon").fadeOut(0);
                setTimeout(function() {
                    $("#tableData").fadeIn(300);
                }, 200);
            }

            function onErrorCurrent(error) {
             $("#loadingIcon").fadeOut(0);
                setTimeout(function() {
                    $("#tableData").fadeIn(300);
                }, 200);
                vm.isData = false;
                AlertService.error(error.data.message);
            }
        }

      setTimeout(function(){vm.getCurrentWatch();},500)

        vm.getWatch = function(watch) {
            $("#data").fadeOut(0);
            setTimeout(function() {
                $("#loadingData").fadeIn(300);
            }, 200)
            Watch.get({
                id: watch.id
            }, onSuccess, onError)

            function onSuccess(data, headers) {
                $("#loadingData").fadeOut(0);
                setTimeout(function() {
                    $("#data").fadeIn(300);
                }, 200)
                setWatch(data);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }



        function formatResponsableOfficer(stringOfficer) {
            var variables = stringOfficer.split(';')
            var officer = {};
            officer.id = variables[0];
            officer.identificationnumber = variables[1];
            officer.name = variables[2];
            return officer;
        }

        function getformatResponsableOfficers(watch) {
            var formattedOfficers = [];
            var stringOfficers = watch.responsableofficer.slice(0, -2);
            var officers = stringOfficers.split('||');
            angular.forEach(officers, function(officer, key) {
                formattedOfficers.push(formatResponsableOfficer(officer))
            })
            return formattedOfficers;
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



        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

    }
})();


