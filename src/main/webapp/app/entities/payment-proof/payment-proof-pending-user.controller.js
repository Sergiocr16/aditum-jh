(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofPendingUserController', PaymentProofPendingUserController);

    PaymentProofPendingUserController.$inject = ['CommonMethods', '$localStorage', '$state', 'PaymentProof', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'companyUser'];

    function PaymentProofPendingUserController(CommonMethods, $localStorage, $state, PaymentProof, ParseLinks, AlertService, paginationConstants, pagingParams, companyUser) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        loadAll();
        vm.detailPayment = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-detail', {
                id: encryptedId
            })
        }

        function loadAll() {
            PaymentProof.findByHouseId({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                houseId: $localStorage.houseSelected.id,
                status: 1,
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
                vm.isReady = true;
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.paymentProofs = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
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
