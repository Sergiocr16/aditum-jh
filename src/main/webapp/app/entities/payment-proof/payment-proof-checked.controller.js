(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PaymentProofCheckedController', PaymentProofCheckedController);

    PaymentProofCheckedController.$inject = ['CommonMethods', '$rootScope', 'globalCompany', '$state', 'PaymentProof', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function PaymentProofCheckedController(CommonMethods, $rootScope, globalCompany, $state, PaymentProof, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isReady = false;
        vm.paymentProofs = [];
        loadAll();
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.detailPayment = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-detail', {
                id: encryptedId
            })
        }
        vm.detailProof = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('payment-proof-detail', {
                id: encryptedId
            })
        };
        function loadAll() {
            PaymentProof.query({
                page: vm.page,
                size: 20,
                companyId: globalCompany.getId(),
                status: 2,
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.isReady = true;
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                for (var i = 0; i < data.length; i++) {
                    vm.paymentProofs.push(data[i]);
                }
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
