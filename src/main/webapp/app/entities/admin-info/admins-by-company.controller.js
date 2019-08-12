(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminsByCompanyController', AdminsByCompanyController);

    AdminsByCompanyController.$inject = ['globalCompany','MacroCondominium','$state', '$stateParams', 'Company', 'DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants', 'Principal', '$rootScope', 'CommonMethods'];

    function AdminsByCompanyController(globalCompany,MacroCondominium,$state, $stateParams, Company, DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants, Principal, $rootScope, CommonMethods) {

        var vm = this;
        $rootScope.active = "adminsByCompanyMacro";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        var admins = [];
        vm.isReady = false;
        if ($stateParams.companyId!==undefined) {
            var companyId = CommonMethods.decryptIdUrl($stateParams.companyId);
        }else{
            loadCondos();
        }

        function loadCondos() {
            MacroCondominium.getCondos({id: globalCompany.getMacroId()}, onSuccessHouses);
            function onSuccessHouses(data, headers) {
                vm.condos = data.companies;
                vm.companySelected = data.companies[0];
                loadAll();
            }
        }

        vm.changeCompany = function () {
            loadAll();
            vm.isReady = false;
        };


        function loadAll() {
            AdminInfo.getAdminsByCompanyId({
                companyId: vm.companySelected.id
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
                vm.isReady = true;
            }

        }

        vm.viewDetail = function (adminId) {
            var adminInfoId = CommonMethods.encryptIdUrl(adminId);
            $state.go('admin-info-detail', {id: adminInfoId});

        };

        function formatAdminInfo() {
            for (var i = 0; i < admins.length; i++) {

                for (var e = 0; e < vm.companies.length; e++) {
                    if (admins[i].companyId == vm.companies[e].id) {
                        admins[i].companyId = vm.companies[e].name;
                        admins[i].name = admins[i].name + " " + admins[i].lastname + " " + admins[i].secondlastname;
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
