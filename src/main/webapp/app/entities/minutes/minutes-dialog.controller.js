(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MinutesDialogController', MinutesDialogController);

    MinutesDialogController.$inject = ['$state', 'AditumStorageService', 'globalCompany', 'Modal', '$timeout', '$scope', '$stateParams', 'entity', 'CondominiumRecord', '$rootScope'];

    function MinutesDialogController($state, AditumStorageService, globalCompany, Modal, $timeout, $scope, $stateParams, entity, CondominiumRecord, $rootScope) {
        var vm = this;
        vm.condominiumRecord = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        $rootScope.active = "minutes";
        vm.fileNameStart = vm.condominiumRecord.fileName;
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
        if (entity.id === null) {
            vm.title = "Crear minuta";
            vm.confirmText = "¿Está seguro que desea guardar la minuta?";
        } else {
            vm.title = "Editar minuta";
            vm.confirmText = "¿Está seguro que desea editar la minuta?";
            vm.fileName = vm.condominiumRecord.fileName;
        }
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
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/condominium-records/' + fileName).put(file);
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
                    vm.condominiumRecord.fileUrl = downloadURL;
                    vm.condominiumRecord.fileName = fileName;
                    vm.condominiumRecord.deleted = 0
                    vm.condominiumRecord.status = 2;
                    if (vm.condominiumRecord.id !== null) {
                        CondominiumRecord.update(vm.condominiumRecord, onSaveSuccess, onSaveError);
                    } else {
                        CondominiumRecord.save(vm.condominiumRecord, onSaveSuccess, onSaveError);
                    }
                });
            });
        }

        function save() {
            Modal.confirmDialog(vm.confirmText, "", function () {
                vm.isSaving = true;
                vm.condominiumRecord.companyId = globalCompany.getId();
                Modal.showLoadingBar();
                if (!vm.condominiumRecord.id) {
                    upload();
                } else {
                    if (vm.fileName != vm.fileNameStart) {
                        // Create a reference to the file to delete
                        var desertRef = AditumStorageService.ref().child(globalCompany.getId() + '/minutes/' + vm.fileNameStart);
                        desertRef.delete().then(function () {
                            upload();
                        }).catch(function (error) {
                            // Uh-oh, an error occurred!
                        });
                    } else {
                        if (vm.condominiumRecord.id !== null) {
                            CondominiumRecord.update(vm.condominiumRecord, onSaveSuccess, onSaveError);
                        } else {
                            CondominiumRecord.save(vm.condominiumRecord, onSaveSuccess, onSaveError);
                        }
                    }
                }
            })
        }


        function onSaveSuccess(result) {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            $state.go("minutes");
            Modal.toast("Minuta guardada correctamente");
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
