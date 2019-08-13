(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminsByCompanyController', AdminsByCompanyController);

    AdminsByCompanyController.$inject = ['User', 'Modal', 'globalCompany', 'MacroCondominium', '$state', '$stateParams', 'Company', 'DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants', 'Principal', '$rootScope', 'CommonMethods'];

    function AdminsByCompanyController(User, Modal, globalCompany, MacroCondominium, $state, $stateParams, Company, DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants, Principal, $rootScope, CommonMethods) {

        var vm = this;
        $rootScope.active = "admins";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        var admins = [];
        vm.isReady = false;
        if ($stateParams.companyId !== undefined) {
            var companyId = CommonMethods.decryptIdUrl($stateParams.companyId);
        } else {
            loadCondos();
        }

        function loadCondos() {
            Company.query(onSuccessCompany);

            function onSuccessCompany(data, headers) {
                vm.condos = data;
                vm.companySelected = data[0];
                loadAll();
            }
        }

        vm.changeCompany = function (company, i) {
            vm.isReady = false;
            vm.adminInfos = [];

            if (company !== undefined) {
                vm.selectedIndex = i;
                vm.companySelected = company;


            } else {

            }

            loadAll();

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

        vm.deleteAdmin = function (admin) {

            Modal.confirmDialog("¿Está seguro que desea eliminar al administrador " + admin.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    vm.login = admin.userLogin;
                    AdminInfo.delete({
                        id: admin.id
                    }, onSuccessDelete);
                });


        };

        function onSuccessDelete() {
            User.delete({login: vm.login},
                function () {
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha eliminado el administrador correctamente.");
                    loadAll();
                });
        }


        vm.disableEnabledAdmin = function (adminInfo) {

            var correctMessage;

            if (adminInfo.enabled == 1) {
                correctMessage = "¿Está seguro que desea deshabilitar al administrador " + adminInfo.name + "?";
                adminInfo.enabled = 0;

            } else {
                correctMessage = "¿Está seguro que desea habilitar al administrador " + adminInfo.name + "?";
                adminInfo.enabled = 1;
            }

            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                AdminInfo.update(adminInfo, onSuccessDisabledAdmin);
            });

        };

        function onSuccessDisabledAdmin(data) {
            User.getUserById({
                id: data.userId
            }, onSuccessGetDisabledUser);

        }

        function onSuccessGetDisabledUser(data) {
            if (data.activated == 1) {
                data.activated = 0;
            } else {
                data.activated = 1;
            }

            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {
                Modal.toast("Se ha deshabilitado el usuario correctamente.");
                Modal.hideLoadingBar();
                loadAll();
            }
        }
    }
})();
