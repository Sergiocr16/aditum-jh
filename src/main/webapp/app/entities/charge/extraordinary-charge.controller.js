(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ExtraordinaryChargeController', ExtraordinaryChargeController);

    ExtraordinaryChargeController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods'];

    function ExtraordinaryChargeController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.verificando = false;
        vm.selectedAll = false;
        vm.globalConcept = {
            date: "",
            text: undefined,
            type: 2
        };
        moment.locale("es");

        vm.selectAll = function() {
            angular.forEach(vm.houses, function(house, i) {
                if (vm.selectedAll == true && house.isdesocupated == 0) {
                    house.isIncluded = true;
                } else {
                    house.isIncluded = false;
                }
            })
        }

        vm.globalConceptSelected = function() {
            if (vm.globalConcept.text != undefined) {
                bootbox.confirm({
                    message: "¿Está seguro que desea modificar el concepto de todas las cuotas?",
                    buttons: {
                        confirm: {
                            label: 'Aceptar',
                            className: 'btn-success'
                        },
                        cancel: {
                            label: 'Cancelar',
                            className: 'btn-danger'
                        }
                    },
                    callback: function(result) {
                        console.log(vm.globalConcept.text)
                        if (result) {
                            $scope.$apply(function() {
                                angular.forEach(vm.houses, function(house, i) {

                                    house.cuota.concept = vm.globalConcept.text;

                                })
                            })

                        }
                    }
                });
            }
        }
        vm.validate = function(cuota) {
            var s = cuota.ammount;
            var caracteres = ['"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]
            var invalido = 0;
            angular.forEach(caracteres, function(val, index) {
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
        setTimeout(function() {
            loadAll();
        }, 1500)
        vm.verificarCargos = function() {
            var invalid = 0;
            vm.selectedHouses = [];
            angular.forEach(vm.houses, function(house, key) {
                if (house.isIncluded == true) {
                    vm.selectedHouses.push(house)
                }
            })
            angular.forEach(vm.selectedHouses, function(house, key) {
                if (house.cuota.valida == false) {
                    invalid++;
                }
            })

            if (vm.selectedHouses.length == 0) {
                toastr["error"]("Debe de seleccionar almenos una casa para realizar una cuota.")
            } else {
                if (invalid == 0) {
                    vm.verificando = true;
                } else {
                    toastr["error"]("Porfavor verifica las cuotas ingresadas")
                }
            }
        }
        vm.cancelar = function() {
            vm.verificando = false;
            console.log(vm.verificando)
        }

        function buildCharge(house) {
            house.cuota.houseId = parseInt(house.id);
            house.cuota.type = vm.globalConcept.type;
            house.cuota.date = vm.globalConcept.date;
            house.cuota.state = 1;
            house.cuota.deleted = 0;
            return house.cuota;
        }

        vm.createDues = function() {
            angular.forEach(vm.selectedHouses, function(house, i) {
                if (house.cuota.ammount != 0) {
                    Charge.save(buildCharge(house), function(result) {})
                }
            })
        }

        function loadAll() {
            House.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: $rootScope.companyId
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
                    companyId: $rootScope.companyId
                }).$promise.then(function(result) {
                    vm.adminConfig = result;

                })

                vm.globalConcept = {
                    date: "",
                    text: undefined,
                    type: "2"
                };
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    if (value.isdesocupated == 1) {
                        value.cuota = ({
                            ammount: 0,
                            concept: ""
                        })
                    } else {
                        if (value.due == undefined) {
                            value.cuota = ({
                                ammount: 0,
                                concept: ""
                            })

                        } else {
                            value.cuota = ({
                                ammount: 0,
                                concept: ""
                            })

                        }
                    }
                    value.isIncluded = false;
                })
                vm.houses = data;

                vm.page = pagingParams.page;
                setTimeout(function() {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#tableData").fadeIn('slow');
                }, 700)
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
