(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseChargeController', HouseChargeController);

    HouseChargeController.$inject = ['entity', '$state', 'House', 'ParseLinks', 'AlertService', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', '$uibModalInstance'];

    function HouseChargeController(entity, $state, House, ParseLinks, AlertService, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, $uibModalInstance) {
        var vm = this;
        vm.loadPage = loadPage;

        vm.transition = transition;
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        vm.house = entity;
        console.log(vm.house)
        vm.clear = clear;
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
        vm.autoConcept = function() {
        if(vm.charge.type=="1"){
        vm.charge.concept = "";
            String.prototype.capitalize = function() {
                return this.replace(/(?:^|\s)\S/g, function(a) {
                    return a.toUpperCase();
                });
            };
            vm.charge.concept = "Mantenimiento " + moment(vm.charge.date).format("MMMM").capitalize() + " " + moment(vm.charge.date).format("YYYY");
}
        }
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


        angular.element(document).ready(function() {
            $('.dating').keydown(function() {
                return false;
            });
        });

        function buildCharge(charge) {
            charge.houseId = parseInt(vm.house.id);
            charge.state = 1;
            charge.deleted = 0;
            return charge;
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

        vm.crearCuota = function() {
            if (vm.charge.valida) {
                if (vm.charge.ammount != 0) {
                    bootbox.confirm({
                        message: "¿Está seguro que generar una cuota por ₡" + vm.formatearNumero(vm.charge.ammount) + " a la filial " + vm.house.housenumber + "?",
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
                                vm.isSaving = true;
                                vm.charge = buildCharge(vm.charge)
                                CommonMethods.waitingMessage();
                                Charge.save(vm.charge, function(result) {
                                    vm.isSaving = false;

                                    $uibModalInstance.close(result);
                                    bootbox.hideAll();
                                    toastr["success"]("Se ha generado la cuota correctamente.")
                                })
                            }
                        }
                    });
                } else {
                    toastr["error"]("Para generar una cuota su monto debe de ser mayor a ₡ 0.00")
                }
            } else {
                toastr["error"]("Debe de ingresar un monto de solo números.")
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

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function openCalendar(index) {
            vm.datePickerOpenStatus = true;
        }

    }
})();
