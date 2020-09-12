(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualBillingFileDetailController', MensualBillingFileDetailController);

    MensualBillingFileDetailController.$inject = ['Principal', 'CommonMethods', 'globalCompany', '$scope', '$rootScope', '$stateParams', 'previousState', 'MensualBillingFile', 'Company', '$state', 'Modal'];

    function MensualBillingFileDetailController(Principal, CommonMethods, globalCompany, $scope, $rootScope, $stateParams, previousState, MensualBillingFile, Company, $state, Modal) {
        var vm = this;
        vm.previousState = previousState.name;
        var vm = this;
        vm.isReady = false;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $rootScope.active = "mensualBillingFile";

        if ($rootScope.monthArchives == undefined) {
            $state.go("mensual-billing-file")
        } else {
            vm.month = $rootScope.monthArchives.month;
            vm.year = $rootScope.monthArchives.year;
        }
        vm.files = [];
        $rootScope.mainTitle = vm.month.name + " " + vm.year;
        Principal.identity().then(function (account) {
            switch (account.authorities[0]) {
                case "ROLE_USER":
                    vm.userType = 2;
                    break;
                case "ROLE_OWNER":
                    vm.userType = 2;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 1;
                    break;
                case "ROLE_JD":
                    vm.userType = 1;
                    break;
            }
            if (vm.userType == 1) {
                MensualBillingFile.findAll({
                    companyId: globalCompany.getId(),
                    month: vm.month.number,
                    year: vm.year
                }, function (data) {
                    vm.files = data;
                    vm.isReady = true;
                })
            } else {
                MensualBillingFile.findAllWithPrivacy({
                    companyId: globalCompany.getId(),
                    month: vm.month.number,
                    year: vm.year
                }, function (data) {
                    vm.files = data;
                    vm.isReady = true;
                })
            }
        })


        var unsubscribe = $rootScope.$on('aditumApp:mensualBillingFileUpdate', function (event, result) {
            vm.mensualBillingFile = result;
        });

        vm.editFile = function (file) {
            var encryptedId = CommonMethods.encryptIdUrl(file.id)
            $state.go('mensual-billing-file.edit', {
                id: encryptedId
            })
        }

        vm.deleteFile = function (file) {
            Modal.confirmDialog("¿Está seguro que desea eliminar al archivo?", "",
                function () {
                    Modal.showLoadingBar();
                    MensualBillingFile.delete({id: file.id},
                        function () {
                            CommonMethods.deleteFromArray(file, vm.files)
                            Modal.toast("Se ha eliminado el archivo correctamente.");
                            Modal.hideLoadingBar();
                        });
                })
        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
