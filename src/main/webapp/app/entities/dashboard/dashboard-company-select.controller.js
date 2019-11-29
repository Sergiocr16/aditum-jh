(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DashboardCompanySelectController', DashboardCompanySelectController);

    DashboardCompanySelectController.$inject = ['globalCompany','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'Watch', 'AccessDoor', 'Company', 'MultiCompany', '$rootScope', '$state', 'Auth'];

    function DashboardCompanySelectController(globalCompany,$timeout, $scope, $stateParams, $uibModalInstance, Watch, AccessDoor, Company, MultiCompany, $rootScope, $state, Auth) {
        var vm = this;
        vm.clear = clear;
        $rootScope.active = "selectCondominio";
        $rootScope.backgroundSelectCompany = true;
        $rootScope.selectedAlready = false;
        setTimeout(function () {
            loadAll();
        }, 500)
        $("#selectCompany").fadeIn();
        $("#dashboard").fadeOut();

        var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

        if(companyConfig.initialConfiguration==0){
            $state.go('dashboard.initialConfiguration');
        }

        function logout() {
            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
        }

        vm.selectCompany = function (id) {
            $rootScope.companyUser.companyId = id;
            $rootScope.companyId = id;
            MultiCompany.getCurrentUserCompany().then(function (data) {
                Company.get({id: $rootScope.companyUser.companyId}, function (condo) {
                    vm.contextLiving = " / " + condo.name;
                    $rootScope.contextLiving = vm.contextLiving;
                    $rootScope.currentUserImage = data.image_url;
                    if (condo.active == 0) {
                        logout();
                    }
                })
                $state.go('dashboard', null, {reload: false, notify: false});
                vm.clear();
                setTimeout(function () {
                    $rootScope.selectedAlready = true;
                }, 2000)

                $rootScope.loadingDash = true;
            })

        }

        function loadAll() {
            MultiCompany.getCurrentUserCompany().then(function (data) {
                if (data != null) {
                    $rootScope.companyUser = data;
                    vm.companies = data.companies;
                    setTimeout(function () {
                        $("#loadingIcon").fadeOut(300);
                    }, 400)
                    setTimeout(function () {
                        $("#tableData").fadeIn('slow');
                    }, 900)
                }
            })
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }


    }
})();
