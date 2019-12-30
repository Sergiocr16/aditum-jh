(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofDialogController', PaymentProofDialogController);

    PaymentProofDialogController.$inject = ['$rootScope', 'globalCompany', '$state', 'SaveImageCloudinary', 'Modal', '$timeout', '$scope', '$stateParams', 'entity', 'PaymentProof', 'House', 'DataUtils', 'AditumStorageService'];

    function PaymentProofDialogController($rootScope, globalCompany, $state, SaveImageCloudinary, Modal, $timeout, $scope, $stateParams, entity, PaymentProof, House, DataUtils, AditumStorageService) {
        var vm = this;
        vm.paymentProof = entity;
        vm.save = save;
        vm.houses = House.query();
        vm.isReady = true;
        vm.fileNameStart = vm.paymentProof.fileName;
        var file = null;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save() {
            if (file !== null) {
                Modal.confirmDialog("¿Está seguro que desea enviar el comprobante de pago?", "Una vez enviado no podrá eliminarlo", function () {
                    vm.isSaving = true;
                    Modal.showLoadingBar();
                    upload()
                })
            } else {
                Modal.toast("Debe adjuntar un archivo para poder enviar el comprobante de pago.");
                vm.isSaving = false;
            }
        }

        function upload() {
            var today = new Date();
            moment.locale("es");
            vm.direction = globalCompany.getId() + '/payment-proof/' + moment(today).format("YYYY") + '/' + moment(today).format("MMMM") + '/' + globalCompany.getHouseId() + '/';
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
                    vm.paymentProof.houseId = globalCompany.getHouseId();
                    vm.paymentProof.status = 1;
                    vm.paymentProof.companyId = globalCompany.getId();
                    vm.paymentProof.registerDate = moment(new Date());
                    PaymentProof.save(vm.paymentProof, onSaveSuccess, onSaveError);
                });
            });
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

    }
})();
