(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargePerHouseController', ChargePerHouseController);

    ChargePerHouseController.$inject = ['$rootScope', '$scope', '$state', 'Charge', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage'];

    function ChargePerHouseController($rootScope, $scope, $state, Charge, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.isEditing = false;
        setTimeout(function() {
            loadAll();
        }, 3000)


        vm.edit = function() {
            var result = {};

            function updateCharge(chargeNumber) {
                if (chargeNumber < vm.charges.length) {
                    var cuota = vm.charges[chargeNumber];
                    if (cuota.ammount != 0) {
                    cuota.type = parseInt(cuota.type)
                        Charge.update(cuota, function(charge) {
                            result = charge;
                            updateCharge(chargeNumber + 1)
                        })
                    }
                } else {
                       House.get({id: $localStorage.houseSelected.id
                                            }, onSuccess)
                                            function onSuccess(house) {
                                             toastr["success"]("Se han actualizado las cuotas correctamente.")
                                                                    $rootScope.houseSelected = house;
                                                $localStorage.houseSelected = house;
                                                loadAll();
                                                bootbox.hideAll();
                                                vm.isEditing = true;
                                            }

                }
            }
            var allGood = 0;
            angular.forEach(vm.charges, function(charge, i) {
                if (charge.valida == false) {
                    allGood++;
                }
            })
            if (allGood == 0) {
                bootbox.confirm({
                    message: "¿Está seguro que desea modificar las cuotas?",
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
                             CommonMethods.waitingMessage();
                            updateCharge(0)

                        }
                    }
                });
            } else {
                toastr["error"]("Alguna de las cuotas tiene un formato inválido.")
            }
        }

        vm.deleteCharge = function(charge) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar la cuota " + charge.concept + "?",
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
                    CommonMethods.waitingMessage();
                        charge.deleted = 1;
                        Charge.update(charge, onSaveSuccess, onSaveError);

                        function onSaveSuccess(result) {
                            House.get({
                                id: result.houseId
                            }, onSuccess)
                            function onSuccess(house) {
                            bootbox.hideAll();
                                toastr["success"]("La cuota se ha eliminado correctamente.")
                                $rootScope.houseSelected = house;
                                $localStorage.houseSelected = house;
                                loadAll();
                                vm.isEditing = true;
                            }

                        }

                        function onSaveError() {

                        }
                    }
                }
            });

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
                cuota.valida = true;
            } else {
                cuota.valida = false
            }
        }
        vm.editing = function() {

            setTimeout(function() {
                $scope.$apply(function() {
                    vm.isEditing = true;
                    $('.dating').keydown(function() {
                                    return false;
                                });
angular.forEach(vm.charges,function(charge,i){
         charge.date = new Date(vm.charges[i].date)
         })
                })
            }, 100)

        }
        vm.createCharge = function() {
            $state.go('houseAdministration.chargePerHouse.new')
        }
        vm.cancel = function() {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            loadAll();
            vm.isEditing = false;
        }
        $scope.$watch(function() {
            return $rootScope.houseSelected;
        }, function() {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            loadAll();
            vm.isEditing = false;
        });
        vm.getCategory = function(type) {
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

        vm.openCalendar = function(charge) {

            charge.openDate = true;

        }

        function loadAll() {

            Charge.queryByHouse({
                houseId: $localStorage.houseSelected.id,
                sort: sort()
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
                angular.forEach(data, function(charge, i) {
                    charge.openDate = false;
                    charge.type = charge.type+""
                })
                vm.charges = data;
                vm.page = pagingParams.page;
                $("#loading").fadeOut(300);
                setTimeout(function() {
                    $("#data").fadeIn("slow");
                }, 900)
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
