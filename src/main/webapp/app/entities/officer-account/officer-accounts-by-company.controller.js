(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountsByCompanyController', OfficerAccountsByCompanyController);

    OfficerAccountsByCompanyController.$inject = ['$state','$stateParams','Company','DataUtils', 'OfficerAccount', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','$rootScope'];

    function OfficerAccountsByCompanyController($state,$stateParams,Company,DataUtils, OfficerAccount, ParseLinks, AlertService, paginationConstants,Principal,$rootScope) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;



        loadAll ();
        function loadAll () {
            OfficerAccount.getOfficerAccountsByCompanyId({
                companyId: $stateParams.companyId
            }, onSuccess, onError);
            function onSuccess(data) {
                vm.officerAccount = data;

            }
            function onError(error) {
                AlertService.error(error.data.message);

            }

        }

        vm.viewDetail = function(officerAccountId){
            $state.go('officerAccount-info-detail', {id: officerAccountId});

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
