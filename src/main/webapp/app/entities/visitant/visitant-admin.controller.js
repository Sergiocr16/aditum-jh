(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantAdminController', VisitantAdminController);

    VisitantAdminController.$inject = ['$mdDialog', 'Visitant', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'House', '$scope', 'globalCompany','CommonMethods'];

    function VisitantAdminController($mdDialog, Visitant, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, House, $scope, globalCompany,CommonMethods) {

        $rootScope.active = "adminVisitors";
        var vm = this;
        vm.Principal;
        $rootScope.mainTitle = "Bitácora de Visitantes";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        vm.itemsPerPage = 30;
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.first_month_day = firstDay;
        vm.houseSelected = -1;
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };
        vm.titleConsult = "Del " + moment(vm.dates.initial_time).format('d mm. yyyy') + " al " + moment(vm.dates.final_time).format("d mm. yyyy");
        vm.visitorProveedor = function(visitor){
            if(visitor == null || visitor == undefined || visitor == "" ){
                return false;
            }
            return true;
        }
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.visitants = [];
        vm.filter = {
            name: " ",
            houseId: "empty"
        };
        moment.locale("es")
        vm.open = function (ev) {
            $mdDialog.show({
                templateUrl: 'app/entities/visitant/visitors-filter.html',
                scope: $scope,
                preserveScope: true,
                targetEvent: ev
            });
        };

        vm.close = function () {
            $mdDialog.hide();
        };
        vm.closeAndFilter = function () {
            vm.filterVisitors();
            $mdDialog.hide();
        };
        vm.filterVisitors = function () {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.visitants = [];
            loadAll();
        }
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

        vm.isDisableButton = function () {
            if (vm.dates.initial_time === undefined || vm.dates.final_time === undefined) return true;
            return false;
        }

        loadHouses();


        function loadHouses() {
            House.query({companyId: globalCompany.getId()}, onSuccessHouses);
            function onSuccessHouses(data, headers) {
                vm.houses = data;
                loadAll();
            }
        }


        vm.stopConsulting = function () {
            vm.dates = {
                initial_time: firstDay,
                final_time: lastDay
            };
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.visitants = [];
            vm.filter = {
                name: "",
                houseId: "empty"
            }
            loadAll();
        }

        function sort() {
            var result = [];
            if (vm.predicate !== 'arrivaltime') {
                result.push('arrivaltime,asc');
            }
            return result;
        }

        function loadAll() {
            if (vm.filter.houseId == undefined) {
                vm.filter.houseId = "empty"
            }
            if (vm.filter.name == "" || vm.filter.name == undefined || vm.filter.name == " ") {
                vm.filterName = "empty";
            }else{
                vm.filterName = vm.filter.name;
            }
            if(vm.filter.houseId=="empty"){
                vm.houseSelected = "-1"
            }else{
                vm.houseSelected = vm.filter.houseId;
            }
            vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + '/' + vm.houseSelected;
            vm.titleConsult = "Del " + moment(vm.dates.initial_time).format('d MMM YYYY') + " al " + moment(vm.dates.final_time).format("d MMM YYYY");
            Visitant.findByFilter({
                name: vm.filterName,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: CommonMethods.encryptS(globalCompany.getId()),
                houseId: CommonMethods.encryptS(vm.filter.houseId)
            }, onSuccess);

            function onSuccess(data,headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.visitants.push(data[i])
                }
                vm.isReady = true;
            }
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
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
