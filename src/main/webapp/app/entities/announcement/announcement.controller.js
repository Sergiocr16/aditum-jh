(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementController', AnnouncementController);

    AnnouncementController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', '$scope'];

    function AnnouncementController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, $scope) {

        var vm = this;

        vm.announcements = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.showingNews = true;
        vm.loadAll = loadAll;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        setTimeout(function () {
            loadAll();
        }, 1000);


        function onSaveSuccess() {
            bootbox.hideAll();
            loadAll();
        }

        function onError(error) {
            toastr["error"]("Ha ocurrido un error actualizando la noticia.")
        }

        vm.publish = function (announcement) {

            bootbox.confirm({
                message: "¿Está seguro que desea publicar la noticia? , una vez publicada será visible para los condóminos.",
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
                        announcement.status = 2;
                        announcement.publishingDate = moment(new Date()).format();
                        Announcement.update(announcement, onSaveSuccess, onError);
                    }
                }
            });

        }


        vm.unPublish = function (announcement) {
            bootbox.confirm({
                message: "¿Está seguro que desea retirar la noticia? , una vez retirada no será visible para los condóminos.",
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
                        announcement.status = 3;
                        Announcement.update(announcement, onSaveSuccess, onError);
                    }
                }
            });
        };

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');

            for (var i = 0; i < data.length; i++) {
                vm.announcements.push(data[i]);
            }
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

        function sort() {
            var result = [];
            if (vm.predicate !== 'publishingDate') {
                result.push('publishingDate,desc');
            }
            return result;
        }

        vm.delete = function(announcement){
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar la noticia? , una vez eliminada no podrá ser recuperada.",
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
                        Announcement.delete({id: announcement.id},
                            function () {
                                toastr["success"]("Se ha elminado la noticia correctamente.")
                                loadAll();
                            });
                    }
                }
            });

        };


        function loadAll() {
            vm.showingNews = true;
            Announcement.queryAsAdmin({
                companyId: $rootScope.companyId,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);


        }

        vm.showSketches = function () {
            vm.announcements = [];
            vm.showingNews = false;
            Announcement.querySketches({
                companyId: $rootScope.companyId,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        };

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
