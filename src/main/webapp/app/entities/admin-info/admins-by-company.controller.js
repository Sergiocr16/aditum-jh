(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminsByCompanyController', AdminsByCompanyController);

    AdminsByCompanyController.$inject = ['$state','$uibModalInstance','$stateParams','Company','DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','$rootScope'];

    function AdminsByCompanyController($state,$uibModalInstance,$stateParams,Company,DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants,Principal,$rootScope) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        var admins = [];
        vm.clear = clear;

        function clear () {
            $uibModalInstance.dismiss('cancel');
            $state.go('company');
        }

        loadAll ();
        function loadAll () {
            AdminInfo.getAdminsByCompanyId({
                companyId: $stateParams.companyId
            }, onSuccess, onError);
            function onSuccess(data) {
                admins = data;
               Company.query(onSuccessCompany, onError);

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
            function onSuccessCompany(data) {
                vm.companies = data;
                vm.adminInfos = formatAdminInfo();
            }

        }

        vm.viewDetail = function(adminId){
            $uibModalInstance.close();
            $state.go('admin-info-detail', {id: adminId});

        }
        function formatAdminInfo() {
            for (var i = 0; i < admins.length; i++) {

                for (var e = 0; e < vm.companies.length; e++) {
                    if (admins[i].companyId == vm.companies[e].id) {
                        admins[i].companyId = vm.companies[e].name;
                        admins[i].name = admins[i].name + " " + admins[i].lastname + " " + admins[i].secondlastname ;
                    }
                }
            }

            return admins;
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
