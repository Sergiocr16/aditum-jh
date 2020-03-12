(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementUserController', AnnouncementUserController);

    AnnouncementUserController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'globalCompany', 'Modal', 'CommonMethods', 'HouseLoginTracker', 'Principal'];

    function AnnouncementUserController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, globalCompany, Modal, CommonMethods, HouseLoginTracker, Principal) {

        var vm = this;
        $rootScope.active = 'userNews';
        $rootScope.mainTitle = 'Noticias';
        vm.isReady = false;
        moment.locale("es");
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
        loadAll();


        Principal.identity().then(function (account) {
                if (account !== null) {
                    switch (account.authorities[0]) {
                        case "ROLE_USER":
                            vm.houseLoginTracker = {
                                lastTime: moment(new Date()).format(),
                                user: account.id,
                                id: null,
                                houseId: globalCompany.getHouseId()
                            }
                            HouseLoginTracker.save(vm.houseLoginTracker, function () {
                            })
                            break;
                        case "ROLE_OWNER":
                            vm.houseLoginTracker = {
                                lastTime: moment(new Date()).format(),
                                user: account.id,
                                id: null,
                                houseId: globalCompany.getHouseId()
                            }
                            HouseLoginTracker.save(vm.houseLoginTracker, function () {
                            })
                            break;
                    }
                }
            }
        );

        function onSaveSuccess() {
            bootbox.hideAll();
            loadAll();
        }

        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.")
        }

        vm.hideCommentForm = function (announcement) {
            announcement.showingCommentForm = false;
            announcement.currentComment.comment = undefined;
        };

        vm.showCommentForm = function (announcement) {
            announcement.showingCommentForm = true;
        };

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            for (var i = 0; i < data.length; i++) {
                data[i].showDate = moment(data[i].publishingDate).fromNow();
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
            vm.isReady = true;
        }


        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.")
        }

        function onErrorComments(error) {
            Modal.toast("Ha ocurrido un error cargando los comentarios.")
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
            announcement.commentsPage = announcement.commentsPage + 1;
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
                    verifyIfCommentExist(data[i], announcement.comments) === false ? announcement.comments.push(data[i]) : false;
                }
                announcement.showingComments = true;
            }, onErrorComments);
        }

        function verifyIfCommentExist(comment, comments) {
            for (var i = 0; i < comments.length; i++) {
                if (comment.id === comments[i].id) {
                    return true;
                }
            }
            return false;
        }

        function loadAll() {
            vm.showingNews = true;
            Announcement.queryAsUser({
                companyId: globalCompany.getId(),
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }

        function saveComment(announcement) {
            Modal.confirmDialog("¿Está seguro que desea realizar el comentario?", "", function () {
                Modal.showLoadingBar();
                var comment = {
                    comment: announcement.currentComment.comment,
                    creationDate: moment(new Date()).format(),
                    residentId: globalCompany.getUserRole() === 'ROLE_USER' ? globalCompany.getUser().id : null,
                    adminInfoId: globalCompany.getUserRole() === 'ROLE_MANAGER' ? globalCompany.getUser().id : null,
                    announcementId: announcement.id,
                    deleted: 0
                };
                console.log(comment)
                Announcement.saveComment(comment,
                    function (data) {
                        Modal.toast("Comentario enviado.")
                        announcement.currentComment = {comment: ""};
                        announcement.commentsQuantity++;
                        data.showingDate = moment(data.creationDate).fromNow();
                        data.editing = false;
                        data.newComment = data.comment;
                        if (announcement.showingComments == true) {
                            announcement.comments.push(data);
                        }
                        Modal.hideLoadingBar();
                    }, function () {
                        Modal.hideLoadingBar()
                        Modal.toast("Ha ocurrido un error enviando tu comentario.")
                    });
            });
        }

        function deleteComment(comment, announcement) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el comentario?", "", function () {
                comment.deleted = 1;
                Modal.showLoadingBar();
                Announcement.deleteComment(comment,
                    function (result) {
                        Modal.toast("Comentario eliminado correctamente.");
                        announcement.commentsQuantity--;
                        Modal.hideLoadingBar();
                        CommonMethods.deleteFromArray(comment, announcement.comments)
                    }, function () {
                        Modal.hideLoadingBar()
                        Modal.toast("Ha ocurrido un error eliminando tu comentario.")
                    });

            })
        }

        function showActionEdit(comment) {
            return comment.resident.id == globalCompany.getUser().id && comment.resident.identificationnumber == globalCompany.getUser().idNumber;
        }

        function showActionDelete(comment) {
            return showActionEdit(comment) || globalCompany.getUserRole() === 'ROLE_MANAGER'
        }

        function editComment(comment) {
            comment.editing = true;
        }

        function submitEditComment(comment, announcement) {
            Modal.confirmDialog("¿Está seguro que desea editar el comentario?", "", function () {
                Modal.showLoadingBar();
                var editedComment = {
                    comment: comment.newComment,
                    creationDate: comment.creationDate,
                    residentId: globalCompany.getUserRole() === 'ROLE_USER' ? globalCompany.getUser().id : null,
                    adminInfoId: globalCompany.getUserRole() === 'ROLE_MANAGER' ? globalCompany.getUser().id : null,
                    announcementId: announcement.id,
                    id: comment.id,
                    deleted: 0,
                    editedDate: moment(new Date()).format()
                };
                Announcement.editComment(editedComment,
                    function (data) {
                        Modal.toast("Comentario editado correctamente.");
                        comment.editing = false;
                        comment.showingDate = moment(data.creationDate).fromNow();
                        comment.comment = data.comment;
                        comment.editedDate = data.editedDate
                        Modal.hideLoadingBar()
                    }, function () {
                        Modal.toast("Ha ocurrido un error editando tu comentario.")
                        Modal.hideLoadingBar()
                    });
            })

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


        function onSaveSuccess() {

            Modal.hideLoadingBar();
            loadAll();
        }


        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.")
        }

        vm.delete = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la noticia?",
                "Una vez eliminada no podrá ser recuperada.", function () {
                    Modal.showLoadingBar()
                    Announcement.delete({id: announcement.id},
                        function () {
                            Modal.hideLoadingBar();
                            Modal.toast("Se ha elminado la noticia correctamente.");
                            CommonMethods.deleteFromArray(announcement, vm.announcements);
                        });
                });
        };
        vm.unPublish = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea retirar la noticia?",
                "Una vez retirada no será visible para los condóminos.", function () {
                    announcement.status = 3;
                    Announcement.update(announcement, function () {
                        Modal.hideLoadingBar();
                        Modal.toast("Se retiro la noticia exitosamente.")
                        CommonMethods.deleteFromArray(announcement, vm.announcements);
                    }, onError);
                });
        };


    }
})();
