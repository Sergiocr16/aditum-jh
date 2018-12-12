(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentsPerHouseController', PaymentsPerHouseController);

    PaymentsPerHouseController.$inject = ['$state', 'Payment', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$localStorage', '$scope', 'Resident','Modal','Principal'];

    function PaymentsPerHouseController($state, Payment, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $localStorage, $scope, Resident,Modal,Principal) {

        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.isReady = false;
        vm.transition = transition;
        vm.loadAll = loadAll;
        Principal.identity().then(function (account) {
            vm.account = account;
            switch (account.authorities[0]) {
                case "ROLE_MANAGER":
                    $rootScope.mainTitle = "Contabilidad filiales";
                    break;
                case "ROLE_USER":
                    $rootScope.mainTitle = "Pagos";
                    break;
            }
        })
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.initialTime = {
            date: '',
            openCalendar: false
        }
        vm.finalTime = {
            date: '',
            openCalendar: false
        }
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.openCalendar = openCalendar;
        vm.filtering = false;
        vm.print = function (paymentId) {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 8000)
            printJS({
                printable: '/api/payments/file/' + paymentId,
                type: 'pdf',
                modalMessage: "Obteniendo comprobante de pago"
            })
        }

        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 8000)
        }

        vm.sendEmail = function (payment) {

            Modal.confirmDialog("¿Está seguro que desea enviarle el comprobante del pago " + payment.receiptNumber + " al contacto principal de la filial " + $localStorage.houseSelected.housenumber + "?","",
                function(){
                    vm.exportActions.sendingEmail = true;
                    Resident.findResidentesEnabledByHouseId({
                        houseId: parseInt($localStorage.houseSelected.id),
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
                            Payment.sendPaymentEmail({
                                paymentId: payment.id
                            })
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    vm.exportActions.sendingEmail = false;
                                });
                                Modal.toast("Se ha enviado el comprobante por correo al contacto principal.")


                            }, 8000)
                        } else {

                            vm.exportActions.sendingEmail = false;
                            Modal.toast("Esta filial no tiene un contacto principal para enviarle el correo.")


                        }
                    }

                    function onError() {
                        Modal.toast("Esta filial no tiene un contacto principal para enviarle el correo.")


                    }
                });



        }

        vm.cleanSearch = function () {
            vm.isReady = false;
            vm.initialTime = {
                date: undefined,
                openCalendar: false
            }
            vm.finalTime = {
                date: undefined,
                openCalendar: false
            }
            vm.updatePicker();
            vm.filtering = false;
            loadAll();
        }

        vm.filter = function () {
            vm.isReady = false;
            vm.filtering = true;
            loadAll();
        }
        vm.updatePicker = function () {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.initialTime.date == undefined ? new Date() : vm.finalTime.date,
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.initialTime.date,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }


        vm.updatePicker();


        loadAll();
        $('.dating').keypress(function (e) {
            return false
        });


        $scope.$watch(function () {
            return $rootScope.houseSelected;
        }, function () {
            vm.isReady = false;
            vm.filtering = false;
            vm.initialTime = {
                date: undefined,
                openCalendar: false
            }
            vm.finalTime = {
                date: undefined,
                openCalendar: false
            }
            loadAll();
        });

        function openCalendar(time) {
            time.openCalendar = true;
        }

        function loadAll() {

            if (vm.initialTime.date != undefined && vm.filtering == true || vm.finalTime.date != undefined && vm.filtering == true) {
                Payment.getByHouseFilteredByDate({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    initial_time: moment(vm.initialTime.date).format(),
                    final_time: moment(vm.finalTime.date).format(),
                    houseId: $localStorage.houseSelected.id,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Payment.getByHouse({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    houseId: $localStorage.houseSelected.id,
                    sort: sort()
                }, onSuccess, onError);
            }

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
                vm.payments = data;
                angular.forEach(vm.payments, function (payment, i) {
                    payment.isShowingCharges = false;
                })
                vm.page = pagingParams.page;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        vm.showCharges = function (payment) {
            payment.isShowingCharges = !payment.isShowingCharges;
            angular.forEach(vm.payments, function (paymentIn, i) {
                if (paymentIn.id != payment.id) {
                    paymentIn.isShowingCharges = false;
                }
            })
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
