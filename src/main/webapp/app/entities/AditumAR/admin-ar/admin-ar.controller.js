(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminsByCompanyARController', AdminsByCompanyARController);

    AdminsByCompanyARController.$inject = ['User', 'Modal', 'globalCompany', 'MacroCondominium', '$state', '$stateParams', 'Company', 'DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants', 'Principal', '$rootScope', 'CommonMethods'];

    function AdminsByCompanyARController(User, Modal, globalCompany, MacroCondominium, $state, $stateParams, Company, DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants, Principal, $rootScope, CommonMethods) {

        var vm = this;
        $rootScope.active = "adminsByCompanyAR";
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
                vm.condos = [];
                for (var i = 0; i < data.length; i++) {
                    if (data[i].productType == "ADITUM AR") {
                        vm.condos.push(data[i])
                    }
                }
                vm.companySelected = vm.condos[0];
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
            if (vm.companySelected != undefined) {
                AdminInfo.getAdminsByCompanyId({
                    companyId: vm.companySelected.id
                }, onSuccess, onError);

            }else{
                onError();
            }

            function onSuccess(data) {
                admins = data;
                Company.query(onSuccessCompany, onError);
            }

            function onError(error) {
                vm.adminInfos = [];
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


            } else {
                correctMessage = "¿Está seguro que desea habilitar al administrador " + adminInfo.name + "?";

            }

            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                if (adminInfo.enabled == 1) {
                    adminInfo.enabled = 0;

                } else {
                    adminInfo.enabled = 1;
                }
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
                if (data.activated == 1) {
                    Modal.toast("Se ha habilitado el usuario correctamente.");
                } else {
                    Modal.toast("Se ha deshabilitado el usuario correctamente.");
                }

                Modal.hideLoadingBar();
                loadAll();
            }
        }
    }
})();
