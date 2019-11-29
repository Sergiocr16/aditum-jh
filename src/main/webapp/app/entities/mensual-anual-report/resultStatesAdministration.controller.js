(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResultStatesAdministrationController', ResultStatesAdministrationController);

    ResultStatesAdministrationController.$inject = ['AlertService','$rootScope','Principal','MensualAndAnualReport','$state'];

    function ResultStatesAdministrationController(AlertService,$rootScope,Principal,MensualAndAnualReport,$state) {
        $rootScope.active = "estadoResultados";
        var vm = this;

    }
})();
