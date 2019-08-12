(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroVisitController', MacroVisitController);

    MacroVisitController.$inject = ['MacroCondominium','$mdDialog', 'MacroVisit', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'House', '$scope', 'globalCompany'];

    function MacroVisitController(MacroCondominium,$mdDialog, MacroVisit, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, House, $scope, globalCompany) {

        $rootScope.active = "adminMacroVisitors";
        var vm = this;
        vm.Principal;

        $rootScope.mainTitle = "Visitantes";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        vm.itemsPerPage = 12;
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.first_month_day = firstDay;
        vm.condoSelected = -1;
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
            name: " ",
            condoId: "empty"
        };


        vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");

        moment.locale("es");
        vm.open = function (ev) {
            $mdDialog.show({
                templateUrl: 'app/entities/macro-visit/visitors-condos-filter.html',
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

        loadCondos();


        function loadCondos() {
            MacroCondominium.getCondos({id: globalCompany.getMacroId()}, onSuccessHouses);
            function onSuccessHouses(data, headers) {
                vm.condos = data.companies;
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
                condoId: "empty"
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
            if (vm.filter.condoId == undefined) {
                vm.filter.condoId = "empty"
            }
            if (vm.filter.name == "" || vm.filter.name == undefined || vm.filter.name == " ") {
                vm.filterName = "empty";
            }else{
                vm.filterName = vm.filter.name;
            }
            if(vm.filter.condoId=="empty"){
                vm.condoSelected = "-1"
            }else{
                vm.condoSelected = vm.filter.condoId;
            }
            vm.path = '/api/visitants/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + '/' + vm.houseSelected;
            vm.titleConsult = moment(vm.dates.initial_time).format('LL') + "   y   " + moment(vm.dates.final_time).format("LL");
            MacroVisit.findByFilter({
                name: vm.filterName,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                macroCondominiumId: globalCompany.getMacroId(),
                companyId:  vm.filter.condoId

            }, onSuccess);

            function onSuccess(data,headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.visitants.push(data[i])
                }
                if(vm.filter.condoId=="empty"){
                    vm.title = 'Visitantes entre:';
                }else{
                    for (var i = 0; i < vm.condos.length; i++) {
                        if(vm.condos[i].id==vm.filter.condoId){
                            vm.title = 'Visitantes ' + vm.condos[i].name +' entre:';
                        }
                    }

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
