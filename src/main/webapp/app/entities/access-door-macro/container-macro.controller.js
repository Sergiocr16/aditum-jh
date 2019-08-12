(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ContainerMacroController', ContainerMacroController);

    ContainerMacroController.$inject = ['$mdToast', '$timeout', 'Auth', '$state', '$scope', '$rootScope', 'MacroCondominium', 'globalCompany'];

    function ContainerMacroController($mdToast, $timeout, Auth, $state, $scope, $rootScope, MacroCondominium, globalCompany) {
        var vm = this;
        $rootScope.mainTitle = "Puerta de Acceso";
        MacroCondominium.getCondos({id: globalCompany.getId()}, function (data) {
            $rootScope.macroCondominium = data;
        });
        $rootScope.condominiumSelected = -1;
        $rootScope.houseSelected = -1;
        var delay = 1000;
        $rootScope.online = true;
        var toastOffline;
        Offline.on('confirmed-down', function () {
            if ($rootScope.online) {
                toastOffline = $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo perdió conexión a internet.")
                        .hideDelay(0)
                        .position("top center")
                );
                $rootScope.online = false;
            }
        });

        Offline.on('confirmed-up', function () {
            if (!$rootScope.online) {
                $mdToast.hide();
                $mdToast.show(
                    $mdToast.simple()
                        .textContent("Tu dispositivo está conectado a internet nuevamente.")
                        .position("top center")
                );
                $rootScope.online = true;
            }
        });
        $rootScope.timerAd = $timeout(function retry() {
            Offline.check();
            $timeout(retry, delay);
        }, delay);
    }
})();
