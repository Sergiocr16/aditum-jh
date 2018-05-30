(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('GeneratePaymentController', GeneratePaymentController);

    GeneratePaymentController.$inject = ['$scope', '$localStorage', '$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'House', 'Charge'];

    function GeneratePaymentController($scope, $localStorage, $state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, House, Charge) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $rootScope.active = "capturarIngresos";
        vm.payment = {};
        vm.selectedAll = true;
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        angular.element(document).ready(function() {


            $('.infoCharge').popover('show')
        });

        vm.defineContent = function(charge) {
            var content = "";
            switch (charge.state) {
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
        }, 1500)

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
                        chargeIn.payment = chargeIn.ammount - chargeIn.left;
                        if (chargeIn.payment >= chargeIn.ammount) {
                            chargeIn.payment = chargeIn.ammount;
                        }
                      defineNewStateCharge(chargeIn)
                        vm.ammount = parseInt(vm.ammount - chargeIn.ammount)
                        if (vm.ammount <= 0) {
                            vm.ammount = 0;
                        }
                    }
                    if (vm.ammount == undefined) {
                        chargeIn.left = charge.ammount;
                        chargeIn.payment = 0;
                        chargeIn.state = 1;
                    }
                })
            }

        }

function defineNewStateCharge(chargeIn){
console.log(vm.payment.ammount)
if(vm.payment.ammount==undefined){
console.log('a')
chargeIn.left = chargeIn.ammount;
chargeIn.payment = 0;
 chargeIn.state = 1;
}
    if (chargeIn.left <= 0) {
        chargeIn.left = 0;
        chargeIn.state = 3;
    } else if (chargeIn.left > 0 && chargeIn.left < chargeIn.ammount) {
        chargeIn.state = 2;
    } else if (chargeIn.left >= 0) {
        chargeIn.state = 1;
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

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function loadCharges(houseId) {

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
                    charge.payment = 0;
                    charge.state = 1;
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
    }
})();
