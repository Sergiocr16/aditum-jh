(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('IndividualChargeController', IndividualChargeController);

    IndividualChargeController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods'];

    function IndividualChargeController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods) {
        var vm = this;
         $rootScope.active = 'individual';
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.houses = [];
        vm.minDate = new Date();
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

        vm.validate = function(cuota) {
            var s = cuota.ammount;
            var caracteres = ['{','}','[',']','"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]
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
                vm.charge.valida = true;
            } else {
                vm.charge.valida = false
            }
        }
        setTimeout(function() {
            loadAll();
        }, 1500)

        angular.element(document).ready(function() {
            $('.dating').keydown(function() {
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
        vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }

        vm.encontrarHouseNumber = function(houseId) {
            var housenumber = undefined;
            angular.forEach(vm.houses, function(house, i) {
                if (house.id == houseId) {
                    housenumber = house.housenumber;
                }
            })
            return housenumber;
        }
        vm.crearCuota = function() {
            if (vm.charge.ammount != 0) {
                bootbox.confirm({
                    message: "¿Está seguro que generar una cuota por ₡" + vm.formatearNumero(vm.charge.ammount) + " a la filial " + vm.encontrarHouseNumber(vm.selectedHouse) + "?",
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
                        if (result) {
                            vm.isSaving == true;
                            vm.charge.houseId = parseInt(vm.selectedHouse)
                            Charge.save(vm.charge, function(result) {
                                vm.isSaving == false;
                                toastr["success"]("Se ha generado la cuota correctamente.")
                            })


                        }
                    }
                });
            } else {
                toastr["error"]("Para generar una cuota su monto debe de ser mayor a ₡ 0.00")
            }
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


                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    if (value.isdesocupated == 0) {
                        vm.houses.push(value)
                    }

                })
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
