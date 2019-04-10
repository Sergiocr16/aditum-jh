(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PresupuestoDetailController', PresupuestoDetailController);

    PresupuestoDetailController.$inject = ['$state', 'DetallePresupuesto', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Presupuesto', '$localStorage','Modal'];

    function PresupuestoDetailController($state, DetallePresupuesto, $scope, $rootScope, $stateParams, previousState, entity, Presupuesto, $localStorage,Modal) {
        var vm = this;
        console.log('hola');
        $rootScope.active = "presupuestos";
        vm.presupuesto = entity;
        vm.previousState = previousState.name;
        vm.ingressCategories = [];
        vm.isReady = false;
        $rootScope.mainTitle = "PRESUPUESTO " + vm.presupuesto.anno;
        vm.egressCategories = [];
        var invalidInputs = 0;
        var inputsFullQuantity = 0;
        vm.expanding = false;
        DetallePresupuesto.getCategoriesByBudget({budgetId: vm.presupuesto.id}, onSuccess, onError);
        vm.totalEgressValue = 0;
        vm.totalIngressValue = 0;

        function onSuccess(data) {
            vm.budgetCategories = data;
            vm.totalIngressByMonth = [];
            vm.totalEgressByMonth = [];
            for (var i = 0; i < 12; i++) {
                var month1 = {month: i, valuePerMonth: 0}
                var month2 = {month: i, valuePerMonth: 0}
                vm.totalEgressByMonth.push(month1)
                vm.totalIngressByMonth.push(month2)
            }
            angular.forEach(data, function (item, key) {
                item.valuesPerMonth = []
                item.total = 0;
                var values = item.valuePerMonth.split(",");
                for (var i = 0; i < 12; i++) {
                    var monthValues = {month: i + 1, valuePerMonth: values[i]}
                    item.valuesPerMonth.push(monthValues)
                    item.total = item.total + parseInt(values[i]);
                    if (item.type == 1) {
                        vm.totalIngressByMonth[i].valuePerMonth = vm.totalIngressByMonth[i].valuePerMonth + parseInt(values[i]);
                    } else if (item.type == 2) {
                        vm.totalEgressByMonth[i].valuePerMonth = vm.totalEgressByMonth[i].valuePerMonth + parseInt(values[i]);
                    }

                }
                if (item.type == 1) {
                    vm.ingressCategories.push(item);
                    vm.totalIngressValue = vm.totalIngressValue + item.total;
                } else {
                    vm.egressCategories.push(item);
                    vm.totalEgressValue = vm.totalEgressValue + item.total;
                }

            })
            if ($localStorage.budgetAction == 1) {
                vm.budgetAction = 1;
            } else {
                vm.budgetAction = 2;
            }
            vm.isReady = true;
        };
        vm.expand = function () {

            setTimeout(function () {
                $scope.$apply(function () {
                    vm.expanding = !vm.expanding;
                });
            }, 200);

        }
        vm.eliminateZero = function (item) {
            if (item.valuePerMonth == "0") {
                item.valuePerMonth = "";
            }

        }
        vm.putZero = function (item) {
            if (item.valuePerMonth == "" || item.valuePerMonth == null || item.valuePerMonth == undefined) {
                item.valuePerMonth = "0";
            }

        }


        vm.setTotalIngressByMonth = function (index, month) {
            if (vm.hasLettersOrSpecial(month.valuePerMonth)) {
                month.valido = false;
            } else {
                month.valido = true;
            }
            vm.totalIngressByMonth[index].valuePerMonth = 0;
            angular.forEach(vm.budgetCategories, function (item, key1) {
                if (item.type == 1) {

                    angular.forEach(item.valuesPerMonth, function (value, key2) {
                        if (index == key2) {
                            var value = value.valuePerMonth;
                            if (value == "" || value == undefined) {
                                value = "0";
                            }
                            vm.totalIngressByMonth[key2].valuePerMonth = vm.totalIngressByMonth[key2].valuePerMonth + parseInt(value);
                        }
                    })
                }
            })

        }

        vm.setTotalEgressByMonth = function (index, month) {
            if (vm.hasLettersOrSpecial(month.valuePerMonth)) {
                month.valido = false;
            } else {
                month.valido = true;
            }
            vm.totalEgressByMonth[index].valuePerMonth = 0;
            angular.forEach(vm.budgetCategories, function (item, key1) {
                if (item.type == 2) {
                    angular.forEach(item.valuesPerMonth, function (value, key2) {
                        if (index == key2) {
                            var value = value.valuePerMonth;
                            if (value == "" || value == undefined) {
                                value = "0";
                            }
                            vm.totalEgressByMonth[key2].valuePerMonth = vm.totalEgressByMonth[key2].valuePerMonth + parseInt(value);
                        }
                    })
                }
            })

        }
        vm.showEditOptions = function () {
            vm.budgetAction = 2;
        }
        vm.edit = function () {

            getValuesPerMonth();
            if (invalidInputs > 0) {
                Modal.toast("No puede ingresar letras ni carácteres especiales");
            } else if (inputsFullQuantity == 0) {
                Modal.toast("Debe ingresar al menos un valor en algún campo");
            }
            else {
                vm.presupuesto.modificationDate = moment(new Date(), 'DD/MM/YYYY').toDate();


                vm.isReady= false;
                Presupuesto.update(vm.presupuesto, updateBudgetCategories, onError);
            }
        }

        function updateBudgetCategories(result) {
            vm.totalEgressValue = 0;
            vm.totalIngressValue = 0;
            angular.forEach(vm.ingressCategories, function (item, key) {
                item.total = 0;
                DetallePresupuesto.update(item);
                angular.forEach(item.valuesPerMonth, function (month, key) {
                    item.total = item.total + parseInt(month.valuePerMonth);

                })
                vm.totalIngressValue = vm.totalIngressValue + item.total;

            })
            angular.forEach(vm.egressCategories, function (item, key) {
                item.total = 0;
                DetallePresupuesto.update(item);
                angular.forEach(item.valuesPerMonth, function (month, key) {
                    item.total = item.total + parseInt(month.valuePerMonth);

                })
                vm.totalEgressValue = vm.totalEgressValue + item.total;
            })
            Modal.toast("Se ha actualizado el presupuesto correctamente");

            vm.isReady= true;
            vm.budgetAction = 1;


        }

        function getValuesPerMonth() {
            invalidInputs = 0;
            inputsFullQuantity = 0;
            angular.forEach(vm.ingressCategories, function (item, key) {
                item.valuePerMonth = "";
                angular.forEach(item.valuesPerMonth, function (value, key) {
                    if (vm.hasLettersOrSpecial(value.valuePerMonth)) {
                        invalidInputs++;
                    }
                    item.valuePerMonth = item.valuePerMonth + sortMonthValues(value)
                })
            })
            angular.forEach(vm.egressCategories, function (item, key) {
                item.valuePerMonth = "";
                angular.forEach(item.valuesPerMonth, function (value, key) {
                    if (vm.hasLettersOrSpecial(value.valuePerMonth)) {
                        invalidInputs++;
                    }
                    item.valuePerMonth = item.valuePerMonth + sortMonthValues(value)
                })


            })
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

        function sortMonthValues(item) {
            var valuePerMonth = "";
            console.log(item.valuesPerMonth);
            if (item.valuePerMonth == "" || item.valuePerMonth == undefined || item.valuePerMonth == "0" || item.valuePerMonth == " ") {
                valuePerMonth = "0" + ","

            } else {
                inputsFullQuantity++;
                valuePerMonth = item.valuePerMonth + ","
            }
            return valuePerMonth;
        }

        vm.hasLettersOrSpecial = function (s) {

            var caracteres = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "´ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^"]
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
                return false;
            } else {
                return true;
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        var unsubscribe = $rootScope.$on('aditumApp:presupuestoUpdate', function (event, result) {
            vm.presupuesto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
