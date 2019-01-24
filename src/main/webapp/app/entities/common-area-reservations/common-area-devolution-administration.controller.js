(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDevolutionAdministrationController', CommonAreaDevolutionAdministrationController);

    CommonAreaDevolutionAdministrationController.$inject = ['AlertService','$rootScope','Principal'];

    function CommonAreaDevolutionAdministrationController(AlertService,$rootScope,Principal) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;

    }
})();
