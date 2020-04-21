(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WaterChargePerHouseController', WaterChargePerHouseController);

    WaterChargePerHouseController.$inject = ['$rootScope', '$scope', '$state', 'Charge', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage', 'Modal', '$timeout', 'Principal', 'globalCompany'];

    function WaterChargePerHouseController($rootScope, $scope, $state, Charge, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage, Modal, $timeout, Principal, globalCompany) {

        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.isEditing = false;
        vm.isReady = false;
        vm.downloading = false;

        var houseId;
        Principal.identity().then(function (account) {
            vm.account = account;
            switch (account.authorities[0]) {
                case "ROLE_MANAGER":
                    $rootScope.mainTitle = "Contabilidad filiales";
                    houseId = $localStorage.houseSelected.id;
                    break;
                case "ROLE_USER":
                    $rootScope.mainTitle = "Cuotas de agua vigentes";
                    $rootScope.active = "waterChargesResidentAccount";
                    houseId = globalCompany.getHouseId();
                    break;
                case "ROLE_OWNER":
                    $rootScope.mainTitle = "Cuotas de agua vigentes";
                    $rootScope.active = "waterChargesResidentAccount";
                    houseId = globalCompany.getHouseId();
                    break;
            }
            loadAll();
        })

        vm.download = function () {
            vm.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.downloading = false;
                })
            }, 1000)
        };


        vm.datePassed = function (cuota) {
            var rightNow = new Date();
            var chargeDate = new Date(moment(cuota.date))
            return ((chargeDate.getTime() > rightNow.getTime()))
        }

        vm.validCharges = function () {

            var invalido = 0;
            angular.forEach(vm.charges, function (val, index) {
                for (var i = 0; i < vm.charges.length; i++) {
                    if (val.ammount == 0) {
                        invalido++;
                    }
                }
            })
            if (invalido == 0) {
                return true;
            } else {
                return false
            }
        }


        vm.getCategory = function (type) {
            switch (type) {
                case "1":
                    return "MANTENIMIENTO"
                    break;
                case "2":
                    return "EXTRAORDINARIA"
                    break;
                case "3":
                    return "ÁREAS COMUNES"
                    break;
                case "5":
                    return "MULTA"
                    break;
                case "6":
                    return "CUOTA AGUA"
                    break;
            }
        }

        vm.openCalendar = function (charge) {

            charge.openDate = true;

        }

        function loadAll() {
            Charge.waterChargeByHouse({
                houseId: houseId,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [];
                if (vm.predicate !== 'date') {
                    result.push('date,desc');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                var countPassedDate = 0;
                var rightNow = new Date();
                angular.forEach(data, function (cuota, i) {
                    cuota.openDate = false;
                    cuota.type = cuota.type + ""
                    var chargeDate = new Date(moment(cuota.date))
                    if (chargeDate.getTime() > rightNow.getTime()) {
                        cuota.datePassed = true;
                        if (countPassedDate == 0) {
                            cuota.definedFirstDatePassed = true;
                            countPassedDate++;
                        }
                    }
                    cuota.temporalAmmount = cuota.ammount;
                });
                vm.charges = data;
                vm.page = pagingParams.page;
                vm.isReady = true;
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
    }
})();
