(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('EmergencyCreateController', EmergencyCreateController);

        EmergencyCreateController.$inject = ['globalCompany','AditumStorageService','Modal', 'AlertService', '$scope', '$rootScope', '$stateParams', '$localStorage', 'Emergency'];

        function EmergencyCreateController(globalCompany,AditumStorageService,Modal, AlertService, $scope, $rootScope, $stateParams, $localStorage, Emergency) {
            var vm = this;
            vm.isReady = true;

            var file;
            $rootScope.mainTitle = "Documentar emergencia";
            $rootScope.active = 'emergency';
            Modal.enteringDetail();
            $scope.$on("$destroy", function () {
                Modal.leavingDetail();
            });
            vm.emergencyTypes = [{tipo:'Incendio',selected:false},{tipo:'Agresiones físicas',selected:false},{tipo:'Problemas de salud',selected:false},{tipo:'Vida en riesgo',selected:false},{tipo:'Otra',selected:false}];

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
            $rootScope.active = "emergency";

            vm.edit = function () {

                Modal.confirmDialog("¿Está seguro que desea documentar esta emergencia?", "",
                    function () {
                        vm.isSaving = true;
                        Modal.showLoadingBar();
                        vm.emergency.isAttended=1;
                        if (file) {
                            upload();
                        } else {
                            Emergency.update(vm.emergency, onSaveSuccess, onSaveError);
                        }



                    })
            }

            function onSaveSuccess(result) {
                Modal.hideLoadingBar();
                Modal.toast("Se documentó la emeregncia exitosamente.")
                vm.editing = false;
                vm.isSaving = false;
            }


            function onSaveError() {
                Modal.hideLoadingBar();
                vm.isSaving = false;
            }

            function makeid(length,fileName) {
                var result           = '';
                var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                var charactersLength = characters.length;
                for ( var i = 0; i < length; i++ ) {
                    result += characters.charAt(Math.floor(Math.random() * charactersLength));
                }
                return result+"."+fileName.split('.').pop();
            }
            function upload() {
                var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/emergencies/' + vm.emergency.houseId + '/' + makeid(10,file.name)).put(file);
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
                        vm.emergency.file_url = downloadURL;
                        Emergency.update(vm.emergency, onSaveSuccess, onSaveError);
                    });
                });
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
    }

)();
