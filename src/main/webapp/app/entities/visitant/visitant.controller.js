(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantController', VisitantController);

    VisitantController.$inject = ['Visitant', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'companyUser', 'globalCompany'];

    function VisitantController(Visitant, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, companyUser, globalCompany) {
        $rootScope.active = "residentsVisitors";
        var vm = this;
        vm.Principal;
        $rootScope.mainTitle = "Bitácora de visitantes";
        vm.isAuthenticated = Principal.isAuthenticated;
        Principal.identity().then(function (account) {
            switch (account.authorities[0]) {
                case "ROLE_USER":
                    vm.userType = 1;
                    break;
                case "ROLE_OWNER":
                    vm.userType = 1;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    break;
            }
            vm.loadPage = loadPage;
            vm.consult = consult;
            vm.predicate = pagingParams.predicate;
            vm.reverse = pagingParams.ascending;
            vm.transition = transition;
            vm.itemsPerPage = paginationConstants.itemsPerPage;

            vm.isReady = false;
            var date = new Date(), y = date.getFullYear(), m = date.getMonth();
            var firstDay = new Date(y, m, 1);
            var lastDay = new Date(y, m + 1, 0);
            vm.first_month_day = firstDay;
            vm.houseSelected = -1;
            vm.dates = {
                initial_time: firstDay,
                final_time: lastDay
            };
            vm.exportActions = {
                downloading: false,
                printing: false,
                sendingEmail: false
            };
            vm.download = function () {
                vm.exportActions.downloading = true;
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.exportActions.downloading = false;
                    })
                }, 7000)
            };

            vm.print = function () {
                vm.exportActions.printing = true;
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.exportActions.printing = false;
                    })
                }, 7000);
                printJS({
                    printable: vm.path,
                    type: 'pdf',
                    modalMessage: "Obteniendo reporte de visitantes"
                })
            };


            loadAll();

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

            function consult() {
                vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + companyUser.companyId + '/' + globalCompany.getHouseId();
                vm.isReady = false;

                if (vm.userType == 1) {
                    Visitant.findBetweenDatesByHouse({
                        initial_time: moment(vm.dates.initial_time).format(),
                        final_time: moment(vm.dates.final_time).format(),
                        houseId: globalCompany.getHouseId()
                    }).$promise.then(onSuccess);
                } else {
                    Visitant.findBetweenDatesForAdmin({
                        initial_time: moment(vm.dates.initial_time).format(),
                        final_time: moment(vm.dates.final_time).format(),
                        companyId: globalCompany.getId(),
                    }).$promise.then(onSuccess);
                }


                function onSuccess(data) {
                    vm.visitants = data;
                    vm.page = pagingParams.page;
                    vm.title = 'Visitantes entre:';
                    vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");
                    vm.isConsulting = true;
                    formatVisitors(vm.visitants);
                    vm.isReady = true;
                    vm.showFilterDiv = false;
                }

            }

            vm.stopConsulting = function () {
                vm.isReady = false;
                vm.dates = {
                    initial_time: firstDay,
                    final_time: lastDay
                };
                vm.isConsulting = false;
                vm.showFilterDiv = false;
                loadAll();
                vm.titleConsult = "";
            }

            function loadAll() {
                vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + companyUser.companyId + '/' + globalCompany.getHouseId();

                vm.isReady = false;

                if (vm.userType == 1) {
                    Visitant.findByHouseInLastMonth({
                        houseId: globalCompany.getHouseId(),
                    }).$promise.then(onSuccess);

                } else {
                    Visitant.findForAdminInLastMonth({
                        companyId: globalCompany.getId(),
                    }).$promise.then(onSuccess);
                }

                function onSuccess(data) {
                    vm.visitants = data;
                    vm.page = pagingParams.page;
                    vm.title = 'Visitantes del mes';
                    vm.isConsulting = false;
                    formatVisitors(vm.visitants);
                    vm.isReady = true;
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
        });
    }
})();
