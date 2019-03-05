(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('IndividualChargeController', IndividualChargeController);

    IndividualChargeController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', '$localStorage', 'globalCompany', 'Modal'];

    function IndividualChargeController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, $localStorage, globalCompany, Modal) {
        var vm = this;
        $rootScope.active = 'individual';
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        $rootScope.mainTitle = "Generar Cuota individual";
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.houses = [];
        vm.minDate = new Date();
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

        vm.charge = {
            type: "1",
            concept: "",
            ammount: "",
            date: "",
            valida: true,
            state: 1,
            deleted: 0
        }
        moment.locale("es");
        vm.autoConcept = function () {
            if (vm.charge.type == "1") {
                vm.charge.concept = "";
                String.prototype.capitalize = function () {
                    return this.replace(/(?:^|\s)\S/g, function (a) {
                        return a.toUpperCase();
                    });
                };
                vm.charge.concept = "Mantenimiento " + moment(vm.charge.date).format("MMMM").capitalize() + " " + moment(vm.charge.date).format("YYYY");
            }
        }
        vm.validate = function (cuota) {
            var s = cuota.ammount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase()) {

                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                vm.charge.valida = true;
            } else {
                vm.charge.valida = false
            }
        }
        loadAll();

        angular.element(document).ready(function () {
            $('.dating').keydown(function () {
                return false;
            });
        });

        function buildCharge(house) {
            house.cuota.houseId = parseInt(house.id);
            house.cuota.type = vm.globalConcept.type;
            house.cuota.date = vm.globalConcept.date;
            house.cuota.state = 1;
            house.cuota.deleted = 0;
            return house.cuota;
        }


        vm.encontrarHouseNumber = function (houseId) {
            var housenumber = undefined;
            angular.forEach(vm.houses, function (house, i) {
                if (house.id == houseId) {
                    housenumber = house.housenumber;
                }
            });
            return housenumber;
        };
        vm.crearCuota = function () {
            if (vm.charge.ammount != 0) {
                if (vm.charge.valida) {
                    Modal.confirmDialog("¿Está seguro que desea generar esta cuota a la filial " + vm.encontrarHouseNumber(vm.selectedHouse) + "?", "",
                        function () {
                            vm.isSaving == true;
                            vm.charge.houseId = parseInt(vm.selectedHouse)
                            vm.charge.companyId = globalCompany.getId();
                            Modal.showLoadingBar();
                            Charge.save(vm.charge, function (result) {
                                vm.isSaving == false;
                                House.get({
                                    id: result.houseId
                                }, onSuccess)

                                function onSuccess(house) {
                                    Modal.hideLoadingBar();
                                    Modal.toast("Se ha generado la cuota correctamente.")
                                    $state.go('houseAdministration.chargePerHouse')
                                    $rootScope.houseSelected = house;
                                    $localStorage.houseSelected = house;
                                }
                            })
                        });
                } else {
                    Modal.toast("No puede ingresar carácteres especiales")
                }

            } else {
                Modal.toast("Para generar una cuota su monto debe de ser mayor a ₡ 0.00")
            }
        }

        function loadAll() {
            House.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
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
                AdministrationConfiguration.get({
                    companyId: globalCompany.getId()
                }).$promise.then(function (result) {
                    vm.adminConfig = result;

                })


                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function (value, key) {
                    if (value.isdesocupated == 0) {
                        vm.houses.push(value)
                    }

                })
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

        function openCalendar(index) {
            vm.datePickerOpenStatus = true;
        }

    }
})();
