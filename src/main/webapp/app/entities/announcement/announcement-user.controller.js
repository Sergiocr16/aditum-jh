(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementUserController', AnnouncementUserController);

    AnnouncementUserController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'companyUser'];

    function AnnouncementUserController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, companyUser) {

        var vm = this;
        $rootScope.active = 'userNews';

        moment.locale("es")
        vm.announcements = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = 4;
        vm.showingNews = true;
        vm.loadAll = loadAll;
        vm.saveComment = saveComment;
        vm.loadComments = loadComments;
        vm.showActionEdit = showActionEdit;
        vm.showActionDelete = showActionDelete;
        vm.editComment = editComment;
        vm.cancelEditing = cancelEditing;
        vm.submitEditComment = submitEditComment;
        vm.deleteComment = deleteComment;
        vm.loadCommentsPage = loadCommentsPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = false;
        setTimeout(function () {
            loadAll();
        }, 2000);


        function onSaveSuccess() {
            bootbox.hideAll();
            loadAll();
        }

        function onError(error) {
            toastr["error"]("Ha ocurrido un error actualizando la noticia.")
        }

        vm.hideCommentForm = function (announcement) {
            announcement.showingCommentForm = false;
        };

        vm.showCommentForm = function (announcement) {
            announcement.showingCommentForm = true;
        };

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');

            for (var i = 0; i < data.length; i++) {
                data[i].publishingDate = moment(data[i].publishingDate).fromNow();
                data[i].comments = [];
                data[i].showingComments = false;
                data[i].currentComment = {comment: ""};
                data[i].showingCommentForm = false;
                data[i].commentsPage = 0;
                data[i].links = {
                    last: 0
                };
                vm.announcements.push(data[i]);
            }
            console.log(vm.announcements)
            setTimeout(function () {
                $("#loadingIcon").fadeOut(300);
            }, 400);
            setTimeout(function () {
                $("#tableData").fadeIn('slow');
            }, 900);
        }

        function onError(error) {
            toastr["error"]("Ha ocurrido un error actualizando la noticia.")
        }

        function onErrorComments(error) {
            toastr["error"]("Ha ocurrido un error cargando los comentarios.")
        }

        function sort() {
            var result = [];
            if (vm.predicate !== 'publishingDate') {
                result.push('publishingDate,desc');
            }
            return result;
        }

        function sortComment() {
            var result = [];
            if (vm.predicate !== 'publishingDate') {
                result.push('creationDate,desc');
            }
            return result;
        }

        function loadComments(announcement) {
            if (announcement.showingComments === true) {
                announcement.showingComments = false;
                announcement.comments = [];
                announcement.commentsPage = 0;
                announcement.links = {
                    last: 0
                };
            } else {
                Announcement.getComments({
                    announcementId: announcement.id,
                    page: announcement.commentsPage,
                    size: 5,
                    sort: sortComment()
                }, function (data, headers) {
                    announcement.links = ParseLinks.parse(headers('link'));
                    for (var i = 0; i < data.length; i++) {
                        data[i].showingDate = moment(data[i].creationDate).fromNow();
                        data[i].editing = false;
                        data[i].newComment = data[i].comment;
                        announcement.comments.push(data[i]);
                    }
                    announcement.showingComments = true;
                }, onErrorComments);
            }

        }

        function loadCommentsPage(announcement) {
            announcement.commentsPage =  announcement.commentsPage + 1;
                Announcement.getComments({
                    announcementId: announcement.id,
                    page: announcement.commentsPage,
                    size: vm.itemsPerPage,
                    sort: sortComment()
                }, function (data, headers) {
                    announcement.links = ParseLinks.parse(headers('link'));
                    for (var i = 0; i < data.length; i++) {
                        data[i].showingDate = moment(data[i].creationDate).fromNow();
                        data[i].editing = false;
                        data[i].newComment = data[i].comment;
                        console.log( announcement.comments.indexOf(data[i]));
                        verifyIfCommentExist(data[i],announcement.comments) === false ? announcement.comments.push(data[i]) : false ;
                    }
                    console.log(announcement.comments)
                    announcement.showingComments = true;
                }, onErrorComments);
        }

        function verifyIfCommentExist(comment,comments){
            for (var i = 0; i < comments.length; i++) {
                if(comment.id===comments[i].id){
                    return true;
                }
            }
            return false;
        }

        function loadAll() {
            vm.showingNews = true;
            Announcement.queryAsUser({
                companyId: $rootScope.companyId,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }

        function saveComment(announcement) {
            bootbox.confirm({
                message: "¿Está seguro que desea realizar el comentario?",
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
                            comment: announcement.currentComment.comment,
                            creationDate: moment(new Date()).format(),
                            residentId: companyUser.companies === undefined ? companyUser.id : null,
                            adminInfoId: companyUser.companies !== undefined ? companyUser.id : null,
                            announcementId: announcement.id,
                            deleted: 0
                        };
                        Announcement.saveComment(comment,
                            function () {
                                toastr["success"]("Comentario enviado.")
                                announcement.currentComment = {comment: ""};
                                announcement.commentsQuantity++;
                                announcement.showingComments = true;
                                loadComments(announcement);
                            }, function () {
                                toastr["error"]("Ha ocurrido un error enviando tu comentario.")

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
                        Announcement.deleteComment(comment,
                            function (result) {
                                toastr["success"]("Comentario eliminado correctamente.");
                                announcement.commentsQuantity--;

                                loadComments(announcement);
                            }, function () {
                                toastr["error"]("Ha ocurrido un error eliminando tu comentario.")
                            });
                    }
                }
            });
        }

        function showActionEdit(comment) {
            return comment.resident.id == companyUser.id && comment.resident.identificationnumber == companyUser.identificationnumber;
        }

        function showActionDelete(comment) {
            return showActionEdit(comment) || companyUser.companies !== undefined
        }

        function editComment(comment) {
            comment.editing = true;
        }

        function submitEditComment(comment, announcement) {
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
                            comment: comment.newComment,
                            creationDate: comment.creationDate,
                            residentId: companyUser.companies === undefined ? companyUser.id : null,
                            adminInfoId: companyUser.companies !== undefined ? companyUser.id : null,
                            announcementId: announcement.id,
                            id: comment.id,
                            deleted: 0,
                            editedDate: moment(new Date()).format()
                        };
                        Announcement.editComment(editedComment,
                            function () {
                                toastr["success"]("Comentario editado correctamente.");
                                loadComments(announcement);
                            }, function () {
                                toastr["error"]("Ha ocurrido un error editando tu comentario.")
                            });
                    }
                }
            });
        }


        function cancelEditing(comment) {
            comment.newComment = comment.comment;
            comment.editing = false;
        }


        function reset() {
            vm.page = 0;
            vm.announcements = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
