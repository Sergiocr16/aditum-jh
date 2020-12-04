(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivityResidentController', ActivityResidentController);

    ActivityResidentController.$inject = ['Principal', '$state', 'ActivityResident', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Modal', '$rootScope', '$scope'];

    function ActivityResidentController(Principal, $state, ActivityResident, ParseLinks, AlertService, paginationConstants, pagingParams, Modal, $rootScope, $scope) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        $rootScope.mainTitle = 'Actividad';
        vm.page = 0;
        vm.links = {
            last: 0
        };
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        moment.locale("es")
        vm.activityResidents = [];
        Principal.identity().then(function (account) {
            vm.userId = account.id;
            console.log(account)
            loadAll();
        })

        function loadAll() {
            ActivityResident.getByUser({
                page: vm.page,
                userId: vm.userId,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = ['date,desc'];
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                for (var i = 0; i < data.length; i++) {
                    data[i].dateFormat = moment(data[i].date).format("DD MMM YYYY hh:mm a");
                    data[i].seenFormat = data[i].seen;
                    vm.activityResidents.push(data[i])
                    if (data[i].seenFormat == 0) {
                        data[i].seen = 1;
                        ActivityResident.update(data[i]);
                    }
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
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
