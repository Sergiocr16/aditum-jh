(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementSketchController', AnnouncementSketchController);

    AnnouncementSketchController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', '$scope'];

    function AnnouncementSketchController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, $scope) {

        var vm = this;

        vm.announcements = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
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

        };


        function onSuccess(data, headers) {
            vm.announcements = [];
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');

            for (var i = 0; i < data.length; i++) {
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

        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function loadAll() {
            vm.announcements = [];
            vm.showingNews = true;
            Announcement.querySketches({
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
