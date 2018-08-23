(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementUserController', AnnouncementUserController);

    AnnouncementUserController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', '$scope'];

    function AnnouncementUserController(Announcement, ParseLinks, AlertService, paginationConstants, $rootScope, $scope) {

        var vm = this;
        moment.locale("es")
        vm.announcements = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = 2;
        vm.showingNews = true;
        vm.loadAll = loadAll;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = false;
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


        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');

            for (var i = 0; i < data.length; i++) {
                data[i].publishingDate = moment(data[i].publishingDate).fromNow();
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
            var result = [];
            if (vm.predicate !== 'publishingDate') {
                result.push('publishingDate,desc');
            }
            return result;
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
