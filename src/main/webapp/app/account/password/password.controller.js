(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PasswordController', PasswordController);

    PasswordController.$inject = ['Auth', 'Principal', '$rootScope', 'Modal', '$scope'];

    function PasswordController(Auth, Principal, $rootScope, Modal, $scope) {
        var vm = this;
        $rootScope.mainTitle = "Cambiar mi contraseña";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.changePassword = changePassword;
        Modal.enteringForm(changePassword);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.doNotMatch = null;
        vm.error = null;
        vm.success = null;

        Principal.identity().then(function (account) {
            vm.account = account;
        });

        function changePassword() {
            if (vm.password !== vm.confirmPassword) {
                vm.error = null;
                vm.success = null;
                vm.doNotMatch = 'ERROR';
                Modal.toast("La contraseña y la confirmación de contraseña no coinciden.")
            } else {
                vm.doNotMatch = null;
                Modal.confirmDialog("¿Está seguro que desea cambiar su contraseña?","",function(){
                    Modal.showLoadingBar();
                    Auth.changePassword(vm.password).then(function () {
                        vm.error = null;
                        Modal.hideLoadingBar();
                        vm.success = 'OK';
                        Modal.toast("La contraseña ha sido cambiada exitosamente")
                    }).catch(function () {
                        Modal.hideLoadingBar();
                        vm.success = null;
                        vm.error = 'ERROR';
                        Modal.toast("Ah ocurrido un error cambiando la contraseña.")
                    });
                })

            }
        }
    }
})();
