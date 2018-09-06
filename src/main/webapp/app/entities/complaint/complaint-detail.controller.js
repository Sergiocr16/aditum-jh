(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDetailController', ComplaintDetailController);

    ComplaintDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Complaint', 'ComplaintComment', 'companyUser'];

    function ComplaintDetailController($scope, $rootScope, $stateParams, previousState, entity, Complaint, ComplaintComment, companyUser) {
        var vm = this;

        vm.complaint = entity;
        vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
        vm.previousState = previousState.name;
        vm.showActionEdit = showActionEdit;
        vm.saveComment = saveComment;
        vm.deleteComment = deleteComment;
        moment.locale('es');
        vm.newComment = {};

        function formatComments(comments){
            for (var i = 0; i <  comments.length; i++) {
                var comment = comments[i] ;
                comment.showingDate = moment(comment.creationDate).fromNow();
                comment.editing = false;
                comment.newComment = comment.description;
            }
        }
        formatComments(vm.complaint.complaintComments.content);
console.log(vm.complaint.complaintComments.content)

        setTimeout(function () {
            $("#loadingIcon").fadeOut(300);
        }, 400);
        setTimeout(function () {
            $("#tableData").fadeIn('slow');
        }, 900);
        var unsubscribe = $rootScope.$on('aditumApp:complaintUpdate', function (event, result) {
            vm.complaint = result;
        });

        vm.setStatus = function (status) {
            vm.complaint.status = status;
            vm.complaint.complaintComments = undefined;
            Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
        };

        function onSaveSuccess(result) {
            toastr["success"]("Se modificó el estado correctamente.")
            vm.complaint = result;
            vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
            formatComments(vm.complaint.complaintComments.content);
            vm.isSaving = false;
        }
        function showActionEdit(comment) {
            return comment.resident.id == companyUser.id && comment.resident.identificationnumber == companyUser.identificationnumber;
        }

        function onSaveError() {
            toastr["error"]("Ah ocurrido un error actualizando el estado de la noticia")
        }
        function saveComment() {
            bootbox.confirm({
                message: "¿Está seguro que desea enviar la respuesta?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {
                    if (result) {
                        var comment = {
                            description: vm.newComment.description,
                            creationDate: moment(new Date()).format(),
                            residentId: companyUser.companies === undefined ? companyUser.id : null,
                            adminInfoId: companyUser.companies !== undefined ? companyUser.id : null,
                            complaintId: vm.complaint.id,
                            deleted: 0
                        };
                        console.log(comment)
                        ComplaintComment.save(comment,
                            function (result) {
                                toastr["success"]("Respuesta enviado.");
                                vm.complaint.complaintComments = undefined;
                                Complaint.update(vm.complaint, function(result){
                                    vm.complaint = result;
                                    vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                    formatComments(vm.complaint.complaintComments.content);
                                    vm.newComment.description = undefined;
                                }, function(){
                                    toastr["error"]("Ha ocurrido un error enviando tu respuesta.")
                                });
                            }, function () {
                                toastr["error"]("Ha ocurrido un error enviando tu respuesta.")

                            });
                    }
                }
            });

        }

        function deleteComment(comment, announcement) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar el comentario?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {
                    if (result) {
                        comment.deleted = 1;
                        ComplaintComment.delete(comment,
                            function (result) {
                                toastr["success"]("Comentario eliminado correctamente.");
                                vm.complaint.complaintComments = undefined;
                                Complaint.update(vm.complaint, function(result){
                                    vm.complaint = result;
                                    vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                    formatComments(vm.complaint.complaintComments.content);
                                }, function(){
                                    toastr["error"]("Ha ocurrido un error enviando tu respuesta.")
                                });
                            }, function () {
                                toastr["error"]("Ha ocurrido un error eliminando tu comentario.")
                            });
                    }
                }
            });
        }

        vm.editComment = function(comment) {
            comment.editing = true;
        };

        vm.submitEditComment = function(comment) {
            bootbox.confirm({
                message: "¿Está seguro que desea editar el comentario?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {
                    if (result) {
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
                                toastr["success"]("Comentario editado correctamente.");
                                vm.complaint.complaintComments = undefined;
                                Complaint.update(vm.complaint, function(result){
                                    vm.complaint = result;
                                    vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
                                    formatComments(vm.complaint.complaintComments.content);
                                }, function(){
                                    toastr["error"]("Ha ocurrido un error enviando tu respuesta.")
                                });
                            }, function () {
                                toastr["error"]("Ha ocurrido un error editando tu comentario.")
                            });
                    }
                }
            });
        };

       vm.cancelEditing = function(comment) {
            comment.newComment = comment.comment;
            comment.editing = false;
        };

        $scope.$on('$destroy', unsubscribe);
    }
})();
