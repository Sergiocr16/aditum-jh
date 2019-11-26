(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ContractDialogController', ContractDialogController);

    ContractDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope', 'entity', 'Contract', 'Company', 'Modal', 'globalCompany', 'AditumStorageService'];

    function ContractDialogController($timeout, $scope, $stateParams, $rootScope, entity, Contract, Company, Modal, globalCompany, AditumStorageService) {
        var vm = this;

        vm.contract = entity;
        $rootScope.active = "contract";

        var file;
        vm.options = {
            height: 150,
            toolbar: [
                ['style', ['style']],
                ['font', ['bold', 'underline', 'clear']],
                ['fontname', ['fontname']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['link']],
                ['view', ['fullscreen', 'help']],
            ]
        };
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        if (entity.id === null) {
            vm.title = "Crear contrato";
        } else {
            vm.title = "Editar contrato";
            vm.fileName = vm.contract.fileName;
        }
        vm.isReady = true;
        $rootScope.mainTitle = vm.title;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            vm.contract.companyId = globalCompany.getId();
            Modal.showLoadingBar();

            var uploadTask = AditumStorageService.ref().child('contracts/'+file.name).put(file);
            uploadTask.on('state_changed', function(snapshot){
                setTimeout(function(){
                    $scope.$apply(function(){
                        vm.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    })
                },1)
                switch (snapshot.state) {
                    case firebase.storage.TaskState.PAUSED: // or 'paused'
                        console.log('Upload is paused');
                        break;
                    case firebase.storage.TaskState.RUNNING: // or 'running'
                        console.log('Upload is running');
                        break;
                }
            }, function(error) {
                // Handle unsuccessful uploads
            }, function() {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
                    console.log('File available at', downloadURL);
                    vm.contract.fileUrl = downloadURL;
                    if (vm.contract.id !== null) {
                        Contract.update(vm.contract, onSaveSuccess, onSaveError);
                    } else {
                        Contract.save(vm.contract, onSaveSuccess, onSaveError);
                    }
                });
            });

        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:contractUpdate', result);
            Modal.showLoadingBar();
            Modal.message("Contrato guardado correctamente");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dueDate = false;

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
