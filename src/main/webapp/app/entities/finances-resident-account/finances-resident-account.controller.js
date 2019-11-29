(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('FinancesResidentController', FinancesResidentController);

    FinancesResidentController.$inject = ['AlertService', '$rootScope', 'Principal', '$state', '$localStorage', 'Balance', 'House', 'globalCompany'];

    function FinancesResidentController(AlertService, $rootScope, Principal, $state, $localStorage, Balance, House, globalCompany) {
        var vm = this;
        $rootScope.active = "financesResidentAccount";
        vm.isReady = false;

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;


        loadAll();




    }
})();
