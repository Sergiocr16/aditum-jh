(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyDialogController', EmergencyDialogController);

    EmergencyDialogController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'Principal', 'WSEmergency', '$rootScope', 'Modal'];

    function EmergencyDialogController($timeout, $scope, $state, $stateParams, Principal, WSEmergency, $rootScope, Modal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        $rootScope.mainTitle = "Reportar emergencia"
        vm.reportEmergency = save;
        $rootScope.active = "reportemergencyactive";
        vm.enCamino = undefined;


        setTimeout(function () {
            var emergencyCode = $rootScope.companyId + "" + $rootScope.companyUser.houseId;
            WSEmergency.subscribeAttended(emergencyCode);
            WSEmergency.receiveAttented(emergencyCode).then(null, null, emergencyAttended)
            $rootScope.$on('$stateChangeStart',
                function (event, toState, toParams, fromState, fromParams) {
                    WSEmergency.unsubscribeAttended(emergencyCode);
                });
        }, 1200)

        function formatValidEmergency() {
            vm.emergency = {};
            vm.emergency.companyId = $rootScope.companyId;
            vm.emergency.houseId = $rootScope.companyUser.houseId;
            vm.emergency.isAttended = 0;
        }

        function save() {
            Modal.confirmDialog("¿Seguro que desea reportar una emergencia?", "", function () {
                formatValidEmergency();

                WSEmergency.sendActivity(vm.emergency);
                Modal.toast("Se ha reportado la emergencia, enseguida será notificada a los oficiales");
                setTimeout(function () {
                    Modal.toast("Mantente en esta ventana para ser notificado(a) cuando los oficiales hayan escuchado tu alerta y vengan en camino.");
                }, 3000)
                vm.emergency = undefined;
            })

        }


        vm.closeIt = function () {
            $('#calma').fadeOut(300)
            setTimeout(function () {
                $('#normal').fadeIn(300)
            }, 300)
        }

        function emergencyAttended(emergency) {
            $('#normal').fadeOut(300)
            setTimeout(function () {
                $('#calma').fadeIn(300)
            }, 300)
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:emergencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
