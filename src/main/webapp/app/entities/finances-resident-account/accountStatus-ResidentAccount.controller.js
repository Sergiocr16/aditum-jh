(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountStatusResidentAccountController', AccountStatusResidentAccountController);

    AccountStatusResidentAccountController.$inject = ['Modal','globalCompany', 'Balance', '$rootScope', '$scope', '$state', 'AccountStatus', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage'];

    function AccountStatusResidentAccountController(Modal,globalCompany, Balance, $rootScope, $scope, $state, AccountStatus, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage) {

        var vm = this;
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m - 6, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.isReady = false;
        vm.searchType = 1;
        vm.openCalendar = openCalendar;
        vm.loadAll = loadAll;
        $rootScope.active = "residentAccountStatus";
        $rootScope.mainTitle = "Estado de cuenta";
        vm.datePickerOpenStatus = {};
        vm.isReady = false;
        vm.expading = false;
        vm.house = $localStorage.houseSelected;
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };

        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        };
        vm.sendEmail = function () {

            Modal.confirmDialog("¿Está seguro que desea enviar el estado de cuenta al contacto principal de la filial " + $localStorage.houseSelected.housenumber + "?","",
                function(){
                    vm.exportActions.sendingEmail = true;
                    Resident.getOwners({
                        companyId: globalCompany.getId(),
                        name: " ",
                        houseId: globalCompany.getHouseId(),
                    }).$promise.then(onSuccessResident, onError);

                    function onSuccessResident(data, headers) {
                        var thereIs = false;
                        angular.forEach(data, function (resident, i) {
                            if (resident.email != undefined && resident.email != "" && resident.email != null) {
                                resident.selected = false;
                                if (resident.principalContact == 1) {
                                    thereIs = true;
                                }
                            }
                        });
                        if (thereIs == true) {
                            AccountStatus.sendPaymentEmail({
                                accountStatusObject: vm.superObject,
                                option: 2
                            });
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    vm.exportActions.sendingEmail = false;
                                });
                                Modal.toast("Se ha enviado el estado de cuenta al contacto principal.")


                            }, 8000)
                        } else {

                            vm.exportActions.sendingEmail = false;
                            Modal.toast("Esta filial no tiene un contacto principal para enviar el correo.")


                        }
                    }

                    function onError() {
                        Modal.toast("Esta filial no tiene un contacto principal para enviar el correo.")


                    }
                });



        }
        vm.print = function () {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 7000);
            printJS({
                printable: vm.path,
                type: 'pdf',
                modalMessage: "Obteniendo Estado de cuenta"
            })
        };
        loadAll();

        function loadAll() {
                loadAccountStatus();

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.datePassed = function (cuota) {
            var rightNow = new Date();
            var chargeDate = new Date(moment(cuota.date))
            return ((chargeDate.getTime() > rightNow.getTime()))
        }

        vm.showDetail = function (item) {
            item.showDetail = !item.showDetail;

        }
        vm.searchByType = function (type) {
            switch (type) {
                case 1:
                    vm.searchType = 1;
                    break;
                case 2:
                    vm.searchType = 2;
                    break;
                case 3:
                    vm.searchType = 3;
                    break;
                case 4:
                    vm.searchType = 4;
                    break;

            }

        }
        vm.consult = function () {
            $("#loading2").fadeIn(0);
            $("#accountStatusContainer").fadeOut(0);
            loadAll();
        }

        function loadAccountStatus() {
            AccountStatus.query({
                houseId: CommonMethods.encryptS(globalCompany.getHouseId()),
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                resident_account: false,
                today_time: moment(new Date()).format(),
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.superObject = globalCompany.getHouseId()+ '}' + moment(vm.dates.initial_time).format() + '}' + moment(vm.dates.final_time).format() + '}' + true + '}' + moment(new Date()).format();
                vm.path = '/api/accountStatus/file/' + vm.superObject + '/' + 1;
                vm.initial_time = vm.dates.initial_time
                vm.final_time = vm.dates.final_time
                var countPassedDate = 0;
                angular.forEach(data.listaAccountStatusItems, function (item, i) {
                    var rightNow = new Date();
                    var chargeDate = new Date(moment(item.date))
                    if (chargeDate.getTime() > rightNow.getTime()) {
                        item.datePassed = true;
                        if (countPassedDate == 0) {
                            item.definedFirstDatePassed = true;
                            countPassedDate++;
                        }
                    }
                })
                vm.accountStatusItems = data;
                vm.isReady = true;
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
            vm.findBootstrapEnvironment = function () {
                var envs = ['xs', 'sm', 'md', 'lg'];
                var $el = $('<div>');
                $el.appendTo($('body'));
                for (var i = envs.length - 1; i >= 0; i--) {
                    var env = envs[i];
                    $el.addClass('hidden-' + env);
                    if ($el.is(':hidden')) {
                        $el.remove();
                        return env;
                    }
                }
            }

            vm.isScreenSizeSmall = function () {
                var envs = ['xs', 'sm', 'md'];
                var e = 0;
                for (var i = 0; i < envs.length; i++) {
                    if (envs[i] === vm.findBootstrapEnvironment()) {
                        e++;
                    }
                }
                if (e > 0) {
                    return true;
                }
                return false;
            }
            vm.expand = function () {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.expanding = !vm.expanding;
                    });
                }, 200);

            }
            vm.updatePicker = function () {
                vm.picker1 = {
                    datepickerOptions: {
                        maxDate: vm.dates.final_time,
                        enableTime: false,
                        showWeeks: false,
                    }
                };
                vm.picker2 = {
                    datepickerOptions: {
                        minDate: vm.dates.initial_time,
                        enableTime: false,
                        showWeeks: false,
                    }
                }
            }
            vm.datePickerOpenStatus.initialtime = false;
            vm.datePickerOpenStatus.finaltime = false;

            function onError(error) {
                AlertService.error(error.data.message);
            }

            vm.expand = function () {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.expanding = !vm.expanding;
                    });
                }, 200);

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
        }

    }
})();
