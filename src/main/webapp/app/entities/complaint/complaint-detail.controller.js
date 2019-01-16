(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDetailController', ComplaintDetailController);

    ComplaintDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Complaint', 'ComplaintComment', 'companyUser', 'Modal'];

    function ComplaintDetailController($scope, $rootScope, $stateParams, previousState, entity, Complaint, ComplaintComment, companyUser, Modal) {
        var vm = this;

        vm.complaint = entity;
        vm.complaint.showingCommentForm = false;
        vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
        vm.previousState = previousState.name;
        $rootScope.mainTitle = "Detalle Queja o Sugerencia"
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
            complaint.currentComment.comment = undefined;
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

            return comment.resident.id == companyUser.id && comment.resident.identificationnumber == companyUser.identificationnumber;
        }

        function showActionDelete(comment) {
            return showActionEdit(comment) || companyUser.companies !== undefined
        }

        function onSaveError() {
            Modal.toast("Ah ocurrido un error actualizando el estado de la noticia")
        }

        function saveComment() {
            Modal.confirmDialog("¿Está seguro que desea enviar la respuesta?", "", function () {
                Modal.showLoadingBar();
                var comment = {
                    description: vm.newComment.description,
                    creationDate: moment(new Date()).format(),
                    residentId: companyUser.companies === undefined ? companyUser.id : null,
                    adminInfoId: companyUser.companies !== undefined ? companyUser.id : null,
                    complaintId: vm.complaint.id,
                    deleted: 0
                };
                ComplaintComment.save(comment,
                    function (result) {
                        Modal.toast("Respuesta enviado correctamente.");
                        Modal.hideLoadingBar();
                        vm.complaint.complaintComments = undefined;
                        Complaint.update(vm.complaint, function (result) {
                            vm.complaint = result;
                            vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                            formatComments(vm.complaint.complaintComments.content);
                            vm.newComment.description = undefined;
                        }, function () {
                            Modal.hideLoadingBar();

                            Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                        });
                    }, function () {
                        Modal.hideLoadingBar();

                        Modal.toast("Ha ocurrido un error enviando tu respuesta.")

                    });
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

        vm.submitEditComment = function (comment) {
            if (comment.description == comment.newComment) {
                Modal.toast("Comentario editado correctamente.");
                formatComments(vm.complaint.complaintComments.content);
            } else {
                Modal.confirmDialog("¿Está seguro que desea editar el comentario?", "", function () {
                    Modal.showLoadingBar();

                    var editedComment = {
                        description: comment.newComment,
                        creationDate: comment.creationDate,
                        residentId: companyUser.companies === undefined ? companyUser.id : null,
                        adminInfoId: companyUser.companies !== undefined ? companyUser.id : null,
                        complaintId: vm.complaint.id,
                        id: comment.id,
                        deleted: 0,
                        editedDate: moment(new Date()).format()
                    };
                    ComplaintComment.update(editedComment,
                        function () {
                            Modal.toast("Comentario editado correctamente.");
                            vm.complaint.complaintComments = undefined;
                            Complaint.update(vm.complaint, function (result) {
                                vm.complaint = result;
                                vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                formatComments(vm.complaint.complaintComments.content);
                                Modal.hideLoadingBar();

                            }, function () {
                                Modal.toast("Ha ocurrido un error enviando tu respuesta.")
                                Modal.hideLoadingBar();
                            });
                        }, function () {
                            Modal.toast("Ha ocurrido un error editando tu comentario.")
                            Modal.hideLoadingBar();

                        });
                });
            }
        };

        vm.cancelEditing = function (comment) {
            comment.newComment = comment.description;
            comment.editing = false;
        };

        $scope.$on('$destroy', unsubscribe);
    }
})();
