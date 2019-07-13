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
    }
})();
