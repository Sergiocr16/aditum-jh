(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('GeneratePaymentController', GeneratePaymentController);

    GeneratePaymentController.$inject = ['$scope', '$localStorage', '$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'House', 'Charge', 'Banco', 'Payment'];

    function GeneratePaymentController($scope, $localStorage, $state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, House, Charge, Banco, Payment) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $rootScope.active = "capturarIngresos";
        vm.selectedAll = true;
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        angular.element(document).ready(function() {
            $('.infoCharge').popover('show')
        });

        vm.defineContent = function(charge) {
            var content = "";
            switch (charge.estado) {
                case 3:
                    content = "La deuda se liquidará por completo.";
                    break;
                case 2:
                    content = "La deuda se liquidará parcialmente.";
                    break;
                case 1:
                    content = "Deuda no liquidada.";
                    break;
            }
            if (charge.isIncluded == false) {
                content = "";
            }
            return content;
        }
        vm.showPopOver = function(charge) {
            var element = '#' + charge.id;
            $(element).popover({
                placement: 'left',
                trigger: "hover",
                template: '<div class="popover"><div class="arrow"></div><div class="popover-content" id="infoCharge' + element + '"></div></div>'
            });
            $(element).popover('show')
        }


        setTimeout(function() {
            loadAll();
            vm.payment = {
                paymentMethod: "DEPOSITO BANCO",
                transaction: "Abonar a cuotas",
                companyId: $rootScope.companyId
            };
$('.dating').keypress(function(e) {
    return false
});
        }, 2000)

        vm.selectAll = function() {
            angular.forEach(vm.charges, function(change, i) {
                change.isIncluded = vm.selectedAll;
            })
            vm.defineIfAllSelected()
            if (vm.selectedAll == false && vm.payment.ammount != 0) {
                vm.toPay = 0;
                vm.showPopOverNoPaymentsSelected()
            } else {
                $('.toPay').popover('destroy')
            }
            vm.calculatePayments(vm.payment)
        }

        vm.defineIfAllSelected = function() {
            var countIncluded = 0;
            vm.toPay = 0;
            angular.forEach(vm.charges, function(charge, i) {
                if (charge.isIncluded == true) {
                    vm.toPay = vm.toPay - parseInt(charge.ammount)
                    countIncluded++;
                }
            })
            if (countIncluded < vm.charges.length) {
                vm.selectedAll = false;
            } else {
                vm.selectedAll = true;
            }
            return countIncluded;
        }


        vm.showPopOverNoPaymentsSelected = function() {
            var textContent = "POR FAVOR SELECCIONA MÁS CUOTAS. ₡" + vm.payment.ammount + " NO TIENE UNA CUOTA A LA CUAL ASIGNARSE.";
            $('.toPay').popover({
                content: textContent,
                placement: 'bottom',
                template: '<div class="popover balloon" ><div class="popover-content" id="popPay"></div></div>'
            });
            var popover = $('.toPay').data('bs.popover');
            if (popover.tip().is(':visible') == false) {
                $('.toPay').popover('show')
            }
            $('#popPay').html(textContent);
        }

        function defineIfShowPopOverPayment() {
            var countIncluded = vm.defineIfAllSelected();
            if (vm.selectedAll == false && vm.payment.ammount != undefined && vm.payment.ammount > 0 && countIncluded == 0) {
                vm.toPay = 0;
                vm.showPopOverNoPaymentsSelected()
            } else {
                $('.toPay').popover('hide')
            }
        }
        vm.calculatePayments = function(charge) {
            vm.savedCharges = vm.charges;
            vm.validate(charge)
            defineIfShowPopOverPayment();

            if (charge.valida == true) {
                vm.ammount = charge.ammount;
                if (vm.ammount == undefined) {
                    vm.ammount = 0;
                }
                vm.toPay = parseInt(vm.toPay) + parseInt(vm.ammount);
                angular.forEach(vm.charges, function(chargeIn, i) {
                    if (chargeIn.isIncluded == true) {
                        chargeIn.left = chargeIn.ammount - vm.ammount;
                        chargeIn.paymentAmmount = chargeIn.ammount - chargeIn.left;
                        if (chargeIn.paymentAmmount >= chargeIn.ammount) {
                            chargeIn.paymentAmmount = chargeIn.ammount;
                        }
                        defineNewStateCharge(chargeIn)
                        vm.ammount = parseInt(vm.ammount - chargeIn.ammount)
                        if (vm.ammount <= 0) {
                            vm.ammount = 0;
                        }
                    }
                    if (vm.ammount == undefined) {
                        chargeIn.left = charge.ammount;
                        chargeIn.paymentAmmount = 0;
                        chargeIn.estado = 1;
                    }
                })
            }

        }

        function defineNewStateCharge(chargeIn) {
            if (vm.payment.ammount == undefined) {
                chargeIn.left = chargeIn.ammount;
                chargeIn.paymentAmmount = 0;
                chargeIn.estado = 1;
            }
            if (chargeIn.left <= 0) {
                chargeIn.left = 0;
                chargeIn.estado = 3;
            } else if (chargeIn.left > 0 && chargeIn.left < chargeIn.ammount) {
                chargeIn.estado = 2;
            } else if (chargeIn.left >= 0) {
                chargeIn.estado = 1;
            }
        }
        vm.validate = function(cuota) {
            var s = cuota.ammount;
            var caracteres = [':', '`', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]
            var invalido = 0;
            angular.forEach(caracteres, function(val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase() || s == undefined) {
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

        function loadAll() {
            Balance.queryBalances({
                companyId: $rootScope.companyId
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    value.debit = value.balance.debit;
                })
                vm.houses = data;
                if ($localStorage.houseSelected != null || $localStorage.houseSelected != undefined) {
                    House.get({
                        id: $localStorage.houseSelected.id
                    }, function(result) {
                        $localStorage.houseSelected = result
                        vm.house = $localStorage.houseSelected;
                        $rootScope.houseSelected = $localStorage.houseSelected;
                    })
                } else {
                    if (vm.houses.length > 0) {
                        $rootScope.houseSelected = vm.houses[0]
                        $localStorage.houseSelected = vm.houses[0]
                        vm.house = $rootScope.houseSelected;
                    }
                }
                vm.page = pagingParams.page;
                loadCharges($localStorage.houseSelected.id)
                loadBancos()
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function loadCharges(houseId) {

                                            $("#loadingTable").fadeIn(10);
                                            $("#tableContent").fadeOut(10);

            Charge.queryByHouse({
                houseId: houseId,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.toPay = 0;
                angular.forEach(data, function(charge, i) {
                    charge.isIncluded = true;
                    charge.type = charge.type + ""
                    charge.left = charge.ammount;
                    charge.paymentAmmount = 0;
                    charge.estado = 1;
                    vm.toPay = vm.toPay - parseInt(charge.ammount);
                })
                vm.charges = data.sort(function(a, b) {
                    // Turn your strings into dates, and then subtract them
                    // to get a value that is either negative, positive, or zero.
                    return new Date(a.date) - new Date(b.date);
                });

                vm.savedCharges = vm.charges;
                vm.page = pagingParams.page;
                setTimeout(function() {
                                                    $("#loadingTable").fadeOut(300);
                                                }, 400)
                                                setTimeout(function() {
                                                    $("#tableContent").fadeIn('slow');
                                                }, 700)


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
        vm.defineBalanceClass = function(balance) {
            var b = parseInt(balance);
            if (b != 0) {
                if (b > 0) {
                    return "greenBalance";
                } else {
                    return "redBalance";
                }
            }
        }
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
        vm.defineBalanceTotalClass = function(balance) {
            var b = parseInt(balance);
            if (b != 0) {
                if (b > 0) {
                    return "deuda-total-positiva";
                } else {
                    return "deuda-total-negativa";
                }
            } else {
                return "deuda-total";
            }
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
        vm.changeHouse = function(house) {
            House.get({
                id: house.id
            }, function(result) {
                clear();
                $localStorage.houseSelected = result
                $rootScope.houseSelected = result;
                vm.house = result;
                loadCharges($localStorage.houseSelected.id)
            })

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

        function loadBancos() {
            Banco.query({
                companyId: $rootScope.companyId
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.bancos = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        vm.createPayment = function() {
      if(vm.charges.length==0){
      increaseMaintBalance();
      }else{
      paymentTransaction();
      }

        }
        function paymentTransaction(){
                    bootbox.confirm({
                        message: "¿Está seguro que desea capturar este ingreso?",
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
                                vm.payment.charges = vm.charges;
                                vm.payment.account = vm.payment.account.beneficiario;
                                vm.payment.houseId = $rootScope.houseSelected.id;
                                Payment.save(vm.payment, onSuccess, onError)

                                function onSuccess(result) {
                                    bootbox.hideAll();
                                    clear()
                                    toastr["success"]("Se ha capturado el ingreso correctamente.")
                                    loadAll();
                                }
                                function onError() {
                                    bootbox.hideAll();
                                    clear()
                                    toastr["error"]("Ups. No fue posible capturar el ingreso.")

                                }
                            }
                        }
                    });
        }
        function increaseMaintBalance(){
                    bootbox.confirm({
                                              message: "NO EXISTEN DEUDAS VIGENTES, ¿Está seguro que desea generar un salvo a favor de mantenimiento?",

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
                                     $rootScope.houseSelected.balance.maintenance = $rootScope.houseSelected.balance.maintenance + vm.toPay;
                                     Balance.update($rootScope.houseSelected.balance,function(){
                                     bootbox.hideAll();
                                     loadAll();
                                     clear()
                                     toastr["success"]("Se ha abonado al fondo de mantenimiento de la vivienda correctamente.")
                                     })
                            }
                        }
                    });
        }

        function clear(){
        vm.payment = {
                        paymentMethod: "DEPOSITO BANCO",
                        transaction: "Abonar a cuotas",
                        companyId: $rootScope.companyId
                    };
        }
    }
})();

