(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseAdministrationController', HouseAdministrationController);

    HouseAdministrationController.$inject = ['$localStorage', '$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'House', '$scope','globalCompany'];

    function HouseAdministrationController($localStorage, $state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, House, $scope, globalCompany) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;

        $rootScope.mainTitle = "Contabilidad filiales";
        vm.isReady = false;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $rootScope.active = "houseAdministration";
        vm.expanding = false;

        loadAll();

        function loadAll() {
            Balance.queryBalances({
                companyId: globalCompany.getId()
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
                angular.forEach(data, function (value, key) {
                    value.debit = value.balance.debit;
                    if ($localStorage.houseSelected != null || $localStorage.houseSelected != undefined) {
                        if(value.id == $localStorage.houseSelected.id ){
                            vm.selectedIndex = key;
                        }
                    }
                })
                vm.houses = data;
                if (vm.houses.length > 0 && $localStorage.houseSelected == null || $localStorage.houseSelected == undefined) {
                    $localStorage.houseSelected = vm.houses[0]
                }
                if ($localStorage.houseSelected != null || $localStorage.houseSelected != undefined) {

                    House.get({
                        id: $localStorage.houseSelected.id
                    }, function (result) {
                        $localStorage.houseSelected = result
                        vm.house = $localStorage.houseSelected;
                        $rootScope.houseSelected = $localStorage.houseSelected;
                    })
                } else {
                    if (vm.houses.length > 0) {
                        $rootScope.houseSelected = vm.houses[0]
                        $localStorage.houseSelected = vm.houses[0]
                        vm.house = $rootScope.houseSelected;
                    }
                }
                vm.page = pagingParams.page;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.expand = function () {

            setTimeout(function () {
                $scope.$apply(function () {
                    vm.expanding = !vm.expanding;
                });
            }, 200);

        }
        vm.defineBalanceClass = function (balance) {
            var b = parseInt(balance);
            if (b != 0) {
                if (b > 0) {
                    return "greenBalance";
                } else {
                    return "redBalance";
                }
            }
        }
        vm.defineBalanceTotalClass = function (balance) {
            var b = parseInt(balance);
            if (b != 0) {
                if (b > 0) {
                    return "deuda-total-positiva";
                } else {
                    return "deuda-total-negativa";
                }
            } else {
                return "deuda-total";
            }
        }

        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }
        vm.changeHouse = function (house,i) {
            House.get({
                id: house.id
            }, function (result) {
                $localStorage.houseSelected = result
                $rootScope.houseSelected = result;
                vm.house = result;
                vm.selectedIndex = i;
            })

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
