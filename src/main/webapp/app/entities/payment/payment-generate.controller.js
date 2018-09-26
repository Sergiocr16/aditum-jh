(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('GeneratePaymentController', GeneratePaymentController);

    GeneratePaymentController.$inject = ['$scope', '$localStorage', '$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'House', 'Charge', 'Banco', 'Payment', 'AdministrationConfiguration', 'Resident', 'globalCompany'];

    function GeneratePaymentController($scope, $localStorage, $state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, House, Charge, Banco, Payment, AdministrationConfiguration, Resident, globalCompany) {
        $rootScope.active = "generatePayment";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.printReceipt = false;
        vm.selectedAll = true;
        vm.datePickerOpenStatus = false;
        vm.openCalendar = openCalendar;
        vm.residents = [];
        angular.element(document).ready(function () {
            $('.infoCharge').popover('show')
        });

        vm.defineContent = function (charge) {
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
        vm.showPopOver = function (charge) {
            var element = '#' + charge.id;
            $(element).popover({
                placement: 'left',
                trigger: "hover",
                template: '<div class="popover"><div class="arrow"></div><div class="popover-content" id="infoCharge' + element + '"></div></div>'
            });
            $(element).popover('show')
        }


        loadAll();
        $('.dating').keypress(function (e) {
            return false
        });

        vm.selectAll = function () {
            angular.forEach(vm.charges, function (change, i) {
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

        vm.defineIfAllSelected = function () {
            var countIncluded = 0;
            vm.toPay = 0;
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.isIncluded == true) {
                    vm.toPay = vm.toPay - parseInt(charge.total)
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


        vm.showPopOverNoPaymentsSelected = function () {
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
                $('.toPay').popover('destroy')
            }
        }

        vm.calculatePayments = function (payment) {
            vm.savedCharges = vm.charges;
            vm.validate(payment)
            defineIfShowPopOverPayment();

            if (payment.valida == true) {
                vm.ammount = payment.ammount;
                if (vm.ammount == undefined) {
                    vm.ammount = 0;
                }
                vm.toPay = parseInt(vm.toPay) + parseInt(vm.ammount);
                angular.forEach(vm.charges, function (chargeIn, i) {
                    if (chargeIn.isIncluded == true) {
                        chargeIn.left = chargeIn.total - vm.ammount;
                        chargeIn.paymentAmmount = chargeIn.total - chargeIn.left;
                        if (chargeIn.paymentAmmount >= chargeIn.total) {
                            chargeIn.paymentAmmount = chargeIn.total;
                        }
                        defineNewStateCharge(chargeIn);
                        vm.ammount = parseInt(vm.ammount - chargeIn.total)
                        if (vm.ammount <= 0) {
                            vm.ammount = 0;
                        }
                    }
                    if (vm.ammount == undefined) {
                        chargeIn.left = chargeIn.total;
                        chargeIn.paymentAmmount = 0;
                        chargeIn.estado = 1;
                    }
                })
            }

        }

        function defineNewStateCharge(chargeIn) {
            if (vm.payment.ammount == undefined) {
                chargeIn.left = chargeIn.total;
                chargeIn.paymentAmmount = 0;
                chargeIn.estado = 1;
            }
            if (chargeIn.left <= 0) {
                chargeIn.left = 0;
                chargeIn.estado = 3;
            } else if (chargeIn.left > 0 && chargeIn.left < chargeIn.total) {
                chargeIn.estado = 2;
            } else if (chargeIn.left >= 0) {
                chargeIn.estado = 1;
            }
        }

        vm.validate = function (cuota) {
            var s = cuota.ammount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
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
                companyId: globalCompany.getId()
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    value.debit = value.balance.debit;
                })
                vm.houses = data;
                if ($localStorage.houseSelected != null || $localStorage.houseSelected != undefined) {
                    House.get({
                        id: $localStorage.houseSelected.id
                    }, function (result) {
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
                loadResidentsForEmail($localStorage.houseSelected.id)
                loadBancos()

                vm.payment = {
                    paymentMethod: "DEPOSITO BANCO",
                    transaction: "1",
                    companyId: globalCompany.getId(),
                    concept: 'Abono a cuotas Filial ' + $localStorage.houseSelected.housenumber,
                };
                loadAdminConfig()
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadResidentsForEmail(houseId) {
            vm.residents = [];
            Resident.findResidentesEnabledByHouseId({
                houseId: houseId
            }).$promise.then(onSuccessResident, onError);

            function onSuccessResident(data, headers) {
                angular.forEach(data, function (resident, i) {
                    if (resident.email != undefined && resident.email != "" && resident.email != null) {
                        resident.selected = false;
                        if (resident.principalContact == 1) {
                            resident.selected = true;
                        }

                        vm.residents.push(resident);
                    }
                });
            }

            function onError() {

            }
        }

        vm.selectPrincipalContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.principalContact == 1) {
                    resident.selected = true;
                }
            });
        }
        vm.selectAllContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                resident.selected = true;
            });
        }

        vm.selectNoneContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                resident.selected = false;
            });
        }

        function loadAdminConfig() {
            AdministrationConfiguration.get({
                companyId: globalCompany.getId()
            }).$promise.then(function (result) {
                if (result.folioSerie != null) {
                    vm.admingConfig = result;
                    vm.folioSerie = result.folioSerie;
                    vm.folioNumber = result.folioNumber;
                    vm.payment.receiptNumber = result.folioSerie + "-" + result.folioNumber;
                }
            })
        }

        function obtainEmailToList() {
            var residentsToSendEmails = [];
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.selected == true) {
                    residentsToSendEmails.indexOf(resident) === -1 ? residentsToSendEmails.push(resident) : false;
                }
            })
            return residentsToSendEmails;
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
                angular.forEach(data, function (charge, i) {
                    charge.isIncluded = true;
                    charge.type = charge.type + ""
                    charge.left = charge.total;
                    charge.paymentAmmount = 0;
                    charge.estado = 1;
                    vm.toPay = vm.toPay - parseInt(charge.total);
                })
                vm.charges = data.sort(function (a, b) {
                    // Turn your strings into dates, and then subtract them
                    // to get a value that is either negative, positive, or zero.
                    return new Date(a.date) - new Date(b.date);
                });

                vm.savedCharges = vm.charges;
                vm.page = pagingParams.page;
                setTimeout(function () {
                    $("#loadingTable").fadeOut(300);
                }, 400)
                setTimeout(function () {
                    $("#tableContent").fadeIn('slow');
                }, 700)


                setTimeout(function () {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function () {
                    $("#tableData").fadeIn('slow');
                }, 700)
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        vm.defineBalanceClass = function (balance) {
            var b = parseInt(balance);
            if (b != 0) {
                if (b > 0) {
                    return "greenBalance";
                } else {
                    return "redBalance";
                }
            }
        }
        vm.getCategory = function (type) {
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
        vm.defineBalanceTotalClass = function (balance) {
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
        vm.changeHouse = function (house) {
            House.get({
                id: house.id
            }, function (result) {
                clear();
                $localStorage.houseSelected = result
                $rootScope.houseSelected = result;
                vm.house = result;
                loadCharges($localStorage.houseSelected.id)
                loadResidentsForEmail($localStorage.houseSelected.id)
                loadAdminConfig();
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
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.bancos = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        vm.createPayment = function () {
            if (vm.charges.length == 0) {
                adelantoCondomino();
            } else {
                paymentTransaction();
            }

        }

        function paymentTransaction() {
            var messageS = "¿Está seguro que desea capturar este ingreso?";
            if (vm.toPay > 0) {
                messageS = "SALDO A FAVOR. Además de realizar el pago se creará un adelanto del condómino con el saldo a favor, ¿Está seguro que desea capturar este ingreso?";
            }
            bootbox.confirm({
                message: messageS,
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
                callback: function (result) {
                    if (result) {
                        CommonMethods.waitingMessage();
                        vm.payment.charges = vm.filterCharges(vm.charges);
                        vm.payment.account = vm.account.beneficiario + ";" + vm.account.id;
                        vm.payment.houseId = $rootScope.houseSelected.id;
                        if (vm.toPay > 0) {
                            vm.payment.ammount = parseInt(vm.payment.ammount) - parseInt(vm.toPay);
                        }
                        vm.payment.concept = 'Abono a cuotas Filial ' + $localStorage.houseSelected.housenumber;
                        vm.payment.emailTo = obtainEmailToList();
                        Payment.save(vm.payment, onSuccess, onError)

                        function onSuccess(result) {
                            if (vm.printReceipt == true) {
                                printJS({
                                    printable: '/api/payments/file/' + result.id,
                                    type: 'pdf',
                                    modalMessage: "Obteniendo comprobante de pago"
                                })

                                setTimeout(function () {
                                    bootbox.hideAll();
                                    toastr["success"]("Se ha capturado el ingreso correctamente.")
                                    vm.printReceipt = false;
                                    increaseFolioNumber(function (result) {
                                        vm.admingConfig = result;
                                        vm.folioSerie = result.folioSerie;
                                        vm.folioNumber = result.folioNumber;
                                        if (vm.toPay > 0) {
                                            registrarAdelantoCondomino();
                                        } else {
                                            clear();
                                            loadAll();
                                            loadAdminConfig();
                                        }
                                    })
                                }, 5000)


                            } else {
                                bootbox.hideAll();
                                toastr["success"]("Se ha capturado el ingreso correctamente.")
                                increaseFolioNumber(function (result) {
                                    vm.admingConfig = result;
                                    vm.folioSerie = result.folioSerie;
                                    vm.folioNumber = result.folioNumber;
                                    if (vm.toPay > 0) {
                                        registrarAdelantoCondomino();
                                    } else {
                                        clear();
                                        loadAll();
                                        loadAdminConfig();
                                    }
                                })
                            }


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


        function increaseFolioNumber(success) {
            vm.admingConfig.folioNumber = vm.folioNumber + 1;
            vm.admingConfig.folioSerie = vm.folioSerie;
            AdministrationConfiguration.update(vm.admingConfig, success);
        }

        function adelantoCondomino() {
            bootbox.confirm({
                message: "NO EXISTEN DEUDAS VIGENTES. La transacción será registrada como un adelanto del condomino. ¿Está seguro que desea continuar?",
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
                callback: function (result) {
                    if (result) {
                        registrarAdelantoCondomino()
                    }
                }
            });
        }

        function clear() {
            vm.payment = {
                paymentMethod: "DEPOSITO BANCO",
                transaction: "1",
                companyId: globalCompany.getId(),
                concept: 'Abono a cuotas'
            };
        }

        function registrarAdelantoCondomino() {
            CommonMethods.waitingMessage();
            vm.payment.transaction = "2",
                vm.payment.account = vm.account.beneficiario + ";" + vm.account.id;
            vm.payment.houseId = $rootScope.houseSelected.id;
            vm.payment.charges = [];
            vm.increasedAmmount = vm.payment.ammount;
            vm.payment.ammount = vm.toPay;
            vm.payment.concept = "Adelanto de condómino Filial " + $localStorage.houseSelected.housenumber;
            vm.payment.receiptNumber = vm.admingConfig.folioSerie + "-" + vm.admingConfig.folioNumber;
            vm.payment.emailTo = obtainEmailToList();
            Payment.save(vm.payment, onSuccess, onError)

            function onSuccess(result) {
                if (vm.printReceipt == true) {
                    printJS({
                        printable: '/api/payments/file/' + result.id,
                        type: 'pdf',
                        modalMessage: "Obteniendo comprobante de pago"
                    })
                    setTimeout(function () {
                        bootbox.hideAll();
                        clear();
                        toastr["success"]("Se ha capturado el adelanto del condómino correctamente.")
                        vm.printReceipt = false;
                        increaseFolioNumber(function () {
                        });
                        increaseMaintBalance();
                        loadAll();
                        loadAdminConfig();
                    }, 5000)


                } else {
                    bootbox.hideAll();
                    clear();
                    toastr["success"]("Se ha capturado el adelanto del condómino correctamente.")
                    increaseFolioNumber(function () {
                    });
                    increaseMaintBalance();
                    loadAll();
                    loadAdminConfig();
                }


            }

            function onError() {
                bootbox.hideAll();
                clear()
                toastr["error"]("Ups. No fue posible capturar el adelanto del condómino.")

            }
        }

        function increaseMaintBalance() {
            House.get({
                id: $localStorage.houseSelected.id
            }, function (result) {
                $localStorage.houseSelected = result
                $rootScope.houseSelected = result;
                vm.house = result;
                $rootScope.houseSelected.balance.maintenance = parseInt($rootScope.houseSelected.balance.maintenance) + parseInt(vm.toPay);
                Balance.update($rootScope.houseSelected.balance, function () {
                    bootbox.hideAll();
                    loadAll();
                })


            })
        }

        vm.isAnyChargeSelected = function () {
            var count = 0;
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.isIncluded == true) {
                    count++
                }
            })
            return (count > 0)
        }

        vm.filterCharges = function () {
            var selectedCharges = []
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.isIncluded == true) {
                    selectedCharges.push(charge)
                }
            })
            return selectedCharges;
        }

        vm.back = function () {
            window.history.back();
        }
    }
})();
