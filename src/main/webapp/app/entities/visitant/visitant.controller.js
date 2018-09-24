(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantController', VisitantController);

    VisitantController.$inject = ['Visitant', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'companyUser'];

    function VisitantController(Visitant, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, companyUser) {
        $rootScope.active = "residentsVisitors";
        var vm = this;
        vm.Principal;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.consult = consult;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.dates = {
            initial_time: undefined,
            final_time: undefined
        };
        // setTimeout(function () {
        loadAll();
        // }, 500)
        vm.isDisableButton = function () {
            if (vm.dates.initial_time == undefined || vm.dates.final_time == undefined) return true;
            return false;
        }

        angular.element(document).ready(function () {
            $('.dating').keydown(function () {
                return false;
            });
        });

        function formatVisitors(visitors) {
            angular.forEach(visitors, function (value, key) {
                value.fullName = value.name + " " + value.lastname + " " + value.secondlastname;
            })
        }

        vm.updatePicker = function () {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.dates.final_time == undefined ? new Date() : vm.dates.final_time,
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    maxDate: new Date(),
                    minDate: vm.dates.initial_time,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }
        vm.updatePicker();

        function consult() {
            $("#all").fadeOut(0);
            setTimeout(function () {
                $("#loadingIcon").fadeIn(100);
            }, 200)
            Visitant.findBetweenDatesByHouse({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                houseId: companyUser.houseId,
            }).$promise.then(onSuccess);

            function onSuccess(data) {
                vm.visitants = data;
                vm.page = pagingParams.page;
                vm.title = 'Visitantes entre:';
                vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");
                vm.isConsulting = true;
                formatVisitors(vm.visitants);
                setTimeout(function () {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function () {
                    $("#all").fadeIn('slow');
                }, 700)
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.stopConsulting = function () {
            $("#loadingIcon").fadeIn();
            vm.dates.initial_time = undefined;
            vm.dates.final_time = undefined;
            vm.isConsulting = false;
            loadAll();
            vm.titleConsult = "";
        }

        function loadAll() {
            $("#all").fadeOut(0);
            setTimeout(function () {
                $("#loadingIcon").fadeIn(100);
            }, 250)
            Visitant.findByHouseInLastMonth({
                houseId: companyUser.houseId,
            }).$promise.then(onSuccess);

            function onSuccess(data) {
                vm.visitants = data;
                vm.page = pagingParams.page;
                vm.title = 'Visitantes del mes';
                vm.isConsulting = false;
                formatVisitors(vm.visitants);
                setTimeout(function () {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function () {
                    $("#all").fadeIn('slow');
                }, 700)
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
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

        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
