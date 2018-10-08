(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementSketchController', AnnouncementSketchController);

    AnnouncementSketchController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'globalCompany', 'Modal'];

    function AnnouncementSketchController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, globalCompany, Modal) {

        var vm = this;

        vm.announcements = [];
        vm.loadPage = loadPage;
        $rootScope.mainTitle = 'Borradores';

        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.isReady = false;
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        loadAll();



        function onSaveSuccess() {
            bootbox.hideAll();
            loadAll();
        }

        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.")
        }

        vm.publish = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea publicar la noticia?", "Una vez publicada será visible para los condóminos", function () {
                Announcement.delete({id: announcement.id},
                    function () {
                        announcement.status = 2;
                        announcement.publishingDate = moment(new Date()).format();
                        Announcement.update(announcement, onSaveSuccess, onError);
                    });
            })
        };


        vm.delete = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la noticia?", "Una vez eliminada no podrá ser recuperada", function () {
                Announcement.delete({id: announcement.id},
                    function () {
                        Modal.toast("Se ha elminado la noticia correctamente.")
                        loadAll();
                    });
            })

        };

        function onSuccess(data, headers) {
            vm.announcements = [];
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');

            for (var i = 0; i < data.length; i++) {
                vm.announcements.push(data[i]);
            }
            vm.isReady = true;
        }

        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.")
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
                companyId: globalCompany.getId(),
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

        }

        vm.showSketches = function () {
            vm.announcements = [];
            vm.showingNews = false;
            Announcement.querySketches({
                companyId: globalCompany.getId(),
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
