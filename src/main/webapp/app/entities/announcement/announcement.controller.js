(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementController', AnnouncementController);

    AnnouncementController.$inject = ['Announcement', 'ParseLinks', 'AlertService', 'paginationConstants','$rootScope'];

    function AnnouncementController(Announcement, ParseLinks, AlertService, paginationConstants,$rootScope) {

        var vm = this;

        vm.announcements = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        setTimeout(function(){
                loadAll();
        },400)

        vm.publish = function(announcement){
        announcement.status = 2;
        announcement.publishingDate = moment(new Date()).format();
        Announcement.update(announcement, onSaveSuccess, onError);
         function onSaveSuccess(){
         loadAll();
         }
          function onError(error) {
             toastr["error"]("Ha ocurrido un error actualizando la noticia.")
         }
        }

        vm.unPublish = function(announcement){
        announcement.status = 3;
        Announcement.update(announcement, onSaveSuccess, onError);
         function onSaveSuccess(){
                 loadAll();
                 }
                  function onError(error) {
                                 toastr["error"]("Ha ocurrido un error actualizando la noticia.")
                             }
        }


        function loadAll () {
            Announcement.queryAsAdmin({
                companyId: $rootScope.companyId,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.announcements = [];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.announcements.push(data[i]);
                }
                setTimeout(function() {
                $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                $("#tableData").fadeIn('slow');
                },900 )
            }

            function onError(error) {
                toastr["error"]("Ha ocurrido un error actualizando la noticia.")
            }
        }

        function reset () {
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
