(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('ConfigureChargesController', ConfigureChargesController);

        ConfigureChargesController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', 'globalCompany', 'Modal'];

        function ConfigureChargesController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, globalCompany, Modal) {
            var vm = this;

            $rootScope.active = "configureCharges";
            vm.loadPage = loadPage;
            vm.predicate = pagingParams.predicate;
            vm.reverse = pagingParams.ascending;
            vm.transition = transition;
            vm.radiostatus = true;
            $rootScope.mainTitle = "Configurar cuotas y Área metros cuadrados";
            vm.isReady = false;
            vm.cuotaFija = true;
            vm.datePickerOpenStatus = {};
            vm.openCalendar = openCalendar;
            vm.itemsPerPage = paginationConstants.itemsPerPage;
            vm.verificando = false;
            moment.locale("es");


            vm.saveAdminConfig = function () {
                vm.adminConfig.exchangeRateDate = moment().format();
                vm.formatCurrencyToPay();
                if (vm.adminConfig.id !== null) {
                    AdministrationConfiguration.update(vm.adminConfig, function (result) {
                        console.log("Listo")
                    }, function () {
                    });
                } else {
                    AdministrationConfiguration.save(vm.adminConfig, function () {
                        console.log("Listo")
                    }, function () {
                    });
                }
            }


            vm.formatCurrencyToPay = function () {
                for (var i = 0; i < vm.houses.length; i++) {
                    if (vm.adminConfig.chargesCreateCurrency != vm.adminConfig.chargesCollectCurrency) {

                        vm.houses[i].dueCollect = "0";
                        if (vm.adminConfig.chargesCollectCurrency == "₡" && vm.adminConfig.chargesCreateCurrency == "$") {
                            vm.houses[i].dueCollect = vm.houses[i].due * vm.adminConfig.exchangeRate;
                        }
                        if (vm.adminConfig.chargesCollectCurrency == "$" && vm.adminConfig.chargesCreateCurrency == "₡") {
                            vm.houses[i].dueCollect = vm.houses[i].due / vm.adminConfig.exchangeRate;
                        }
                    } else {
                        vm.houses[i].dueCollect = vm.houses[i].due
                    }
                }
            }

            vm.formatearNumero = function (nStr) {
                var x = nStr.split('.');
                var x1 = x[0];
                var x2 = x.length > 1 ? ',' + x[1] : '';
                var rgx = /(\d+)(\d{3})/;
                while (rgx.test(x1)) {
                    x1 = x1.replace(rgx, '$1' + '.' + '$2');
                }
                return x1 + x2;
            }
            vm.totalToPay = function () {
                var totalToPay = 0;
                for (var i = 0; i < vm.houses.length; i++) {
                    var house = vm.houses[i];
                    if (house.isdesocupated == 0) {
                        totalToPay += parseFloat(house.dueCollect);
                    }
                }
                return totalToPay;
            }
            vm.validate = function (house, s, t) {
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
                    if (t == 1) {
                        house.validDue = true;
                        house.dirtyDue = true;

                    } else {
                        house.validSquare = true;
                        house.dirtySquare = true;
                    }
                } else {
                    if (t == 1) {
                        house.validDue = false;
                        house.dirtyDue = false;
                    } else {
                        house.validSquare = false;

                        house.dirtySquare = false;
                    }
                }
            }
            loadAll();

            vm.saveHouse = function (house, t) {
                if (house.validDue == true && house.validSquare == true) {
                    house.dueCollect = house.due * vm.adminConfig.exchangeRate;
                    if (t == 1) {
                        if (house.dirtyDue == true) {
                            House.update(house, function (result) {
                                Modal.toast("Guardado.")
                                house.dirtyDue = false;
                            })
                        }
                    } else {
                        if (house.dirtySquare == true) {
                            House.update(house, function (result) {
                                Modal.toast("Guardado.")
                                house.dirtySquare = false;
                            })
                        }
                    }

                } else {
                    Modal.toast("Debe de ingresar solo números.")
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
                        if (vm.adminConfig.chargesCollectCurrency == null) {
                            vm.adminConfig.chargesCollectCurrency = "₡";
                        }
                        if (vm.adminConfig.chargesCreateCurrency == null) {
                            vm.adminConfig.chargesCreateCurrency = "₡";
                        }
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
                            value.validDue = true;
                            value.validSquare = true;
                            value.dirtyDue = false;
                            value.dirtySquare = false;
                        })
                        vm.houses = data;
                        vm.page = pagingParams.page;
                        vm.isReady = true;
                        vm.formatCurrencyToPay();
                    })
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
    }

)();
