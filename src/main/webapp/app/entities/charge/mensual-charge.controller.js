(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualChargeController', MensualChargeController);

    MensualChargeController.$inject = ['companyUser', 'BitacoraAcciones', '$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', 'globalCompany', 'Modal'];

    function MensualChargeController(companyUser, BitacoraAcciones, $state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, globalCompany, Modal) {
        var vm = this;
        $rootScope.active = 'mensual';
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.radiostatus = true;
        vm.cuotaFija = true;
        vm.isReady = false;
        $rootScope.mainTitle = "Generar Cuota mensual";
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        vm.verificando = false;
        angular.element(document).ready(function () {


        });

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
                cuota.valida = true;
            } else {
                cuota.valida = false
            }
        }
        loadAll();
        vm.verificarCargos = function () {
            var invalid = 0;
            angular.forEach(vm.houses, function (house, key) {
                angular.forEach(house.cuotas, function (cuota, key) {
                    if (cuota.valida == false) {
                        invalid++;
                    }
                })
            })
            if (invalid == 0) {
                vm.verificando = true;
            } else {
                Modal.toast("Porfavor verifica las cuotas ingresadas")
            }
        }
        vm.cancelar = function () {
            vm.verificando = false;
        }
        vm.addNewDue = function () {
            vm.globalConceptNumber++;
            vm.globalConcept.push({
                date: "",
                concept: "",
                id: vm.globalConceptNumber,
                datePickerOpenStatus: false
            })
            angular.forEach(vm.houses, function (value, key) {
                if (value.isdesocupated == 1) {
                    value.cuotas.push({
                        ammount: 0,
                        globalConcept: vm.globalConceptNumber
                    })
                } else {
                    if (value.due == undefined && vm.cuotaFija == true) {
                        value.cuotas.push({
                            ammount: 0,
                            globalConcept: vm.globalConceptNumber,
                            valida: true
                        })

                    } else {
                        if (vm.cuotaFija == false) {
                            value.cuotas.push({
                                ammount: value.squareMeters * vm.adminConfig.squareMetersPrice,
                                globalConcept: vm.globalConceptNumber
                            })
                        } else {
                            value.cuotas.push({
                                ammount: value.due,
                                globalConcept: vm.globalConceptNumber
                            })
                        }


                    }
                }
            })
        }

        vm.definirCuotaFija = function () {
            vm.cuotaFija = true;
            angular.forEach(vm.houses, function (house, key) {
                angular.forEach(house.cuotas, function (cuota, key) {
                    if (house.isdesocupated == 1) {
                        cuota.ammount = 0;

                    } else {
                        cuota.ammount = house.due;

                    }
                })
            })
        }
        vm.definirCuotaMetroCuadrado = function () {
            vm.cuotaFija = false;
            angular.forEach(vm.houses, function (house, key) {
                angular.forEach(house.cuotas, function (cuota, key) {
                    if (house.isdesocupated == 1) {
                        cuota.ammount = 0;

                    } else {
                        cuota.ammount = house.squareMeters * vm.adminConfig.squareMetersPrice;


                    }
                })
            })
        }

        function getGlobalConcept(gc) {
            var globalFounded = {};
            angular.forEach(vm.globalConcept, function (globalConcept, i) {
                if (gc == globalConcept.id) {
                    globalFounded = globalConcept;
                }
            })
            return globalFounded;
        }

        function buildCharge(cuota, house) {

            cuota.houseId = parseInt(house.id);
            cuota.type = 1;
            cuota.date = getGlobalConcept(cuota.globalConcept).date;
            cuota.concept = getGlobalConcept(cuota.globalConcept).concept;
            cuota.state = 1;
            cuota.deleted = 0;
            cuota.companyId = globalCompany.getId();
            return cuota;
        }

        vm.createDues = function () {
            Modal.showLoadingBar();
            var selectedHouses = "";

            function createCharge(houseNumber, cuotaNumber) {
                console.log("holis")
                var cuota = vm.houses[houseNumber].cuotas[cuotaNumber];
                var cuotaNumber = cuotaNumber;
                var house = vm.houses[houseNumber]
                if (cuota.ammount != 0) {

                    Charge.save(buildCharge(cuota, house), function (result) {
                        selectedHouses = selectedHouses + house.housenumber + ", ";
                        if (house.cuotas.length - 1 > cuotaNumber) {
                            createCharge(houseNumber, cuotaNumber + 1)
                        } else {
                            if (vm.houses.length - 1 > houseNumber) {
                                chargesPerHouse(houseNumber + 1)
                            } else {
                                $state.go('mensualCharge', null, {
                                    reload: true
                                });
                                angular.forEach(vm.globalConcept, function (value, key) {
                                    var housesSelected = "";
                                    angular.forEach(vm.houses, function (house, key) {
                                        angular.forEach(house.cuotas, function (cuota, key) {
                                            if (cuota.ammount > 0 && vm.globalConcept[cuota.globalConcept].id == value.id) {
                                                housesSelected = housesSelected + house.housenumber + ",";
                                            }
                                        });
                                    });
                                    var concept = "Creación de cuota mensual" + ": " + value.concept +", a las filiales " + housesSelected;
                                    BitacoraAcciones.save(mapBitacoraAcciones(concept), function () {
                                    });

                                });

                                Modal.hideLoadingBar();
                                Modal.toast("Se generaron las cuotas correctamente.")
                            }
                        }
                    }, function () {
                        Modal.hideLoadingBar();
                        Modal.toast("Hubo un error al crear las cuotas mensuales, por favor verificar los datos ingresados.")
                    })
                } else {
                    if (house.cuotas.length - 1 > cuotaNumber) {
                        createCharge(houseNumber, cuotaNumber + 1)
                    } else {
                        if (vm.houses.length - 1 > houseNumber) {
                            chargesPerHouse(houseNumber + 1)
                        } else {
                            $state.go('mensualCharge', null, {
                                reload: true
                            });
                            Modal.hideLoadingBar();
                            angular.forEach(vm.globalConcept, function (value, key) {
                                var housesSelected = "";
                                angular.forEach(vm.houses, function (house, key) {
                                    angular.forEach(house.cuotas, function (cuota, key) {
                                        if (cuota.ammount > 0 && vm.globalConcept[cuota.globalConcept].id == value.id) {
                                            housesSelected = housesSelected + house.housenumber + ",";
                                        }
                                    });
                                });
                                var concept = "Creación de cuota mensual" + ": " + value.concept +", a las filiales " + housesSelected;
                                BitacoraAcciones.save(mapBitacoraAcciones(concept), function () {
                                });

                            });
                            Modal.toast("Se generaron las cuotas correctamente.")
                        }
                    }

                }

            }

            function chargesPerHouse(houseNumber) {
                var cuotaNumber = 0;
                createCharge(houseNumber, cuotaNumber)
            }

            chargesPerHouse(0)
        }


        function mapBitacoraAcciones(concept) {
            vm.bitacoraAcciones = {};
            vm.bitacoraAcciones.concept = concept.substring(0, concept.length - 1);
            vm.bitacoraAcciones.type = 6;
            vm.bitacoraAcciones.ejecutionDate = new Date();
            vm.bitacoraAcciones.category = "Cuotas";
            vm.bitacoraAcciones.idReference = 1;
            vm.bitacoraAcciones.companyId = globalCompany.getId();

            return vm.bitacoraAcciones;

        }

        vm.autoConcept = function (globalConcept) {
            String.prototype.capitalize = function () {
                return this.replace(/(?:^|\s)\S/g, function (a) {
                    return a.toUpperCase();
                });
            };

            globalConcept.concept = "Mantenimiento " + moment(globalConcept.date).format("MMMM").capitalize() + " " + moment(globalConcept.date).format("YYYY");

        }
        vm.deleteDue = function (id) {
            Modal.confirmDialog("¿Está seguro que desea eliminar esta columna?", "",
                function () {
                    angular.forEach(vm.globalConcept, function (value, key) {
                        if (value.id == id) {
                            vm.globalConcept.splice(key, 1);
                        }
                    })

                    angular.forEach(vm.houses, function (value, key) {
                        angular.forEach(value.cuotas, function (cuota, key) {
                            if (cuota.globalConcept == id) {
                                value.cuotas.splice(key, 1);
                            }
                        })
                    })

                });


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
                $('.dating').keydown(function () {
                    return false;
                });
                $('.numbers').keypress(function (tecla) {
                    if (tecla.charCode < 48 || tecla.charCode > 57) return false;
                });
                AdministrationConfiguration.get({
                    companyId: globalCompany.getId()
                }).$promise.then(function (result) {
                    vm.adminConfig = result;

                })
                vm.globalConceptNumber = 0;
                vm.globalConcept = [{
                    date: "",
                    concept: "",
                    id: vm.globalConceptNumber,
                    datePickerOpenStatus: false
                }];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function (value, key) {
                    value.cuotas = [];
                    if (value.isdesocupated == 1) {
                        value.cuotas.push({
                            ammount: 0,
                            globalConcept: vm.globalConceptNumber
                        })
                    } else {
                        if (value.due == undefined) {
                            value.cuotas.push({
                                ammount: 0,
                                globalConcept: vm.globalConceptNumber
                            })

                        } else {
                            value.cuotas.push({
                                ammount: value.due,
                                globalConcept: vm.globalConceptNumber
                            })

                        }
                    }
                })
                vm.houses = data;
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
            vm.globalConcept[index].datePickerOpenStatus = true;
        }
    }
})();
