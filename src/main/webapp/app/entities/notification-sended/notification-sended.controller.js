(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NotificationSendedController', NotificationSendedController);

    NotificationSendedController.$inject = ['$rootScope','$state', 'NotificationSended', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'globalCompany'];

    function NotificationSendedController($rootScope,$state, NotificationSended, ParseLinks, AlertService, paginationConstants, pagingParams, globalCompany) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        vm.notificationSendeds = [];
        moment.locale("es")
        $rootScope.active = "sended-notifications";
        $rootScope.mainTitle = "QuickMessage"
        loadAll();
        vm.page = 0;
        vm.links = {
            last: 0
        };

        function loadAll() {
            NotificationSended.findAllByCompany({
                page: vm.page,
                size: vm.itemsPerPage,
                companyId: globalCompany.getId(),
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = ['date,desc'];
                vm.isReady = true;
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                for (var i = 0; i < data.length; i++) {
                    var n = data[i];
                    n.date = moment(n.date).format("MMMM DD, h:mm a");
                    vm.notificationSendeds.push(n)
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            loadAll()
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
