(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargePerHouseController', ChargePerHouseController);

    ChargePerHouseController.$inject = ['$rootScope', '$scope', '$state', 'Charge', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage', 'Modal', '$timeout', 'Principal'];

    function ChargePerHouseController($rootScope, $scope, $state, Charge, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage, Modal, $timeout, Principal) {

        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.isEditing = false;
        vm.isReady = false;
        Principal.identity().then(function (account) {
            vm.account = account;
            switch (account.authorities[0]) {
                case "ROLE_MANAGER":
                    $rootScope.mainTitle = "Contabilidad filiales";
                    break;
                case "ROLE_USER":
                    $rootScope.mainTitle = "Deudas vigentes";
                    $rootScope.active = "chargesResidentAccount";
                    break;
            }
        })
        loadAll();

        vm.createPayment = function () {
            $state.go('generatePayment')
        }
        vm.datePassed = function (cuota) {
            var rightNow = new Date();
            var chargeDate = new Date(moment(cuota.date))
            return ((chargeDate.getTime() > rightNow.getTime()))
        }

        vm.edit = function () {
            var result = {};

            function updateCharge(chargeNumber) {
                if (chargeNumber < vm.charges.length) {
                    var cuota = vm.charges[chargeNumber];
                    if (cuota.ammount != 0) {
                        cuota.type = parseInt(cuota.type)
                        Charge.update(cuota, function (charge) {
                            result = charge;
                            updateCharge(chargeNumber + 1)
                        })
                    }
                } else {
                    House.get({
                        id: $localStorage.houseSelected.id
                    }, onSuccess)


                }

                function onSuccess(house) {
                    Modal.toast("Se han actualizado las cuotas correctamente.");
                    $rootScope.houseSelected = house;
                    $localStorage.houseSelected = house;
                    loadAll();
                    Modal.hideLoadingBar();
                    vm.isEditing = true;
                }
            }

            var allGood = 0;
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.valida == false) {
                    allGood++;
                }
            })
            if (allGood == 0) {

                Modal.confirmDialog("¿Está seguro que desea modificar las cuotas?", "",
                    function () {
                        Modal.showLoadingBar();
                        updateCharge(0)
                    });

            } else {
                Modal.toast("Alguna de las cuotas tiene un formato inválido.")
            }
        }

        vm.deleteCharge = function (charge) {

            Modal.confirmDialog("¿Está seguro que desea eliminar la cuota " + charge.concept + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    charge.deleted = 1;
                    Charge.update(charge, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        House.get({
                            id: result.houseId
                        }, onSuccess)

                        function onSuccess(house) {
                            Modal.hideLoadingBar();
                            Modal.toast("La cuota se ha eliminado correctamente.")
                            $rootScope.houseSelected = house;
                            $localStorage.houseSelected = house;
                            loadAll();
                            vm.isEditing = true;
                        }

                    }

                    function onSaveError() {

                    }
                });

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
        vm.editing = function () {

            setTimeout(function () {
                $scope.$apply(function () {
                    vm.isEditing = true;
                    $('.dating').keydown(function () {
                        return false;
                    });
                    angular.forEach(vm.charges, function (charge, i) {
                        charge.date = new Date(vm.charges[i].date)
                    })
                })
            }, 100)

        }
        vm.createCharge = function () {
            $state.go('houseAdministration.chargePerHouse.new')
        }
        vm.cancel = function () {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            loadAll();
            vm.isEditing = false;
        }
        $scope.$watch(function () {
            return $rootScope.houseSelected;
        }, function () {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            loadAll();
            vm.isEditing = false;
        });
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
            }
        }

        vm.openCalendar = function (charge) {

            charge.openDate = true;

        }

        function loadAll() {
            $localStorage.houseSelected.id
            Charge.queryByHouse({
                houseId: $localStorage.houseSelected.id,
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

                angular.forEach(data, function (cuota, i) {
                    cuota.openDate = false;
                    cuota.type = cuota.type + ""
                    var rightNow = new Date();
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
