(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDialogAdminController', RegulationDialogAdminController);

    RegulationDialogAdminController.$inject = ['Modal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Regulation', 'Company','globalCompany','AditumStorageService'];

    function RegulationDialogAdminController(Modal,$timeout, $scope, $stateParams, $uibModalInstance, entity, Regulation, Company,globalCompany,AditumStorageService) {
        var vm = this;

        vm.regulation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.required = 1;
        var file;
        vm.companies = Company.query();
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        if (entity.id === null) {
            vm.title = "Crear reglamento";
            vm.confirmText = "¿Está seguro que desea guardar el reglamento?";
        } else {
            vm.title = "Editar reglamento";
            vm.confirmText = "¿Está seguro que desea editar el reglamento?";
            vm.fileName = vm.regulation.fileName;
        }

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
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/regulation/' + fileName).put(file);
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
            }, function () {

                uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    vm.regulation.notes = downloadURL;
                    vm.regulation.deleted = 0
                    vm.regulation.type = 5;
                    if (vm.regulation.id !== null) {
                        Regulation.update(vm.regulation, onSaveSuccess, onSaveError);
                    } else {
                        Regulation.save(vm.regulation, onSaveSuccess, onSaveError);
                    }
                });
            });
        }

        function save() {
            Modal.confirmDialog(vm.confirmText, "", function () {
                vm.isSaving = true;
                vm.regulation.companyId = globalCompany.getId();
                Modal.showLoadingBar();
                if (!vm.regulation.id) {
                    upload();
                } else {
                    if (vm.fileName != vm.fileNameStart) {
                        // Create a reference to the file to delete
                        var desertRef = AditumStorageService.ref().child(globalCompany.getId() + '/regulation/' + vm.fileNameStart);
                        desertRef.delete().then(function () {
                            upload();
                        }).catch(function (error) {
                            // Uh-oh, an error occurred!
                        });
                    } else {
                        if (vm.regulation.id !== null) {
                            Regulation.update(vm.regulation, onSaveSuccess, onSaveError);
                        } else {
                            Regulation.save(vm.regulation, onSaveSuccess, onSaveError);
                        }
                    }
                }
            })
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:regulationUpdate', result);
            Modal.hideLoadingBar();
            Modal.toast("Se ha gestionado el reglamento correctamente.");
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError() {
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
