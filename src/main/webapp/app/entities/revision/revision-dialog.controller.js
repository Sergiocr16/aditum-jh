(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDialogController', RevisionDialogController);

    RevisionDialogController.$inject = ['AditumStorageService', 'RevisionTaskCategory', 'Modal', '$rootScope', 'ParseLinks', 'globalCompany', 'RevisionConfig', '$timeout', '$scope', '$stateParams', 'entity', 'Revision', 'RevisionTask', 'Company', 'CommonMethods', '$state'];

    function RevisionDialogController(AditumStorageService, RevisionTaskCategory, Modal, $rootScope, ParseLinks, globalCompany, RevisionConfig, $timeout, $scope, $stateParams, entity, Revision, RevisionTask, Company, CommonMethods, $state) {
        var vm = this;
        vm.revision = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.saveForm = saveForm;
        vm.saveTask = saveTask;
        vm.saveTaskForm = saveTaskForm;

        var file;

        vm.fileName = vm.revision.fileUrl ? "already" : null;
        vm.revisionConfigSelected = null;
        vm.revisiontasks = RevisionTask.query();
        vm.companies = Company.query();
        vm.revisionTaskCategories = [];
        vm.ready = false;
        $rootScope.mainTitle = "Revisión rutinaria";
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });


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

        vm.deleteFile = function () {
            Modal.confirmDialog("¿Está seguro que desea eliminar el archivo?", "Una vez eliminado no lo podrá recuperar", function () {
                vm.fileName = null;
                file = null;
                vm.file = null;
                if (vm.revision.fileUrl) {
                    AditumStorageService.deleteFromUrl(globalCompany.getId() + '/routine-check/' + vm.revision.id + '/' + vm.revision.fileName);
                    vm.revision.fileUrl = null;
                    vm.revision.fileName = null;
                    Modal.toast("Se elimino el archivo correctamente");
                    save();
                }
            })
        };

        vm.deleteFileTask = function (task) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el archivo?", "Una vez eliminado no lo podrá recuperar", function () {
                if (task.observationFile) {
                    AditumStorageService.deleteFromUrl(globalCompany.getId() + '/routine-check/' + vm.revision.id + '/routine-tasks/' + task.fileName);
                    task.observationFile = null;
                    task.fileName = null;
                    Modal.toast("Se elimino el archivo correctamente");
                    saveTask(task);
                }
            })
        };

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
            if (vm.revision.fileUrl) {
                AditumStorageService.deleteFromUrl(globalCompany.getId() + '/routine-check/' + vm.revision.id + "/" + vm.revision.fileName);
                vm.revision.fileUrl = null;
                vm.revision.fileName = null;
            }
            var fileName = makeid(15, file.name);
            vm.revision.fileName = fileName;
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/routine-check/' + vm.revision.id + "/" + fileName).put(file);
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
                    vm.revision.fileUrl = downloadURL;
                    Modal.toast("Se adjunto el archivo correctamente")
                    if (vm.revision.id !== null) {
                        Revision.update(vm.revision, onSaveSuccessRevisionForm, onSaveError);
                    } else {
                        Revision.save(vm.revision, onSaveSuccessRevisionForm, onSaveError);
                    }
                });
            });
        }

        vm.detailRevision = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('revision-detail', {
                id: encryptedId
            })
        }

        function uploadTask(task) {
            if (task.observationFile) {
                AditumStorageService.deleteFromUrl(globalCompany.getId() + '/routine-check/' + vm.revision.id + '/routine-tasks/' + task.fileName);
                task.observationFile = null;
                task.fileName = null;
            }
            var fileName = makeid(15, task.file.name);
            task.fileName = fileName;
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/routine-check/' + vm.revision.id + '/routine-tasks/' + task.fileName).put(task.file);
            uploadTask.on('state_changed', function (snapshot) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        task.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
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
                    task.observationFile = downloadURL;
                    Modal.toast("Se adjunto el archivo correctamente");
                    task.progress = 0;
                    task.hasObservations = task.showObservations ? 1 : 0;
                    if (task.id !== null) {
                        RevisionTask.update(task, function (result) {
                            vm.isSaving = false;
                            task.observationFile = result.observationFile;
                            task.fileName = result.fileName;
                            task.fileAlready = task.observationFile ? true : false;
                            Modal.hideLoadingBar();
                            vm.isSavingFileTask = false;
                        }, onSaveError);
                    } else {
                        RevisionTask.save(task, function (result) {
                            vm.isSaving = false;
                            task.observationFile = result.observationFile;
                            task.fileName = result.fileName;
                            task.fileAlready = task.observationFile ? true : false;
                            Modal.hideLoadingBar();
                            vm.isSavingFileTask = false;
                        }, onSaveError);
                    }
                });
            });
        }

        function formatCategories(array) {
            for (var i = 0; i < vm.revisionTaskCategories.length; i++) {
                vm.revisionTaskCategories[i].tasks = [];
                for (var j = 0; j < array.length; j++) {
                    var task = array[j];
                    if (task.revisionTaskCategoryId == vm.revisionTaskCategories[i].id) {
                        task.showObservations = task.hasObservations == 1 ? true : false;
                        task.file = null;
                        task.fileAlready = task.observationFile ? true : false;
                        vm.revisionTaskCategories[i].tasks.push(task)
                    }
                }
            }
            vm.revision.revisionTasks = vm.revisionTaskCategories;
            vm.isReady = true;
        }

        loadAllCategories()

        function loadAllCategories() {
            RevisionTaskCategory.findByCompany({
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    vm.revisionTaskCategories.push(data[i]);
                }
                formatCategories(vm.revision.revisionTasks);
            }

            function onError(error) {
                Modal.toast("Ha ocurrido un error inesperado")
            }
        }

        function save() {
            vm.isSaving = true;
            if (vm.revision.id !== null) {
                Revision.update(vm.revision, onSaveSuccess, onSaveError);
            } else {
                Revision.save(vm.revision, onSaveSuccess, onSaveError);
            }
        }

        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        function saveForm() {
            Modal.confirmDialog("¿Está seguro que desea guardar las observaciones?", "", function () {
                vm.isSaving = true;
                vm.isSavingFile = true;
                Modal.showLoadingBar();
                if (vm.fileName != "already" && vm.fileName != null) {
                    upload();
                } else {
                    if (vm.revision.id !== null) {
                        Revision.update(vm.revision, onSaveSuccessRevisionForm, onSaveError);
                    } else {
                        Revision.save(vm.revision, onSaveSuccessRevisionForm, onSaveError);
                    }
                }
            })
        }

        function saveTask(task) {
            vm.isSaving = true;
            task.hasObservations = task.showObservations ? 1 : 0;
            if (task.id !== null) {
                RevisionTask.update(task, onSaveSuccess, onSaveError);
            } else {
                RevisionTask.save(task, onSaveSuccess, onSaveError);
            }
        }

        function saveTaskForm(task) {
            Modal.confirmDialog("¿Está seguro que desea guardar las observaciones?", "", function () {
                vm.isSaving = true;
                vm.isSavingFileTask = true;
                task.hasObservations = task.showObservations ? 1 : 0;
                Modal.showLoadingBar();
                if (!task.fileAlready && task.fileName != null) {
                    uploadTask(task);
                } else {
                    if (task.id !== null) {
                        RevisionTask.update(task, function (result) {
                            vm.isSaving = false;
                            task.observationFile = result.observationFile;
                            task.fileName = result.fileName;
                            task.fileAlready = task.observationFile ? true : false;
                            Modal.hideLoadingBar();
                            vm.isSavingFileTask = false;
                        }, onSaveError);
                    } else {
                        RevisionTask.save(task, function (result) {
                            vm.isSaving = false;
                            task.observationFile = result.observationFile;
                            task.fileName = result.fileName;
                            task.fileAlready = task.observationFile ? true : false;
                            Modal.hideLoadingBar();
                            vm.isSavingFileTask = false;
                        }, onSaveError);
                    }
                }
            });

        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
        }

        function onSaveSuccessRevisionForm(result) {
            vm.isSaving = false;
            vm.revision.fileUrl = result.fileUrl;
            vm.fileName = vm.revision.fileUrl ? "already" : null;
            Modal.hideLoadingBar();
            vm.isSavingFile = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.executionDate = false;

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

        vm.setFileTask = function ($file, task) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                task.file = $file;
                task.fileName = $file.name;
            }
        };
    }
})();
