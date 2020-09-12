(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualBillingFileDialogController', MensualBillingFileDialogController);

    MensualBillingFileDialogController.$inject = ['$state', 'AditumStorageService', 'globalCompany', 'Modal', '$timeout', '$scope', '$stateParams', 'entity', 'MensualBillingFile', '$rootScope'];

    function MensualBillingFileDialogController($state, AditumStorageService, globalCompany, Modal, $timeout, $scope, $stateParams, entity, MensualBillingFile, $rootScope) {
        var vm = this;
        vm.mensualBillingFile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $rootScope.active = "collectionTable";
        vm.months = [
            {name: "Enero", number: 1},
            {name: "Febrero", number: 2},
            {name: "Marzo", number: 3},
            {name: "Abril", number: 4},
            {name: "Mayo", number: 5},
            {name: "Junio", number: 6},
            {name: "Julio", number: 7},
            {name: "Agosto", number: 8},
            {name: "Septiembre", number: 9},
            {name: "Octubre", number: 10},
            {name: "Noviembre", number: 11},
            {name: "Diciembre", number: 12},
        ]
        vm.years = [
            "2019","2020","2021","2022","2023"
        ]
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.fileNameStart = vm.mensualBillingFile.name;
        var file;
        vm.options = {
            toolbar: [
                // [groupName, [list of button]]
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['font', ['strikethrough']],
                // ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                // ['height', ['height']]
            ]
        }
        vm.isReady = true;

        vm.title = "Subir estado financiero"
        vm.confirmText = "¿Está seguro que desea subir el estado financiero?";

        $rootScope.mainTitle = vm.title;
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function makeid(length, fileName) {
            var result = '';
            var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
            var charactersLength = characters.length;
            for (var i = 0; i < length; i++) {
                result += characters.charAt(Math.floor(Math.random() * charactersLength));
            }
            return result + "." + fileName.split('.').pop();
        }

        function upload() {
            var fileName = makeid(15, file.name);
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/mensual-billings/' + fileName).put(file);
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
                    vm.mensualBillingFile.url = downloadURL;
                    vm.mensualBillingFile.deleted = 0
                    if (vm.mensualBillingFile.id !== null) {
                        MensualBillingFile.update(vm.mensualBillingFile, onSaveSuccess, onSaveError);
                    } else {
                        MensualBillingFile.save(vm.mensualBillingFile, onSaveSuccess, onSaveError);
                    }
                });
            });
        }

        function save() {
            Modal.confirmDialog(vm.confirmText, "", function () {
                vm.isSaving = true;
                vm.mensualBillingFile.companyId = globalCompany.getId();
                Modal.showLoadingBar();
                if (!vm.mensualBillingFile.id) {
                    upload();
                } else {
                    if (vm.fileName != vm.fileNameStart) {
                        // Create a reference to the file to delete
                        var desertRef = AditumStorageService.ref().child(globalCompany.getId() + '/mensual-billings/' + vm.fileNameStart);
                        desertRef.delete().then(function () {
                            upload();
                        }).catch(function (error) {
                            // Uh-oh, an error occurred!
                        });
                    } else {
                        if (vm.mensualBillingFile.id !== null) {
                            MensualBillingFile.update(vm.mensualBillingFile, onSaveSuccess, onSaveError);
                        } else {
                            MensualBillingFile.save(vm.mensualBillingFile, onSaveSuccess, onSaveError);
                        }
                    }
                }
            })
        }


        function onSaveSuccess(result) {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            $state.go("mensual-billing-file");
            Modal.toast("Estado financiero subido correctamente");
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.uploadDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
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
