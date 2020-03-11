(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceByAccountController', BalanceByAccountController);

    BalanceByAccountController.$inject = ['BalanceByAccount','globalCompany','Banco'];

    function BalanceByAccountController(BalanceByAccount,globalCompany,Banco) {

        var vm = this;

        vm.balanceByAccounts = [];

        loadBancos();

        function loadBancos() {

            Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);

            function onSuccessBancos(data, headers) {
                vm.bancos = data;
                BalanceByAccount.getByBanco
            }

        }



    }
})();
