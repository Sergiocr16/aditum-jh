(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentDetailController', PaymentDetailController);

    PaymentDetailController.$inject = ['globalCompany','$state', 'CommonMethods', '$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Officer', 'User', 'Company', 'Principal', 'Modal', 'Resident', '$localStorage', 'Payment'];

    function PaymentDetailController(globalCompany,$state, CommonMethods, $scope, $rootScope, $stateParams, DataUtils, entity, Officer, User, Company, Principal, Modal, Resident, $localStorage, Payment) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.payment = entity;
        vm.isReady = true;
        vm.email = "";
        vm.exportActions = {
            downloading: false,
            printing: false,
            sendingEmail: false,
        }
        $rootScope.mainTitle = 'Detalle de pago';
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        vm.detailProof = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-proof-detail', {
                id: encryptedId
            })
        };
        vm.print = function (paymentId) {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 8000);
            printJS({
                printable: '/api/payments/file/' + paymentId,
                type: 'pdf',
                modalMessage: "Obteniendo comprobante de pago"
            })
        };
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 8000)
        };

        vm.sendEmail2 = function (payment) {
            if(vm.email!= undefined || vm.email != "") {
                Modal.confirmDialog("¿Está seguro que desea enviarle el comprobante del pago " + payment.receiptNumber + " a " + vm.email + "?", "",
                    function () {
                        vm.exportActions.sendingEmail = true;
                        Payment.sendPaymentEmail({
                            paymentId: payment.id,
                            email: vm.email
                        });
                        setTimeout(function () {
                            $scope.$apply(function () {
                                vm.exportActions.sendingEmail = false;
                            });
                            Modal.toast("Se ha enviado el comprobante por correo al contacto principal.")
                        }, 8000)
                    });
            }else{
                Modal.toast("Debe de ingresar un email.")
            }
        };



        vm.sendEmail = function (payment) {

            Modal.confirmDialog("¿Está seguro que desea enviarle el comprobante del pago " + payment.receiptNumber + " al contacto principal de la filial " + $localStorage.houseSelected.housenumber + "?", "",
                function () {
                    vm.exportActions.sendingEmail = true;
                    Resident.getOwners({
                        companyId: globalCompany.getId(),
                        name: " ",
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


        vm.getCategory = function (type) {
            switch (type) {
                case 1:
                    return "MANTENIMIENTO";
                    break;
                case 2:
                    return "EXTRAORDINARIA";
                    break;
                case 3:
                    return "ÁREAS COMUNES";
                    break;
                case 5:
                    return "MULTA";
                    break;
                case 6:
                    return "CUOTA AGUA";
                    break;
            }
        }
    }
})();
