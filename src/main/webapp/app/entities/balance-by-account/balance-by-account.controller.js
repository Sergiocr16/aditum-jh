(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceByAccountController', BalanceByAccountController);

    BalanceByAccountController.$inject = ['BalanceByAccount'];

    function BalanceByAccountController(BalanceByAccount) {

        var vm = this;

        vm.balanceByAccounts = [];

        loadAll();

        function loadAll() {
            BalanceByAccount.query(function(result) {
                vm.balanceByAccounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
