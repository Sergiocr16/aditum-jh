(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('IndividualReleaseDetailController', IndividualReleaseDetailController);

    IndividualReleaseDetailController.$inject = ['CompanyConfiguration','AditumStorageService', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Complaint', 'ComplaintComment', 'Modal', 'globalCompany'];

    function IndividualReleaseDetailController(CompanyConfiguration,AditumStorageService, $scope, $rootScope, $stateParams, previousState, entity, Complaint, ComplaintComment, Modal, globalCompany) {
        var vm = this;

        vm.complaint = entity;
        vm.complaint.showingCommentForm = false;
        vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
        vm.previousState = previousState.name;
        var file;

        CompanyConfiguration.get({id: globalCompany.getId()}, function (companyConfig) {
          vm.companyConfig = companyConfig
        })
        $rootScope.mainTitle = "Detalle comunicado";
        vm.showActionEdit = showActionEdit;
        vm.showActionDelete = showActionDelete;
        vm.saveComment = saveComment;
        vm.deleteComment = deleteComment;
        moment.locale('es');
        vm.newComment = {};
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        vm.options = {
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['view', ['fullscreen']],
                ['table', ['table']],
            ]
        }

        function formatComments(comments) {
            for (var i = 0; i < comments.length; i++) {
                var comment = comments[i];
                comment.showingDate = moment(comment.creationDate).fromNow();
                comment.editing = false;
                comment.newComment = comment.description;

            }
        }

        vm.hideCommentForm = function (complaint) {
            complaint.showingCommentForm = false;
            vm.newComment.fileName = undefined;
            vm.newComment.file = undefined;
            vm.newComment.description = undefined;
        };

        vm.showCommentForm = function (complaint) {
            complaint.showingCommentForm = true;
        };
        formatComments(vm.complaint.complaintComments.content);

        var unsubscribe = $rootScope.$on('aditumApp:complaintUpdate', function (event, result) {
            vm.complaint = result;
        });

        vm.setStatus = function (status) {
            setTimeout(function () {
                vm.complaint.status = status;
                vm.complaint.complaintComments = undefined;
                Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
            }, 300)
        };

        function onSaveSuccess(result) {
            Modal.toast("Se modificó el estado correctamente.")
            vm.complaint = result;
            vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
            formatComments(vm.complaint.complaintComments.content);
            vm.isSaving = false;
        }

        function showActionEdit(comment) {
           return comment.resident.id == globalCompany.getUser().id && comment.resident.identificationnumber == globalCompany.getUser().idNumber;
        }

        function showActionDelete(comment) {
           return showActionEdit(comment) || globalCompany.getUserRole() === 'ROLE_MANAGER';
        }

        function onSaveError() {
            Modal.toast("Ah ocurrido un error actualizando el estado de la noticia")
        }

        function saveComment() {
            Modal.confirmDialog("¿Está seguro que desea enviar la respuesta?", "", function () {
                Modal.showLoadingBar();
                vm.isSaving = true;
                var comment = {
                    description: vm.newComment.description,
                    creationDate: moment(new Date()).format(),
                    residentId: globalCompany.getUserRole() === 'ROLE_USER' ? globalCompany.getUser().id: null,
                    adminInfoId: globalCompany.getUserRole() === 'ROLE_MANAGER' ? globalCompany.getUser().id  : null,
                    complaintId: vm.complaint.id,
                    file:  vm.newComment.file,
                    fileName:  vm.newComment.fileName,
                    deleted: 0
                };
                if (comment.file) {
                    upload(comment);
                } else {
                    ComplaintComment.save(comment,
                        function (result) {
                            Modal.toast("Respuesta enviada correctamente.");
                            Modal.hideLoadingBar();
                            vm.complaint.complaintComments = undefined;
                            Complaint.update(vm.complaint, function (result) {
                                vm.complaint = result;
                                vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                formatComments(vm.complaint.complaintComments.content);
                                vm.newComment.description = undefined;
                            }, function () {
                                Modal.hideLoadingBar();
                                vm.isSaving = false;
                                vm.progress  = 0;
                                Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                            });
                        }, function () {
                            Modal.hideLoadingBar();
                            Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                            vm.isSaving = false;
                        });
                }
            });
        }

        function deleteComment(comment, announcement) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el comentario?", "", function () {
                Modal.showLoadingBar();
                comment.deleted = 1;
                ComplaintComment.delete(comment,
                    function (result) {
                        Modal.toast("Comentario eliminado correctamente.");
                        vm.complaint.complaintComments = undefined;
                        Complaint.update(vm.complaint, function (result) {
                            vm.complaint = result;
                            vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                            formatComments(vm.complaint.complaintComments.content);
                            Modal.hideLoadingBar();
                        }, function () {
                            Modal.hideLoadingBar();

                            Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                        });
                    }, function () {
                        Modal.hideLoadingBar();

                        Modal.toast("Ha ocurrido un error eliminando tu comentario.")
                    });
            });
        }

        vm.editComment = function (comment) {
            comment.editing = true;
            comment.newComment = comment.description;
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

        vm.deleteFile = function (comment) {
            var taskFile = comment.fileName;
            Modal.confirmDialog("¿Está seguro que desea eliminar el archivo?", "Una vez eliminado no lo podrá recuperar", function () {
                comment.fileName = null;
                AditumStorageService.deleteFromUrl(globalCompany.getId() + '/complaints/' + vm.complaint.houseId + '/comments/' + taskFile);
                comment.fileUrl = null;
                comment.fileName = null;
                ComplaintComment.update(comment,
                    function (result) {
                        Modal.toast("Se elimino el archivo correctamente");
                    })
            })
        };

        function upload(comment) {
            var fileName = makeid(10, comment.fileName);
            comment.fileName = fileName;
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/complaints/' + vm.complaint.houseId + '/comments/' + fileName).put(comment.file);
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
                    comment.fileUrl = downloadURL;
                    if(comment.id){
                        ComplaintComment.update(comment,
                            function (result) {
                                Modal.toast("Respuesta enviada correctamente.");
                                Modal.hideLoadingBar();
                                vm.isSaving = false;

                                vm.complaint.complaintComments = undefined;
                                Complaint.update(vm.complaint, function (result) {
                                    vm.complaint = result;
                                    vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                    formatComments(vm.complaint.complaintComments.content);
                                    vm.newComment.description = undefined;
                                    vm.progress  = 0;
                                }, function () {
                                    vm.isSaving = false;
                                    Modal.hideLoadingBar();
                                    Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                                });
                            }, function () {
                                Modal.hideLoadingBar();
                                vm.isSaving = false;
                                Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                            });
                    }else{
                        ComplaintComment.save(comment,
                            function (result) {
                                Modal.toast("Respuesta enviada correctamente.");
                                Modal.hideLoadingBar();
                                vm.complaint.complaintComments = undefined;
                                Complaint.update(vm.complaint, function (result) {
                                    vm.complaint = result;
                                    vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                    formatComments(vm.complaint.complaintComments.content);
                                    vm.newComment.description = undefined;
                                    vm.progress = 0;
                                    vm.isSaving = false;
                                }, function () {
                                    vm.isSaving = false;
                                    Modal.hideLoadingBar();
                                    Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                                });
                            }, function () {
                                Modal.hideLoadingBar();
                                vm.isSaving = false;
                                Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                            });
                    }

                });
            });
        }

        vm.submitEditComment = function (comment) {
            if (comment.description == comment.newComment && !comment.file) {
                Modal.toast("Comentario editado correctamente.");
                vm.isSaving = false;
                formatComments(vm.complaint.complaintComments.content);
            } else {
                Modal.confirmDialog("¿Está seguro que desea editar el comentario?", "", function () {
                        Modal.showLoadingBar();
                        var editedComment = {
                            description: comment.newComment,
                            creationDate: comment.creationDate,
                            residentId: globalCompany.getUserRole() === 'ROLE_USER' ? globalCompany.getUser().id: null,
                            adminInfoId: globalCompany.getUserRole() === 'ROLE_MANAGER' ? globalCompany.getUser().id  : null,
                            complaintId: vm.complaint.id,
                            file :comment.file,
                            fileName: comment.fileName,
                            id: comment.id,
                            deleted: 0,
                            editedDate: moment(new Date()).format()
                        };
                        if (comment.file) {
                            upload(editedComment);
                        } else {
                            ComplaintComment.update(editedComment,
                                function () {
                                    vm.complaint.complaintComments = undefined;
                                    Complaint.update(vm.complaint, function (result) {
                                        vm.complaint = result;
                                        vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                        formatComments(vm.complaint.complaintComments.content);
                                        Modal.hideLoadingBar();
                                        vm.isSaving = false;
                                        Modal.toast("Comentario editado correctamente.");
                                    }, function () {
                                        Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                                        Modal.hideLoadingBar();
                                    });
                                }, function () {
                                    Modal.toast("Ha ocurrido un error editando tu comentario.")
                                    Modal.hideLoadingBar();

                                });
                        }
                    }
                );
            }
        };

        vm.cancelEditing = function (comment) {
            comment.newComment = comment.description;
            comment.editing = false;
        };
        vm.setFile = function ($file, comment) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                comment.file = $file;
                comment.fileName = $file.name;
            }
        };
        $scope.$on('$destroy', unsubscribe);
    }
})();
