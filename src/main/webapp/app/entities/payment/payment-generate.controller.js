(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('GeneratePaymentController', GeneratePaymentController);

    GeneratePaymentController.$inject = ['ExchangeRateBccr', 'AditumStorageService', 'PaymentProof', '$scope', '$localStorage', '$state', 'Balance', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'CommonMethods', 'House', 'Charge', 'Banco', 'Payment', 'AdministrationConfiguration', 'Resident', 'globalCompany', 'Modal'];

    function GeneratePaymentController(ExchangeRateBccr, AditumStorageService, PaymentProof, $scope, $localStorage, $state, Balance, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, CommonMethods, House, Charge, Banco, Payment, AdministrationConfiguration, Resident, globalCompany, Modal) {
        $rootScope.active = "generatePayment";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.isReady = false;
        $rootScope.mainTitle = "Realizar pago";
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.printReceipt = false;
        vm.selectedAll = true;
        vm.datePickerOpenStatus = false;
        vm.payment = {ammount: "0", date: new Date()}
        vm.openCalendar = openCalendar;
        vm.today = new Date();
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        vm.hasPaymentProof = false;
        vm.residents = [];
        vm.isSaving = false;
        vm.account = null;
        vm.bccrUse = true;
        vm.Today = new Date();
        vm.balanceToApply = "-1";
        vm.selectedSaldo = 0;
        vm.useSaldoFavor = true;
        vm.totalToUse = 0;
        vm.payment.ammount = 0;
        vm.useSaldo = {};
        vm.payment.cancellingFavorBalance = false;
        vm.hasSaldoAFavor = function (balance) {
            return balance.maintenance > 0 || balance.commonAreas > 0 || balance.extraordinary > 0 || balance.waterCharge > 0 || balance.others > 0 || balance.multa > 0;
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
        angular.element(document).ready(function () {
            $('.infoCharge').popover('show')
        });
        $scope.$on("$destroy", function () {
            $rootScope.paymentProofData = {};
        });
        ExchangeRateBccr.get({
            fechaInicio: moment(new Date()).format(),
            fechaFinal: moment(new Date()).format(),
        }, function (result) {
            vm.tipoCambio = result;
        })
        vm.showDate = function () {
            if (vm.payment.date != null) {
                ExchangeRateBccr.get({
                    fechaInicio: moment(vm.payment.date).format(),
                    fechaFinal: moment(vm.payment.date).format(),
                }, function (result) {
                    vm.tipoCambio = result;
                    vm.Today = vm.payment.date;
                })
            }
        }
        vm.clearSearchTerm = function () {
            vm.searchTerm = '';
        };
        vm.newProof = false;
        vm.searchTerm;
        vm.searchTermFilial;
        vm.clearSearchTermFilial = function () {
            vm.searchTermFilial = '';
        };
        vm.typingSearchTermFilial = function (ev) {
            ev.stopPropagation();
        }
        vm.typingSearchTerm = function (ev) {
            ev.stopPropagation();
        }

        function loadAllPaymentsProof(houseId) {
            PaymentProof.findByHouseIdWithoutPayment({
                houseId: houseId,
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.paymentProofs = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.newProofSet = function (bool) {
            vm.newProof = bool;
        }
        var file = null;
        vm.setFile = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                vm.file = $file;
                vm.fileName = vm.file.name;
                file = $file;
            }
        };

        function saveProof(result) {
            upload(result, onSaveSuccessProof);
        }

        function saveProofAdelanto(result) {
            upload(result, onSaveSuccessProofAdelanto);
        }

        function upload(result, onSuccess) {
            var today = new Date();
            moment.locale("es");
            vm.direction = globalCompany.getId() + '/payment-proof/' + moment(today).format("YYYY") + '/' + moment(today).format("MMMM") + '/' + $localStorage.houseSelected.id + '/';
            var uploadTask = AditumStorageService.ref().child(vm.direction + file.name).put(file);
            uploadTask.on('state_changed', function (snapshot) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    })
                }, 1)
                switch (snapshot.state) {
                    case firebase.storage.TaskState.PAUSED: // or 'paused'
                        console.log('Upload is paused');
                        break;
                    case firebase.storage.TaskState.RUNNING: // or 'running'
                        console.log('Upload is running');
                        break;
                }
            }, function (error) {
                // Handle unsuccessful uploads
            }, function () {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    vm.paymentProof.imageUrl = downloadURL;
                    vm.paymentProof.houseId = $localStorage.houseSelected.id;
                    vm.paymentProof.status = 2;
                    vm.paymentProof.companyId = globalCompany.getId();
                    vm.paymentProof.registerDate = moment(new Date());
                    vm.paymentProof.paymentId = result.id;
                    PaymentProof.save(vm.paymentProof, onSuccess, onSaveErrorProof);
                });
            });
        }

        loadAdminConfig();

        function onSaveErrorProof(error) {
            Modal.hideLoadingBar();
            Modal.toast("Hubo un error capturando el comprobante.")
        }

        function onSaveSuccessProofAdelanto() {
            if (vm.printReceipt == true) {
                printJS({
                    printable: '/api/payments/file/' + result.id,
                    type: 'pdf',
                    modalMessage: "Obteniendo comprobante de pago"
                })
                setTimeout(function () {
                    vm.printReceipt = false;
                    if (vm.admingConfig.incomeFolio == true) {
                        increaseFolioNumber(function (result) {
                            Modal.toast("Se ha capturado el ingreso correctamente.");

                            vm.admingConfig = result;
                            vm.folioSerie = result.folioSerie;
                            vm.folioNumber = result.folioNumber;
                            clear();
                            loadAll();
                            loadAdminConfig();
                        })
                    }
                }, 1000)
            } else {
                if (vm.admingConfig.incomeFolio == true) {
                    increaseFolioNumber(function (result) {
                        Modal.toast("Se ha capturado el ingreso correctamente.");
                        vm.admingConfig = result;
                        vm.folioSerie = result.folioSerie;
                        vm.folioNumber = result.folioNumber;
                        clear();
                        loadAll();
                        loadAdminConfig();
                    })
                }
            }
        }

        function onSaveSuccessProof() {
            if (vm.printReceipt == true) {
                printJS({
                    printable: '/api/payments/file/' + result.id,
                    type: 'pdf',
                    modalMessage: "Obteniendo comprobante de pago"
                })
                setTimeout(function () {
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha capturado el ingreso correctamente.")
                    vm.printReceipt = false;
                    if (vm.admingConfig.incomeFolio == true) {
                        increaseFolioNumber(function (result) {
                            vm.admingConfig = result;
                            vm.folioSerie = result.folioSerie;
                            vm.folioNumber = result.folioNumber;
                            // if (vm.toPay > 0) {
                            //     registrarAdelantoCondomino();
                            // } else {
                            clear();
                            loadAll();
                            loadAdminConfig();
                            // }
                        })
                    }
                }, 5000)
            } else {
                Modal.hideLoadingBar();
                Modal.toast("Se ha capturado el ingreso correctamente.");
                if (vm.admingConfig.incomeFolio == true) {
                    increaseFolioNumber(function (result) {
                        vm.admingConfig = result;
                        vm.folioSerie = result.folioSerie;
                        vm.folioNumber = result.folioNumber;
                        // if (vm.toPay > 0) {
                        //     registrarAdelantoCondomino();
                        // } else {
                        clear();
                        loadAll();
                        loadAdminConfig();
                        // }
                    })
                }
            }
        }

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
                    vm.toPay = vm.toPay - parseFloat(charge.leftToPay)
                    countIncluded++;
                }
            })
            if (countIncluded < vm.charges.length) {
                vm.selectedAll = false;
            } else {
                vm.selectedAll = true;
            }
            vm.toPayTotal = vm.toPay;
            return countIncluded;
        };


        vm.showPopOverNoPaymentsSelected = function () {
            // var textContent = "POR FAVOR SELECCIONA MÁS CUOTAS. ₡" + vm.payment.ammount + " NO TIENE UNA CUOTA A LA CUAL ASIGNARSE.";
            // $('.toPay').popover({
            //     content: textContent,
            //     placement: 'bottom',
            //     template: '<div class="popover balloon" ><div class="popover-content" id="popPay"></div></div>'
            // });
            // var popover = $('.toPay').data('bs.popover');
            // if (popover.tip().is(':visible') == false) {
            //     $('.toPay').popover('show')
            // }
            // $('#popPay').html(textContent);
        }

        function stillChargesNotCancelled(pay) {
            var count = 0;
            var countPayed = 0;
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.isIncluded == true) {
                    if (charge.left > 0) {
                        count++;
                    } else {
                        countPayed++;
                    }
                }
            });
            if (countPayed == vm.charges.length) {
                return false;
            }
            return count == 0 && pay > 0;
        }

        function defineIfShowPopOverPayment() {
            var countIncluded = vm.defineIfAllSelected();
            if (vm.selectedAll == false && vm.payment.ammount != undefined && vm.payment.ammount > 0 && countIncluded == 0) {
                vm.toPay = 0;
                vm.showPopOverNoPaymentsSelected();
                vm.blockPaymentInAdvanced = true;
            } else {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.blockPaymentInAdvanced = stillChargesNotCancelled(vm.toPay);
                    })
                }, 1)
            }
        }

        vm.defineIfChargesSelected = function () {
            var countIncluded = vm.defineIfAllSelected();
            if (vm.selectedAll == false && vm.payment.ammount != undefined && vm.payment.ammount > 0 && countIncluded == 0) {
                return true;
            } else {
                return false;
            }
        }
        vm.formatCurrencyToPay = function () {
            var venta = vm.bccrUse ? vm.tipoCambio.venta : vm.account.saleExchangeRate;
            vm.venta = venta;
            if (vm.admingConfig.chargesCollectCurrency != vm.account.currency) {
                if (vm.admingConfig.chargesCollectCurrency == "₡" && vm.account.currency == "$") {
                    vm.payment.ammount = vm.payment.ammountToShow * venta;
                }
                if (vm.admingConfig.chargesCollectCurrency == "$" && vm.account.currency == "₡") {
                    vm.payment.ammount = vm.payment.ammountToShow / venta;
                }
            }
        }
        vm.calculatePayments = function (payment) {
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.ammount = 0;
                    vm.savedCharges = vm.charges;
                    vm.validate(payment)
                    defineIfShowPopOverPayment();
                    if (payment.valida == true) {
                        if (Number.isNaN(payment.ammount)) {
                            vm.ammount = 0;
                            payment.ammount = 0;
                        } else {
                            vm.ammount = parseFloat(payment.ammount);
                        }
                        if (vm.hasSaldoAFavor(vm.house.balance) && vm.useSaldoFavor) {
                            if (-vm.toPay < vm.totalToUse) {
                                var diff = parseFloat(-((-vm.toPay) - vm.totalToUse));
                                vm.house.balance.newMaintenance = diff;
                                vm.totalToUseUsed = parseFloat(vm.totalToUse) - diff;
                                vm.ammount = parseFloat(vm.totalToUseUsed) + payment.ammount;
                            } else {
                                vm.totalToUseUsed = parseFloat(vm.totalToUse);
                            }
                        }
                        vm.formatCurrencyToPay()
                        if (vm.ammount == undefined) {
                            vm.ammount = 0;
                        }
                        if (vm.totalToUseUsed > 0) {
                            if (-vm.toPay >= vm.totalToUse) {
                                vm.ammount = parseFloat(vm.ammount) + parseFloat(vm.totalToUseUsed);
                                vm.toPay = parseFloat(vm.toPay) + parseFloat(vm.ammount);
                            } else {
                                vm.toPay = parseFloat(vm.toPay) + parseFloat(vm.ammount);
                            }
                        } else {
                            vm.toPay = parseFloat(vm.toPay) + parseFloat(vm.ammount);
                        }
                        vm.toPay = parseFloat(vm.toPay).toFixed(2);
                        angular.forEach(vm.charges, function (chargeIn, i) {
                            if (chargeIn.isIncluded == true) {
                                chargeIn.left = parseFloat(chargeIn.leftToPay).toFixed(2) - parseFloat(vm.ammount).toFixed(2);
                                chargeIn.paymentAmmount = parseFloat(chargeIn.leftToPay).toFixed(2) - parseFloat(chargeIn.left).toFixed(2);
                                if (chargeIn.paymentAmmount >= parseFloat(chargeIn.leftToPay).toFixed(2)) {
                                    chargeIn.paymentAmmount = parseFloat(chargeIn.leftToPay).toFixed(2);
                                }
                                defineNewStateCharge(chargeIn);
                                vm.ammount = parseFloat(vm.ammount).toFixed(2) - parseFloat(chargeIn.leftToPay).toFixed(2)
                                if (vm.ammount <= 0) {
                                    vm.ammount = 0;
                                }
                            }
                            if (vm.ammount == undefined) {
                                chargeIn.left = parseFloat(chargeIn.leftToPay).toFixed(2);
                                chargeIn.paymentAmmount = 0;
                                chargeIn.estado = 1;
                            }
                        })
                    }
                })
            }, 1)
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
            Modal.hideLoadingBar();
            House.getAllHousesClean({
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.houses = data;
                if ($localStorage.houseSelected != null || $localStorage.houseSelected != undefined) {
                    House.get({
                        id: $localStorage.houseSelected.id
                    }, function (result) {
                        $localStorage.houseSelected = result
                        vm.house = $localStorage.houseSelected;
                        vm.houseId = vm.house.id;
                        $rootScope.houseSelected = $localStorage.houseSelected;
                        vm.useSaldoFavor = true;
                        vm.keepShowingForm = true;
                        vm.totalToUseUsed = 0;
                        vm.charges = [];
                        vm.selectedSaldo = 0;
                        vm.page = pagingParams.page;
                        Balance.positiveBalanceByHouse({houseId: vm.houseId}, function (data) {
                            if (data != undefined) {
                                vm.house.balance = data;
                            }
                            loadCharges($localStorage.houseSelected.id)
                        })
                        loadResidentsForEmail($localStorage.houseSelected.id)
                        loadAllPaymentsProof($localStorage.houseSelected.id)
                        loadBancos()
                        vm.payment = {
                            paymentMethod: "Transferencia",
                            transaction: "1",
                            ammount: 0,
                            companyId: globalCompany.getId(),
                            concept: 'Abono a cuotas Filial ' + $localStorage.houseSelected.housenumber,
                        };
                        loadAdminConfig()
                    })
                } else {
                    if (vm.houses.length > 0) {
                        $rootScope.houseSelected = vm.houses[0]
                        $localStorage.houseSelected = vm.houses[0]
                        vm.house = vm.houses[0];
                        vm.houseId = vm.house.id;
                        vm.useSaldoFavor = true;
                        vm.keepShowingForm = true;
                        vm.totalToUseUsed = 0;
                        vm.charges = [];
                        vm.selectedSaldo = 0;
                        vm.page = pagingParams.page;
                        Balance.positiveBalanceByHouse({houseId: vm.houseId}, function (data) {
                            if (data != undefined) {
                                vm.house.balance = data;
                            }
                            loadCharges($localStorage.houseSelected.id)
                        })
                        loadResidentsForEmail($localStorage.houseSelected.id)
                        loadAllPaymentsProof($localStorage.houseSelected.id)
                        loadBancos()
                        vm.payment = {
                            paymentMethod: "Transferencia",
                            transaction: "1",
                            ammount: 0,
                            companyId: globalCompany.getId(),
                            concept: 'Abono a cuotas Filial ' + $localStorage.houseSelected.housenumber,
                        };
                        loadAdminConfig()
                    }
                }

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.disabledPositiveCharge = function (type) {
            var count = 0;
            for (var i = 0; i < vm.charges.length; i++) {
                if (vm.charges[i].type == type) {
                    if (parseFloat(vm.charges[i].left) > 0 || vm.charges[i].isIncluded == false) {
                        count++;
                    }
                }
            }
            return count > 0;
        }

        vm.defineResidentType = function (type) {
            switch (type) {
                case 1:
                    return "Propietario"
                    break;
                case 2:
                    return "Propietario arrendador"
                    break;
                case 3:
                    return "Residente autorizado"
                    break;
                case 4:
                    return "Inquilino"
                    break;
            }
        }

        function loadResidentsForEmail(houseId) {
            vm.residents = [];
            Resident.findAllResidentesEnabledByHouseId({
                houseId: houseId
            }, onSuccessResident, onError);

            function sort() {
                var result = [];
                if (vm.predicate !== 'name') {
                    result.push('name,asc');
                }
                return result;
            }

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


        vm.saveExchangeRate = function () {
            vm.account.exchangeRateDate = moment(new Date()).format()
            Banco.update(vm.account, function () {
                Modal.toast("Monto de tipo de cambio actualizado.")
            }, function () {
            });
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
                vm.admingConfig = result;
                if (result.incomeFolio == true) {
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

        Array.prototype.move = function (from, to) {
            this.splice(to, 0, this.splice(from, 1)[0]);
        };

        vm.moveOrderCharge = function (from, to) {
            vm.charges.move(from, to);
            vm.calculatePayments(vm.payment)
        }

        function loadCharges(houseId) {
            vm.isReady = false;
            Charge.queryByHouse({
                houseId: CommonMethods.encryptS(houseId),
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.toPay = 0;
                angular.forEach(data, function (charge, i) {
                    charge.isIncluded = true;
                    charge.type = charge.type + ""
                    charge.left = charge.leftToPay;
                    charge.paymentAmmount = 0;
                    charge.estado = 1;
                    vm.toPay = vm.toPay - parseFloat(charge.leftToPay);
                })
                vm.toPayTotal = vm.toPay;
                vm.charges = data.sort(function (a, b) {
                    // Turn your strings into dates, and then subtract them
                    // to get a value that is either negative, positive, or zero.
                    return new Date(a.date) - new Date(b.date);
                });
                if (vm.hasSaldoAFavor(vm.house.balance) && vm.charges.length != 0) {
                    vm.keepShowingForm = false;
                }
                vm.savedCharges = vm.charges;
                vm.page = pagingParams.page;
                vm.isReady = true;
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
                case "4":
                    return "MULTA";
                    break;
                case "6":
                    return "CUOTA AGUA";
                    break;
                case "7":
                    return "OTROS";
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

        vm.calculateDisabledMonto = function () {
            if (vm.totalToUse <= 0) {
                return true;
            } else {
                return false;
            }
        }
        vm.usingSaldo = function () {
            vm.totalToUse = 0;
            if (vm.ammount == undefined) {
                vm.ammount = 0;
            }
            if (vm.selectedSaldo == "1") {
                vm.totalToUse = vm.totalToUse + parseFloat(vm.house.balance.maintenance);
            }
            if (vm.selectedSaldo == "4") {
                vm.totalToUse = vm.totalToUse + parseFloat(vm.house.balance.multa);
            }
            if (vm.selectedSaldo == "2") {
                vm.totalToUse = vm.totalToUse + parseFloat(vm.house.balance.extraordinary);
            }
            if (vm.selectedSaldo == "6") {
                vm.totalToUse = vm.totalToUse + parseFloat(vm.house.balance.waterCharge);
            }
            if (vm.selectedSaldo == "7") {
                vm.totalToUse = vm.totalToUse + parseFloat(vm.house.balance.others);
            }
            if (vm.selectedSaldo == "3") {
                vm.totalToUse = vm.totalToUse + parseFloat(vm.house.balance.commonAreas);
            }
            vm.calculatePayments(vm.payment)
        }

        vm.addAbonoAdicional = function () {
            vm.keepShowingForm = true;
            vm.calculatePayments(vm.payment)
        }
        vm.removeAbonoAdicional = function () {
            vm.keepShowingForm = false;
            vm.payment = {ammount: 0};
            vm.calculatePayments(vm.payment)
        }
        vm.changingMonto = function () {
            if (vm.hasSaldoAFavor(vm.house.balance) && vm.charges.length != 0 && vm.useSaldoFavor) {
                // vm.useSaldo.maintenance = false;
                // vm.useSaldo.multas = false;
                // vm.useSaldo.others = false;
                // vm.useSaldo.waterCharges = false;
                // vm.useSaldo.extraordinary = false;
                // vm.usingSaldo()
                vm.calculatePayments(vm.payment)
            } else {
                vm.calculatePayments(vm.payment)
            }
        }
        vm.isUsingSaldo = function () {
            if (vm.hasSaldoAFavor(vm.house.balance) && vm.charges.length != 0 && vm.useSaldoFavor) {
                vm.keepShowingForm = false;
            } else {
                vm.keepShowingForm = true;
            }
            vm.calculatePayments(vm.payment)
        }
        vm.changeHouse = function (houseId) {
            House.get({
                id: houseId
            }, function (result) {
                clear();
                $localStorage.houseSelected = result
                $rootScope.houseSelected = result;
                vm.house = result;
                Balance.positiveBalanceByHouse({houseId: houseId}, function (data) {
                    if (data != undefined) {
                        vm.house.balance = data;
                    }
                    loadCharges($localStorage.houseSelected.id)
                })
                vm.useSaldoFavor = true;
                vm.keepShowingForm = true;
                vm.totalToUseUsed = 0;
                vm.charges = [];
                vm.totalToUse = 0;
                vm.selectedSaldo = 0;
                loadResidentsForEmail($localStorage.houseSelected.id)
                loadAllPaymentsProof($localStorage.houseSelected.id)
                loadAdminConfig();
                if (vm.bancos.length < 3) {
                    vm.account = vm.bancos[1];
                }
                if ($rootScope.paymentProofData != undefined) {
                    if ($rootScope.paymentProofData.bank) {
                        for (var i = 0; i < vm.bancos.length; i++) {
                            if (vm.bancos[i].beneficiario == $rootScope.paymentProofData.bank) {
                                vm.account = vm.bancos[i];
                            }
                        }
                    }
                    if ($rootScope.paymentProofData.reference) {
                        vm.payment.documentReference = $rootScope.paymentProofData.reference;
                    }
                }
                vm.useSaldo.maintenance = false;
                vm.useSaldo.multas = false;
                vm.useSaldo.others = false;
                vm.useSaldo.waterCharges = false;
                vm.useSaldo.extraordinary = false;

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
                if (vm.bancos.length < 3) {
                    vm.account = vm.bancos[1];
                }
                if ($rootScope.paymentProofData != undefined) {
                    if ($rootScope.paymentProofData.bank) {
                        for (var i = 0; i < vm.bancos.length; i++) {
                            if (vm.bancos[i].beneficiario == $rootScope.paymentProofData.bank) {
                                vm.account = vm.bancos[i];
                            }
                        }
                    }
                    if ($rootScope.paymentProofData.reference) {
                        vm.payment.documentReference = $rootScope.paymentProofData.reference;
                    }
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.countChargesSelected = function () {
            var count = 0;
            for (var i = 0; i < vm.charges.length; i++) {
                if (vm.charges[i].isIncluded) {
                    count++;
                }
            }
            return count;
        }

        vm.createPayment = function () {
            if (vm.hasPaymentProof && vm.newProof) {
                if (file == null) {
                    Modal.toast("Debe adjuntar un archivo para poder enviar el comprobante de pago.");
                    vm.isSaving = false;
                } else {
                    if ((vm.toPay > 0 && vm.charges.length == 0) || (vm.toPay > 0 && vm.countChargesSelected() == 0)) {
                        adelantoCondomino();
                    } else {
                        paymentTransaction();
                    }
                }
            } else {
                if (vm.toPay > 0 && vm.charges.length == 0 || (vm.toPay > 0 && vm.countChargesSelected() == 0)) {
                    adelantoCondomino();
                } else {
                    paymentTransaction();
                }
            }
        }

        vm.getCategoryToApplySaldoFavor = function () {
            switch (vm.balanceToApply) {
                case "1":
                    return 1;
                case "2":
                    return 2;
                case "3":
                    return 3;
                case "4":
                    return 4;
                case "5":
                    return 5;
                case "6":
                    return 6;
                case "7":
                    return 7;
            }
        }
        vm.getNameCategoryToApplySaldoFavor = function () {
            switch (vm.balanceToApply) {
                case "1":
                    return "Mantenimiento";
                case "4":
                    return "Multas";
                case "2":
                    return "Extraordinarias";
                case "3":
                    return "Áreas comunes";
                case "6":
                    return "Cuotas agua";
                case "7":
                    return "Otros";
            }
        }

        function paymentTransaction() {
            var messageS = "¿Está seguro que desea capturar este ingreso?";
            var messageS2 = "";
            if (vm.toPay > 0) {
                messageS = "SALDO A FAVOR. Además de realizar el pago se sumará un saldo de " + $rootScope.fMoney(vm.toPay) + "a favor ";
                messageS2 = "¿Está seguro que desea capturar este ingreso?";
            }
            Modal.confirmDialog(messageS, messageS2,
                function () {
                    Modal.showLoadingBar();
                    vm.payment.charges = vm.filterCharges(vm.charges);
                    if (vm.toPay > 0) {
                        var chargeAdelanto = {
                            concept: "Abono saldo a favor para " + vm.getNameCategoryToApplySaldoFavor(),
                            category: "10",
                            ammount: vm.toPay,
                            type: vm.getCategoryToApplySaldoFavor(),
                            companyId: globalCompany.getId(),
                            houseId: vm.house.id,
                            paymentAmmount: vm.toPay
                        }
                        vm.payment.charges.push(chargeAdelanto);
                        switch (vm.getCategoryToApplySaldoFavor()) {
                            case 1:
                                vm.house.balance.maintenance = parseFloat(vm.house.balance.maintenance) + parseFloat(vm.toPay);
                                break;
                            case 4:
                                vm.house.balance.multa = parseFloat(vm.house.balance.multa) + parseFloat(vm.toPay);
                                break;
                            case 2:
                                vm.house.balance.extraordinary = parseFloat(vm.house.balance.extraordinary) + parseFloat(vm.toPay);
                                break;
                            case 3:
                                vm.house.balance.commonAreas = parseFloat(vm.house.balance.commonAreas) + parseFloat(vm.toPay);
                                break;
                            case 6:
                                vm.house.balance.waterCharge = parseFloat(vm.house.balance.waterCharge) + parseFloat(vm.toPay);
                                break;
                            case 7:
                                vm.house.balance.others = parseFloat(vm.house.balance.others) + parseFloat(vm.toPay);
                                break;
                        }
                    }
                    if (vm.totalToUseUsed > 0) {
                        switch (vm.selectedSaldo) {
                            case "1":
                                vm.house.balance.maintenance = vm.house.balance.maintenance - vm.totalToUseUsed;
                                break;
                            case "4":
                                vm.house.balance.multa = vm.house.balance.multa - vm.totalToUseUsed;
                                break;
                            case "3":
                                vm.house.balance.commonAreas = vm.house.balance.commonAreas - vm.totalToUseUsed;
                                break;
                            case "2":
                                vm.house.balance.extraordinary = vm.house.balance.extraordinary - vm.totalToUseUsed;
                                break;
                            case "6":
                                vm.house.balance.waterCharge = vm.house.balance.waterCharge - vm.totalToUseUsed;
                                break;
                            case "7":
                                vm.house.balance.others = vm.house.balance.maintenance - vm.totalToUseUsed;
                                break;
                        }
                    }

                    vm.payment.account = vm.account.beneficiario + ";" + vm.account.id;
                    vm.payment.houseId = $rootScope.houseSelected.id;
                    vm.payment.doubleMoney = 0;
                    if (vm.account.currency != vm.admingConfig.chargesCollectCurrency) {
                        vm.payment.doubleMoney = 1;
                        vm.payment.ammountDollar = vm.payment.ammountToShow;
                        vm.payment.exchangeRate = vm.venta;
                    }
                    vm.isSaving = true;
                    if (vm.toPay > 0) {
                        // vm.payment.ammount = parseFloat(vm.payment.ammount) + parseFloat(vm.toPay);
                    }
                    vm.payment.concept = 'Abono a cuotas Filial ' + $localStorage.houseSelected.housenumber;
                    vm.payment.emailTo = obtainEmailToList();
                    if (Number.isNaN(vm.payment.ammount) || vm.payment.ammount == 0) {
                        vm.payment.ammount = 0;
                        vm.payment.transaction = "1";
                        vm.payment.cancellingFavorBalance = true;
                        vm.payment.account = "-;-";
                        vm.payment.paymentMethod = "Cancelado por saldos a favor";
                        vm.payment.doubleMoney = 0;
                    }

                    Payment.save(vm.payment, onSuccess, onError)

                    function onSuccess(result) {
                        if (vm.totalToUseUsed > 0 || vm.toPay > 0) {
                            Balance.update(vm.house.balance, function (data) {
                                vm.house.balance = data;
                            })
                        }
                        if (vm.hasPaymentProof && vm.newProof) {
                            saveProof(result);
                        } else {
                            vm.isSaving = false;
                            if (vm.printReceipt == true) {
                                printJS({
                                    printable: '/api/payments/file/' + result.id,
                                    type: 'pdf',
                                    modalMessage: "Obteniendo comprobante de pago"
                                })
                                setTimeout(function () {
                                    Modal.toast("Se ha capturado el ingreso correctamente.")
                                    vm.printReceipt = false;
                                    if (vm.admingConfig.incomeFolio == true) {
                                        increaseFolioNumber(function (result) {
                                            vm.admingConfig = result;
                                            vm.folioSerie = result.folioSerie;
                                            vm.folioNumber = result.folioNumber;
                                            clear();
                                            loadAll();
                                            loadAdminConfig();
                                        })
                                    } else {
                                        clear();
                                        loadAll();
                                        loadAdminConfig();
                                    }
                                }, 100)
                            } else {
                                Modal.toast("Se ha capturado el ingreso correctamente.");
                                if (vm.admingConfig.incomeFolio == true) {
                                    increaseFolioNumber(function (result) {
                                        vm.admingConfig = result;
                                        vm.folioSerie = result.folioSerie;
                                        vm.folioNumber = result.folioNumber;
                                        clear();
                                        loadAll();
                                        loadAdminConfig();
                                    })
                                } else {
                                    clear();
                                    loadAll();
                                    loadAdminConfig();
                                }
                            }
                        }
                    }

                    function onError() {
                        Modal.hideLoadingBar();
                        clear()
                        Modal.toast("Ups. No fue posible capturar el ingreso.")

                    }
                }
            );
        }

        vm.isValidToPay = function () {
            var valid = 0;
            if (vm.payment.date == undefined) {
                valid++;
            }
            if (vm.useSaldoFavor && !vm.hasSaldoAFavor(vm.house.balance)) {
                if (Number.isNaN(vm.payment.ammount)) {
                    valid++;
                } else {
                    if (vm.payment.ammount == 0) {
                        valid++
                    }
                }
                if (vm.toPay > 0) {
                    if (vm.balanceToApply == -1) {
                        valid++
                    }
                }
            } else {
                if (vm.totalToUseUsed == 0) {
                    valid++;
                }else{

                }
                if(vm.keepShowingForm==true){
                    if (Number.isNaN(vm.payment.ammount)) {
                        valid++;
                    } else {
                        if (vm.payment.ammount == 0) {
                            valid++
                        }
                    }
                    if (vm.toPay > 0) {
                        if (vm.balanceToApply == -1) {
                            valid++
                        }
                    }
                }
            }
            return valid != 0;
        }

        function increaseFolioNumber(success) {
            vm.admingConfig.folioNumber = vm.folioNumber + 1;
            vm.admingConfig.folioSerie = vm.folioSerie;
            AdministrationConfiguration.update(vm.admingConfig, success);
        }

        function adelantoCondomino() {
            Modal.confirmDialog("NO EXISTEN DEUDAS VIGENTES. La transacción será registrada como un saldo a favor de " + vm.getNameCategoryToApplySaldoFavor() + ".", "¿Está seguro que desea continuar?",
                function () {
                    registrarAdelantoCondomino()
                });
        }

        function clear() {
            vm.payment = {
                paymentMethod: "Transferencia",
                transaction: "1",
                companyId: globalCompany.getId(),
                concept: 'Abono a cuotas',
                ammount: 0
            };
            vm.account = undefined;
            vm.paymentProof = {};
            file = null;
            vm.file = null;
            vm.newProof = false;
            vm.fileName = null;
        }


        function registrarAdelantoCondomino() {
            Modal.showLoadingBar();
            vm.isSaving = true;
            vm.payment.transaction = "2",
                vm.payment.account = vm.account.beneficiario + ";" + vm.account.id;
            vm.payment.houseId = $rootScope.houseSelected.id;
            vm.payment.charges = [];
            if (vm.toPay > 0) {
                var chargeAdelanto = {
                    concept: "Abono saldo a favor para " + vm.getNameCategoryToApplySaldoFavor(),
                    category: "10",
                    ammount: vm.toPay,
                    type: vm.getCategoryToApplySaldoFavor(),
                    companyId: globalCompany.getId(),
                    houseId: vm.house.id,
                    paymentAmmount: vm.toPay
                }
                vm.payment.ammount = vm.toPay;
                vm.payment.charges.push(chargeAdelanto);
                switch (vm.getCategoryToApplySaldoFavor()) {
                    case 1:
                        vm.house.balance.maintenance = parseFloat(vm.house.balance.maintenance) + parseFloat(vm.toPay);
                        break;
                    case 4:
                        vm.house.balance.multa = parseFloat(vm.house.balance.multa) + parseFloat(vm.toPay);
                        break;
                    case 2:
                        vm.house.balance.extraordinary = parseFloat(vm.house.balance.extraordinary) + parseFloat(vm.toPay);
                        break;
                    case 3:
                        vm.house.balance.commonAreas = parseFloat(vm.house.balance.commonAreas) + parseFloat(vm.toPay);
                        break;
                    case 6:
                        vm.house.balance.waterCharge = parseFloat(vm.house.balance.waterCharge) + parseFloat(vm.toPay);
                        break;
                    case 7:
                        vm.house.balance.others = parseFloat(vm.house.balance.others) + parseFloat(vm.toPay);
                        break;
                }
                vm.increasedAmmount = vm.payment.ammount;
                vm.payment.ammount = vm.toPay;
                vm.payment.concept = "Abono saldo a favor para " + vm.getNameCategoryToApplySaldoFavor() + " Filial " + $localStorage.houseSelected.housenumber;
                vm.payment.receiptNumber = vm.admingConfig.folioSerie + "-" + vm.admingConfig.folioNumber;
                vm.payment.emailTo = obtainEmailToList();
                vm.payment.doubleMoney = 0;
                if (vm.account.currency != vm.admingConfig.chargesCollectCurrency) {
                    vm.payment.doubleMoney = 1;
                    vm.payment.ammountDollar = vm.payment.ammountToShow;
                    vm.payment.exchangeRate = vm.account.saleExchangeRate;
                }
                Payment.save(vm.payment, onSuccess, onError)
            }

            function onSuccess(result) {
                if (vm.toPay > 0) {
                    Balance.update(vm.house.balance, function (data) {
                        vm.house.balance = data;
                    })
                }
                if (vm.hasPaymentProof && vm.newProof) {
                    saveProofAdelanto(result);
                } else {
                    vm.isSaving = false;
                    if (vm.printReceipt == true) {
                        printJS({
                            printable: '/api/payments/file/' + result.id,
                            type: 'pdf',
                            modalMessage: "Obteniendo comprobante de pago"
                        })
                        setTimeout(function () {
                            clear();
                            Modal.toast("Se ha capturado el saldo a favor correctamente.")
                            vm.printReceipt = false;
                            increaseFolioNumber(function () {
                            });
                            increaseMaintBalance();
                            loadAll();
                            loadAdminConfig();
                        }, 5000)
                    } else {
                        clear();
                        Modal.toast("Se ha capturado el saldo a favor correctamente.")
                        increaseFolioNumber(function () {
                        });
                        increaseMaintBalance();
                        loadAll();
                        loadAdminConfig();
                    }
                }
            }

            function onError() {
                Modal.hideLoadingBar();
                clear();
                Modal.toast("Ups. No fue posible capturar el adelanto del condómino.")
            }
        }

        function increaseMaintBalance() {
            House.get({
                id: $localStorage.houseSelected.id
            }, function (result) {
                $localStorage.houseSelected = result
                $rootScope.houseSelected = result;
                vm.house = result;
                $rootScope.houseSelected.balance.maintenance = parseFloat($rootScope.houseSelected.balance.maintenance) + parseFloat(vm.toPay);
                // Balance.update($localStorage.houseSelected.balance, function () {
                Modal.hideLoadingBar();
                loadAll();
                // })
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
                    charge.waterConsumption = undefined;
                    selectedCharges.push(charge)
                }
            })
            return selectedCharges;
        }

        vm.back = function () {
            window.history.back();
        }
    }
})
();
