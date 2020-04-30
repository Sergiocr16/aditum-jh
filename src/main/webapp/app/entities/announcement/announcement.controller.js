(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementController', AnnouncementController);

    AnnouncementController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'globalCompany', 'Modal', '$state', 'CommonMethods'];

    function AnnouncementController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, globalCompany, Modal, $state, CommonMethods) {

        var vm = this;
        $rootScope.active = 'announcements';
        $rootScope.mainTitle = 'Administrar noticias';
        vm.announcements = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = 15;
        vm.showingNews = true;
        vm.loadAll = loadAll;
        vm.isReady = false;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        // setTimeout(function () {
        loadAll();

        // }, 1000);




        function onSaveSuccess() {
            Modal.hideLoadingBar();
            loadAll();
        }

        vm.goTo = function (link) {
            $state.go(link)
        }

        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.")
        }

        vm.publish = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea publicar la noticia?",
                "Una vez publicada será visible para los condóminos.", function () {
                    announcement.status = 2;
                    announcement.publishingDate = moment(new Date()).format();
                    Modal.showLoadingBar();
                    Announcement.update(announcement, function(){
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha publicado la noticia.");
                    }, onError);
                });
        };


        vm.unPublish = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea retirar la noticia?",
                "Una vez retirada no será visible para los condóminos.", function () {
                    announcement.status = 3;
                    Announcement.update(announcement, function(){
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha retirado la noticia.");
                    }, onError);
                });
        };

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
console.log(data)
            for (var i = 0; i < data.length; i++) {
                vm.announcements.push(data[i]);
            }
            vm.isReady = true;
        }

        function onError(error) {
            Modal.toast("Ha ocurrido un error actualizando la noticia.");
        }

        function sort() {
            var result = [];
            if (vm.predicate !== 'publishingDate') {
                result.push('publishingDate,desc');
            }
            return result;
        }

        vm.delete = function (announcement) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la noticia?",
                "Una vez eliminada no podrá ser recuperada.", function () {
                    Modal.showLoadingBar()
                    Announcement.delete({id: announcement.id},
                        function () {
                            Modal.hideLoadingBar();
                            Modal.toast("Se ha elminado la noticia correctamente.");
                            CommonMethods.deleteFromArray(announcement,vm.announcements);
                        });
                });
        };


        function loadAll() {
            vm.showingNews = true;
            Announcement.queryAsAdmin({
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
