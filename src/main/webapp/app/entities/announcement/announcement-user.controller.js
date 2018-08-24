(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementUserController', AnnouncementUserController);

    AnnouncementUserController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'companyUser'];

    function AnnouncementUserController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, companyUser) {

        var vm = this;
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


        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');

            for (var i = 0; i < data.length; i++) {
                data[i].publishingDate = moment(data[i].publishingDate).fromNow();
                data[i].comments = [];
                data[i].showingComments = false;
                data[i].currentComment = {comment: ""};
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
                result.push('creationDate,asc');
            }
            return result;
        }

        function loadComments(announcement) {
            announcement.comments = [];
            if (announcement.showingComments === true) {
                announcement.showingComments = false;
            } else {

                Announcement.getComments({
                    announcementId: announcement.id,
                    // page: vm.page,
                    // size: vm.itemsPerPage,
                    sort: sortComment()
                }, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].creationDate = moment(data[i].creationDate).fromNow();
                        data[i].editing = false;
                        data[i].newComment =  data[i].comment;
                        announcement.comments.push(data[i]);
                    }
                    announcement.showingComments = true;
                }, onErrorComments);
            }
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
                            announcementId: announcement.id
                        };
                        Announcement.saveComment(comment,
                            function () {
                                toastr["success"]("Comentario enviado.")
                                announcement.currentComment = {comment: ""};
                                announcement.commentsQuantity++;
                                loadComments(announcement);
                            }, function () {
                                toastr["error"]("Ha ocurrido un error enviando tu comentario.")

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
                            creationDate: moment(new Date()).format(),
                            residentId: companyUser.companies === undefined ? companyUser.id : null,
                            adminInfoId: companyUser.companies !== undefined ? companyUser.id : null,
                            announcementId: announcement.id,
                            id: comment.id
                        };
                        Announcement.editComment(editedComment,
                            function (result) {
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
