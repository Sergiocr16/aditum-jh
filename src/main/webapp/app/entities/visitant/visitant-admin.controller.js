(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantAdminController', VisitantAdminController);

    VisitantAdminController.$inject = ['Visitant', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'House', '$scope', 'globalCompany'];

    function VisitantAdminController(Visitant, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, House, $scope, globalCompany) {

        $rootScope.active = "adminVisitors";
        var vm = this;
        vm.Principal;
        $rootScope.mainTitle = "Visitantes"
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.consult = consult;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
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
            console.log(vm.houseSelected)
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

        vm.isDisableButton = function () {
            if(vm.dates.initial_time === undefined || vm.dates.final_time === undefined) return true;
            return false;
        }

        angular.element(document).ready(function () {
            $('.dating').keydown(function () {
                return false;
            });
        });

        loadHouses();


        function loadHouses() {
            House.query({companyId: globalCompany.getId()}, onSuccessHouses);

            function onSuccessHouses(data, headers) {
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                vm.houses = data;
                loadAll();
            }
        }

        function loadByHouseLastMonth(house) {
            Visitant.findByHouseInLastMonth({
                houseId: house.id,
            }).$promise.then(onSuccess);
            vm.isReady = true;

            function onSuccess(data) {
                vm.visitants = data;
                vm.queryCount = data.length;
                vm.page = pagingParams.page;
                vm.title = 'Visitantes del mes';
                $rootScope.mainTitle = vm.title;

                vm.isConsulting = false;
                formatVisitors(vm.visitants);
                vm.isReady = true;
            }

        }

        function consultByHouse(house) {
            vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + '/' + vm.houseSelected;

            vm.isReady = false;
            Visitant.findBetweenDatesByHouse({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                houseId: house.id,
            }).$promise.then(onSuccess);

            function onSuccess(data) {
                vm.visitants = data;
                vm.page = pagingParams.page;
                vm.title = 'Visitantes entre:';
                vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");
                vm.isConsulting = true;
                formatVisitors(vm.visitants);
                vm.isReady = true;
            }

        }

        vm.findVisitorByHouse = function (house) {
            if (house == undefined || house == '-1') {
                vm.houseSelected = -1;
                loadAll();
            } else {
                if (vm.dates.initial_time == undefined || vm.dates.final_time == undefined) {
                    vm.houseSelected = house.id;
                    loadByHouseLastMonth(house);

                } else {
                    vm.houseSelected = house.id;
                    consultByHouse(house);
                }
            }
        }

        function formatVisitors(visitors) {
            angular.forEach(visitors, function (value, key) {
                value.fullName = value.name + " " + value.lastname + " " + value.secondlastname;
                angular.forEach(vm.houses, function (house, key) {
                    if (house.id == value.houseId) {
                        value.houseNumber = house.housenumber
                    }
                    if (value.houseId == undefined) {
                        value.houseNumber = value.responsableofficer;
                    }
                })
            })
        }


        function consult() {
            vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + '/' + vm.houseSelected;

            $scope.house = undefined;
            $("#all").fadeOut(0);
            setTimeout(function () {
                $("#loadingIcon").fadeIn(100);
            }, 200)
            if(vm.houseSelected===-1){
                Visitant.findBetweenDatesByCompany({
                    initial_time: moment(vm.dates.initial_time).format(),
                    final_time: moment(vm.dates.final_time).format(),
                    companyId: globalCompany.getId(),
                }).$promise.then(onSuccess);
            }else{
                Visitant.findBetweenDatesByHouse({
                    initial_time: moment(vm.dates.initial_time).format(),
                    final_time: moment(vm.dates.final_time).format(),
                    houseId: vm.houseSelected,
                }).$promise.then(onSuccess);

            }


            function onSuccess(data) {
                vm.visitants = data;
                vm.page = pagingParams.page;
                vm.queryCount = data.length;
                vm.title = 'Visitantes entre:';
                vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");
                vm.isConsulting = true;
                formatVisitors(vm.visitants);
                vm.isReady = true;
            }

        }

        vm.stopConsulting = function () {
            vm.showFilterDiv = false;
            vm.dates = {
                initial_time: firstDay,
                final_time: lastDay
            };
            vm.isConsulting = false;
            loadAll();
            vm.titleConsult = "";
        }

        function loadAll() {
            vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + '/' + vm.houseSelected;
            $scope.house = undefined;
            Visitant.findByCompanyInLastMonth({
                companyId: globalCompany.getId()
            }).$promise.then(onSuccess);

            function onSuccess(data) {
                vm.visitants = data;
                vm.queryCount = data.length;
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

    }
})();
