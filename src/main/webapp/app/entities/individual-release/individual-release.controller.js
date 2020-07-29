(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('IndividualReleaseController', IndividualReleaseController);

    IndividualReleaseController.$inject = ['$mdDialog','$scope','Complaint', 'ParseLinks', 'AlertService', 'paginationConstants', '$rootScope', 'CommonMethods', '$state', 'globalCompany'];

    function IndividualReleaseController($mdDialog,$scope,Complaint, ParseLinks, AlertService, paginationConstants, $rootScope, CommonMethods, $state, globalCompany) {

        var vm = this;
        $rootScope.active = 'individual-release';
        vm.status = "-1";
        $rootScope.mainTitle = "Comunicados individuales";
        vm.isReady = false;
        moment.locale("es");
        vm.complaints = [];
        vm.loadPage = loadPage;
        vm.loadAllByStatus = loadAllByStatus;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.showFilterDiv = false;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        // setTimeout(function () {
        loadAll();
        // }, 1000);
        vm.open = function(ev) {
            $mdDialog.show({
                templateUrl: 'app/entities/individual-release/individual-release-filter.html',
                scope: $scope,
                preserveScope: true,
                targetEvent: ev
            });
        };

        vm.close = function() {
            $mdDialog.hide();
        };
        vm.closeAndFilter = function() {
            vm.changeStatus();
            $mdDialog.hide();
        };
        vm.changeStatus = function () {
            vm.page = 0;
            vm.complaints = [];
            vm.links = {
                last: 0
            };
            vm.isReady = false;
            vm.loadAllByStatus();
        }

        function loadAllByStatus() {
            if (vm.status !== "-1") {
                Complaint.queryByStatus({
                    companyId: globalCompany.getId(),
                    status: parseInt(vm.status),
                    page: vm.page,
                    category:3,
                    size: 20,
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
            Complaint.query({
                companyId: globalCompany.getId(),
                page: vm.page,
                category:3,
                size: 20,
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
            $state.go('individual-release-detail', {
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
