(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementDialogController', AnnouncementDialogController);

    AnnouncementDialogController.$inject = ['AditumStorageService','$state', '$rootScope', '$timeout', '$scope', '$stateParams', 'entity', 'Announcement', 'CommonMethods', 'Modal', 'globalCompany', 'DataUtils', 'SaveImageCloudinary'];

    function AnnouncementDialogController(AditumStorageService,$state, $rootScope, $timeout, $scope, $stateParams, entity, Announcement, CommonMethods, Modal, globalCompany, DataUtils, SaveImageCloudinary) {
        var vm = this;

        vm.announcement = entity;

        if (vm.announcement.imageBannerUrl != null) {
            vm.announcement.imageSet = true;
        }
        vm.sendEmail = 1;
        vm.fileNameStart = vm.announcement.fileName;
        var file;
        vm.announcement.sendEmail = 1;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        var fileImage = null;

        vm.isCreatingOne = $state.includes('announcement.new');
        if (vm.announcement.id == undefined) {
            $rootScope.mainTitle = 'Crear noticia';
        } else {
            $rootScope.mainTitle = 'Editar noticia';
        }
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
                ['view', ['fullscreen']],
            ]
        };
        vm.save = save;
        vm.saveAsSketch = saveAsSketch;
        Modal.enteringForm(save, "Publicar", saveAsSketch, "Guardar borrador");
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });

        vm.setImage = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        vm.displayImage = base64Data;
                        vm.displayImageType = $file.type;
                        vm.announcement.imageSet = true;
                    });
                });
                fileImage = $file;
            }
        };

        function save() {
            Modal.confirmDialog("¿Está seguro que desea publicar la noticia?", "Una vez publicada será visible para los condóminos", function () {
                Modal.showLoadingBar();
                vm.announcement.publishingDate = moment(new Date()).format();
                vm.announcement.status = 2;
                vm.announcement.companyId = globalCompany.getId();
                if(vm.fileName){
                    upload()
                }else{
                saveAnnouncement(vm.announcement)
                }
            })

        }

        function saveAsSketch() {
            vm.isSaving = true;
            Modal.showLoadingBar();
            vm.announcement.publishingDate = moment(new Date()).format();
            vm.announcement.status = 1;
            vm.announcement.companyId = globalCompany.getId();
            saveAnnouncement(vm.announcement)
        }


        function saveAnnouncement(announcement) {

            if (announcement.useBanner == 1) {
                if (fileImage !== null) {
                    if (announcement.title == null) {
                        announcement.title = "";
                    }
                    vm.announcementImage = {title: announcement.title};
                    SaveImageCloudinary
                        .saveAnnouncement(fileImage, vm.announcementImage)
                        .then(onSaveImageSuccess, onSaveError, onNotify);
                } else {
                    decideActionSave()
                }
            } else {
                decideActionSave()
            }
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
                    vm.announcement.fileUrl = downloadURL;
                    vm.announcement.fileName = fileName;
                    saveAnnouncement(vm.announcement)
                });
            });
        }

        function decideActionSave() {
            if(!vm.sendEmail){
                vm.announcement.sendEmail = 0;
            }
            if (vm.announcement.status == 1) {
                if (vm.announcement.id !== null) {
                    Announcement.update(vm.announcement, onSaveSuccessSketch, onSaveError);
                } else {
                    Announcement.save(vm.announcement, onSaveSuccessSketch, onSaveError);
                }
            } else {
                if (vm.announcement.id !== null) {
                    Announcement.update(vm.announcement, onSaveSuccess, onSaveError);
                } else {
                    Announcement.save(vm.announcement, onSaveSuccess, onSaveError);
                }
            }
        }


        function onNotify(info) {
            vm.progress = Math.round((info.loaded / info.total) * 100);
        }

        function onSaveImageSuccess(data) {
            vm.announcement.imageBannerUrl = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
            decideActionSave()
        }

        function onSaveSuccess(result) {
            vm.announcement = result;
            $state.go("announcements.announcement");
            Modal.hideLoadingBar();
            Modal.toast("Se ha publicado exitosamente el anuncio en el condominio.");
            vm.isSaving = false;
        }

        function onSaveSuccessSketch(result) {
            vm.announcement = result;
            $state.go("announcements.announcement-sketch");
            Modal.hideLoadingBar();
            Modal.toast("Guardado como borrador.");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
            Modal.toast("Ah ocurrido un error, es posible que el contenido del anuncio sea demasiado largo o tenga muchas imagenes en el mismo.")
            Modal.hideLoadingBar();
        }

        vm.datePickerOpenStatus.publishingDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.clear = function () {
            history.back();
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

