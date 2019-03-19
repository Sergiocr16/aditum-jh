(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofDialogController', PaymentProofDialogController);

    PaymentProofDialogController.$inject = ['$rootScope','globalCompany','$state', 'SaveImageCloudinary', 'Modal', '$timeout', '$scope', '$stateParams', 'entity', 'PaymentProof', 'House', 'DataUtils'];

    function PaymentProofDialogController($rootScope,globalCompany,$state, SaveImageCloudinary, Modal, $timeout, $scope, $stateParams, entity, PaymentProof, House, DataUtils) {
        var vm = this;
        var fileImage = null;
        vm.paymentProof = entity;
        vm.save = save;
        vm.houses = House.query();
        vm.isReady = true;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });


        function save() {
            vm.isSaving = true;
            if (vm.paymentProof.id !== null) {
                PaymentProof.update(vm.paymentProof, onSaveSuccess, onSaveError);
            } else {

                Modal.confirmDialog("¿Está seguro que desea enviar el comprobante de pago?", "", function () {

                    if (fileImage !== null) {
                        Modal.showLoadingBar();
                        vm.imageUser = {
                            user: vm.paymentProof.id
                        };
                        SaveImageCloudinary
                            .save(fileImage, vm.imageUser)
                            .then(onSaveImageSuccess, onSaveError, onNotify);

                    } else {
                        Modal.toast("Debe adjuntar una imagen para poder enviar el comprobante de pago.");
                        vm.isSaving = false;
                    }


                })


            }
        }

        function onSaveImageSuccess(data) {
            vm.paymentProof.imageUrl = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
            vm.paymentProof.houseId = $rootScope.companyUser.houseId;
            vm.paymentProof.status = 1;
            vm.paymentProof.companyId = globalCompany.getId();
            PaymentProof.save(vm.paymentProof, onSaveSuccess, onSaveError);
        }

        function onNotify(info) {
            vm.progress = Math.round((info.loaded / info.total) * 100);
        }

        function onSaveSuccess(result) {
            Modal.hideLoadingBar();
            Modal.toast("Se ha enviado el comprobante de pago correctamente.");
            vm.isSaving = false;
            $state.go('paymentProof.pending-user');
        }

        function onSaveError() {
            Modal.hideLoadingBar();
            Modal.toast("Ocurrió un error inesperado.");
            vm.isSaving = false;
        }

        vm.setImage = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        vm.displayImage = base64Data;
                        vm.displayImageType = $file.type;
                    });
                });
                fileImage = $file;
            }
        };

    }
})();
