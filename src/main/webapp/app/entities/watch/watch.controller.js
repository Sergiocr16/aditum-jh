(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchController', WatchController);

    WatchController.$inject = ['Watch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', '$stateParams', 'Company', '$state', 'globalCompany'];

    function WatchController(Watch, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, $stateParams, Company, $state, globalCompany) {
        var vm = this;
        vm.isReady = false;
        vm.loadPage = loadPage;
        vm.isFiltering = false;
        vm.watches = [];
        vm.itemsPerPage = 10;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        $rootScope.mainTitle = "Turnos de oficiales";
        moment.locale("es");
        $('input').attr('autocomplete', 'off');
        vm.defineTextNoContent = function () {
            if(!vm.isFiltering){
                return "No se ha registrado ningún turno aún"
            }else{
                return "No se encontró ningún turno con ese filtro"
            }
        }

        function formatWatch(watch) {
            watch.initialtime = moment(watch.initialtime).format('dddd D, h:mm a');
            if (watch.finaltime === null) {
                watch.finaltime = 'Aún en progreso'
            } else {
                watch.finaltime = moment(watch.finaltime).format('dddd D, h:mm a');
            }
            watch.totalOfficers = totalOfficer(watch);
            return watch;
        }

        function totalOfficer(watch) {
            var ids = watch.responsableofficer.split(";");
            console.log(ids)
            return ids.length - 1;
        }

        function createFiveDaysBehind() {
            vm.days = [];
            for (var i = 0; i <= 4; i++) {
                var diaAnterior = {}
                var today = new Date();
                today.setDate(today.getDate() - i);
                today.setHours(0);
                today.setMinutes(0);
                today.setSeconds(0);
                diaAnterior.fechaMinima = today;
                today.setHours(23);
                today.setMinutes(59);
                today.setSeconds(59);
                diaAnterior.fechaMaxima = today;
                diaAnterior.fecha = moment(today).format("ll")
                diaAnterior.id = i;
                vm.days.push(diaAnterior)
            }
        }

        loadAll();
        vm.loadAllNormal = function () {
            vm.initialTime = undefined;
            vm.finalTime = undefined;
            vm.isFiltering = false;
            vm.page = 0;
            vm.watches = [];
            vm.links = {
                last: 0
            };
            vm.isReady = false;

            loadAll();
        };

        vm.filter = function () {
            vm.isFiltering = true;
            vm.watches = [];
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.isReady = false;
            loadFiltered();
        };

        function sort() {
            var result = [];
            if (vm.predicate !== 'initialtime') {
                result.push('initialtime,desc');
            }
            return result;
        }

        function loadFiltered() {

            Watch.findBetweenDates({
                page: vm.page,
                size: vm.itemsPerPage,
                initial_time: moment(vm.initialTime).format(),
                final_time: moment(vm.finalTime).format(),
                companyId: globalCompany.getId(),
                sort: sort()
            }, onSuccess, onError);
        }

        function loadAll() {
            Watch.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);
        }
        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            for (var i = 0; i < data.length; i++) {
                vm.watches.push(formatWatch(data[i]));
            }
            vm.isReady = true;
        }

        function onError(error) {
            Modal.toast("Ocurrio un error consultando los turnos")
        }

        function loadPage(page) {
            vm.page = page;
            if (!vm.isFiltering) {
                loadAll();
            } else {
                loadFiltered()
            }
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.viewDetail = function (watch) {
            var encryptedId = CommonMethods.encryptIdUrl(watch.id)
            $state.go('watch-detail', {
                id: encryptedId
            })
        }

    }
})();


