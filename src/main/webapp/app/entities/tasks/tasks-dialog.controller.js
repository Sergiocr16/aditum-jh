(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TasksDialogController', TasksDialogController);

    TasksDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'Modal', 'entity', 'Tasks', 'Company','$rootScope','$state','globalCompany','AditumStorageService'];

    function TasksDialogController ($timeout, $scope, $stateParams, Modal, entity, Tasks, Company,$rootScope,$state,globalCompany,AditumStorageService) {
        var vm = this;

        vm.tasks = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        $rootScope.active = "tasks";
        vm.fileNameStart = vm.tasks.fileName;
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
            vm.title = "Crear tarea";
            vm.confirmText = "¿Está seguro que desea guardar esta tarea?";
        } else {
            vm.title = "Editar tarea";
            vm.confirmText = "¿Está seguro que desea editar esta tarea?";
            vm.fileName = vm.tasks.fileName;
        }
        $rootScope.mainTitle = vm.title;


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
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/tasks/' + fileName).put(file);
            console.log(uploadTask)
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
                    vm.tasks.descriptionFile = downloadURL;
                    vm.tasks.fileName = fileName;
                    vm.tasks.deleted = 0;
                    vm.tasks.status = 1;
                    if (vm.tasks.id !== null) {
                        Tasks.update(vm.tasks, onSaveSuccess, onSaveError);
                    } else {
                        Tasks.save(vm.tasks, onSaveSuccess, onSaveError);
                    }
                });
            });
        }
        function save() {
            Modal.confirmDialog(vm.confirmText, "", function () {
                vm.isSaving = true;
                vm.tasks.companyId = globalCompany.getId();
                Modal.showLoadingBar();
                console.log(vm.tasks)
                if (vm.tasks.id==null) {
                    if(vm.fileName!=null){
                        upload();
                    }else{
                        vm.tasks.deleted = 0;
                        vm.tasks.status = 1;
                         Tasks.save(vm.tasks, onSaveSuccess, onSaveError);
                    }

                } else {
                    if(vm.fileName!=null){
                        if (vm.fileName != vm.fileNameStart ) {
                            // Create a reference to the file to delete

                            var desertRef = AditumStorageService.ref().child(globalCompany.getId() + '/tasks/' + vm.fileNameStart);
                            console.log(desertRef)
                            desertRef.delete().then(function () {
                                upload();
                            }).catch(function (error) {

                            });
                        }else{
                            Tasks.update(vm.tasks, onSaveSuccess, onSaveError);
                        }
                    }else{
                        Tasks.update(vm.tasks, onSaveSuccess, onSaveError);
                    }

                }
            })
        }

        function onSaveSuccess (result) {
            Modal.hideLoadingBar();
            vm.isSaving = false;
            $state.go("tasks");
            Modal.toast("Tarea guardada correctamente");
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.updateTimePicker = function () {
            vm.expirationDate = moment(vm.tasks.expirationDate).format('HH:mm');
        };


        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar (date) {
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
