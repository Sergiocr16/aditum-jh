(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MultaChargeController', MultaChargeController);

    MultaChargeController.$inject = ['companyUser','BitacoraAcciones','$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', '$localStorage', 'globalCompany', 'Modal'];

    function MultaChargeController(companyUser,BitacoraAcciones,$state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, $localStorage, globalCompany, Modal) {
        var vm = this;
        $rootScope.active = 'multaCharge';
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        $rootScope.mainTitle = "Generar multa individual";
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.houses = [];
        vm.minDate = new Date();
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

        vm.charge = {
            type: "5",
            concept: "",
            ammount: "",
            date: "",
            valida: true,
            state: 1,
            deleted: 0
        }
        moment.locale("es");

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
                    Modal.confirmDialog("¿Está seguro que desea generar esta multa a la filial " + vm.encontrarHouseNumber(vm.selectedHouse) + "?", "",
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

                                    House.get({
                                        id: vm.selectedHouse
                                    }, function(result) {
                                        var concept = "Creación de multa individual" + ": " + vm.charge.concept + ", "+ " a la filial " + result.housenumber + " por " + vm.formatearNumero(vm.charge.ammount+"") + " colones";
                                        console.log(concept)
                                        BitacoraAcciones.save(mapBitacoraAcciones(concept), function () {});
                                    });

                                    Modal.toast("Se ha generado la cuota correctamente.");
                                    $state.go('houseAdministration.chargePerHouse');
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

        function mapBitacoraAcciones (concept){
            vm.bitacoraAcciones = {};
            vm.bitacoraAcciones.concept = concept;
            vm.bitacoraAcciones.type = 6;
            vm.bitacoraAcciones.ejecutionDate = new Date();
            vm.bitacoraAcciones.category = "Cuotas";
            vm.bitacoraAcciones.idReference = 1;
            vm.bitacoraAcciones.companyId = globalCompany.getId();

            return vm.bitacoraAcciones;

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
