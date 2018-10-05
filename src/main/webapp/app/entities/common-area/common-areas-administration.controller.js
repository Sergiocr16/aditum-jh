(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaAdministrationController', CommonAreaAdministrationController);

    CommonAreaAdministrationController.$inject = ['AlertService','$rootScope','Principal','$state','$localStorage'];

    function CommonAreaAdministrationController(AlertService,$rootScope,Principal,$state,$localStorage) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;

    }
})();
