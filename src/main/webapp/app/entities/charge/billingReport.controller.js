(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BillingReportController', BillingReportController);

    BillingReportController.$inject = ['Charge','Company','Resident', 'Banco', 'House', '$timeout', '$scope', '$state', 'Payment', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'CommonMethods', 'Proveedor', '$rootScope', 'globalCompany', 'Modal'];

    function BillingReportController(Charge, Company,Resident, Banco, House, $timeout, $scope, $state, Payment, ParseLinks, AlertService, paginationConstants, pagingParams, CommonMethods, Proveedor, $rootScope, globalCompany, Modal) {
        $rootScope.active = "reporteFacturación";
        var vm = this;
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false
        };
        $rootScope.mainTitle = "Reporte de facturación";
        vm.isReady = false;
        vm.isReady2 = false;
        vm.datePickerOpenStatus = {};
        vm.propertyName = 'id';
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        vm.detailPayment = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-detail', {
                id: encryptedId
            })
        }
        vm.reverse = true;
        vm.consulting = false;
        vm.banco = "empty";
        vm.paymentMethod = "empty";
        vm.houseId = "empty";
        vm.category = "empty";
        var date = new Date(),
            y = date.getFullYear(),
            m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        vm.consultAgain = function () {
            vm.banco = "empty";
            vm.paymentMethod = "empty";
            vm.houseId = "empty";
            vm.category = "empty";
            vm.consulting = false;
            vm.isReady2 = false;
        }


        vm.loadHouses = function () {
            House.query({
                companyId: globalCompany.getId()
            }, function (data, headers) {
                vm.houses = data;
                Company.get({id:  globalCompany.getId()}).$promise.then(function (result) {
                    vm.isReady = true;
                    vm.companyName = result.name;
                });
            })

        };


        vm.printPayment = function (paymentId) {
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


        vm.print = function () {
            vm.exportActions.printing = true;
            $timeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 6000)
            printJS({
                printable: vm.urlToDownload(),
                type: 'pdf',
                modalMessage: "Obteniendo comprobante de pago"
            })
        }
        vm.urlToDownload = function () {
            return '/api/payments/incomeReport/file/' + moment(vm.dates.initial_time).format() + "/" + moment(vm.dates.final_time).format() + "/" + globalCompany.getId() + "/" + vm.banco + "/" + vm.paymentMethod + "/" + vm.houseId + "/" + vm.category;

        }
        vm.download = function () {
            vm.exportActions.downloading = true;
            $timeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 6000)
        }
        vm.loadHouses();

        vm.generateReport = function () {
            vm.isReady2 = false;
            vm.consulting = true;
            vm.showLoading = true;
            if (vm.houseId == "" || vm.houseId == null) {
                vm.houseId = "empty"
            }
            Charge.findBillingReport({
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                companyId: globalCompany.getId(),
                houseId: vm.houseId,
                category: vm.category
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                console.log(data);
                vm.billingReport = data;
                vm.isReady2 = true;
                vm.showLoading = false;
            }

            function onError(error) {
                vm.isReady2 = true;
                Modal.toast("Ha ocurrido un error al generar el reporte de ingresos.")
                AlertService.error(error.data.message);
            }
        };

        vm.showCharges = function (payment) {
            payment.isShowingCharges = !payment.isShowingCharges;
            angular.forEach(vm.payments, function (paymentIn, i) {
                if (paymentIn.id != payment.id) {
                    paymentIn.isShowingCharges = false;
                }
            })
        };


    }
})();
