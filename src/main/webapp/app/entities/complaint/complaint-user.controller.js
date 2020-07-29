(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintUserController', ComplaintUserController);

    ComplaintUserController.$inject = ['$scope', '$mdDialog', 'Complaint', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'CommonMethods', '$state', 'globalCompany'];

    function ComplaintUserController($scope, $mdDialog, Complaint, ParseLinks, AlertService, paginationConstants, $rootScope, CommonMethods, $state, globalCompany) {

        var vm = this;
        $rootScope.active = 'complaint-user';
        $rootScope.mainTitle = "Mis tickets";
        vm.open = function (ev) {
            $mdDialog.show({
                templateUrl: 'app/entities/complaint/complaints-filter.html',
                scope: $scope,
                preserveScope: true,
                targetEvent: ev
            });
        };

        vm.close = function () {
            $mdDialog.hide();
        };
        vm.closeAndFilter = function () {
            vm.changeStatus();
            $mdDialog.hide();
        };
        vm.status = "-1";
        moment.locale("es");
        vm.complaints = [];
        vm.loadPage = loadPage;
        vm.loadAllByStatus = loadAllByStatus;
        vm.isReady = false;

        vm.itemsPerPage = paginationConstants.itemsPerPage;
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

        vm.changeStatus = function () {
            vm.page = 0;
            vm.isReady = false;
            vm.complaints = [];
            vm.links = {
                last: 0
            };
            vm.isReady = false;
            vm.loadAllByStatus();
        }

        function loadAllByStatus() {
            if (vm.status !== "-1") {
                Complaint.queryAsResidentByStatus({
                    residentId: globalCompany.getUser().id,
                    status: parseInt(vm.status),
                    page: vm.page,
                    category:1,
                    size: 10,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                loadAll();
            }

            function sort() {
                var result = [];
                if (vm.predicate !== 'creationDate') {
                    result.push('creationDate,desc');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    data[i].showingCreationDate = moment(data[i].creationDate).format("DD MMM")
                    vm.complaints.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            Complaint.queryAsResident({
                residentId: globalCompany.getUser().id,
                page: vm.page,
                category:1,
                size: 10,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [];
                if (vm.predicate !== 'creationDate') {
                    result.push('creationDate,desc');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    data[i].showingCreationDate = moment(data[i].creationDate).format("DD MMM")
                    vm.complaints.push(data[i]);
                }
                vm.isReady = true;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.complaints = [];
            loadAll();
        }

        vm.viewDetail = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('complaint-detail', {
                id: encryptedId
            });
        };

        function loadPage(page) {
            vm.page = page;
            if (vm.status !== "-1") {
                loadAllByStatus();
            } else {
                loadAll();
            }

        }
    }
})();
