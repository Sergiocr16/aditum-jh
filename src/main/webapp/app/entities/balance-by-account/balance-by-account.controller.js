(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceByAccountController', BalanceByAccountController);

    BalanceByAccountController.$inject = ['BalanceByAccount','globalCompany','Banco','Modal','$rootScope'];

    function BalanceByAccountController(BalanceByAccount,globalCompany,Banco,Modal,$rootScope) {

        var vm = this;
        vm.year = moment(new Date()).format("YYYY")
        vm.balanceByAccounts = [];

        $rootScope.mainTitle = "Saldo inicial";
        $rootScope.active = "initialBalance";
        loadBancos();

        function loadBancos() {

            Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);

            function onSuccessBancos(data, headers) {
                vm.bancos = data;
                vm.bancoSelected = vm.bancos[0];

                createBudgetYears();

            }

        }


        function createBudgetYears(){
            vm.budgetYearsToSelect = []
            var actualYear = parseInt(moment(new Date()).format('YYYY'));
            for(var i=0;i<4;i++){
                vm.budgetYearsToSelect.push({year:actualYear+i});
            }

            loadBalanceByYear(vm.year,vm.bancoSelected);

        }

        function loadBalanceByYear(year,account) {
            var initialTime = new Date().setFullYear(year,0,1)
            var finalTime = new Date().setFullYear(year,11,31)
            console.log( moment(initialTime).format())
            console.log(moment(finalTime).format())
            BalanceByAccount.findBetweenDatesByAccountToSet({
                initial_time: moment(initialTime).format(),
                final_time: moment(finalTime).format(),
                accountId: account.id}).$promise.then(onSuccessBalance);
        }

        function onSuccessBalance(data) {
            vm.isReady = true;
            vm.initialBalances = data;
            console.log(data)
        }

        vm.changeBanco = function(banco){
            vm.isReady = false;
            vm.bancoSelected = banco;
            loadBalanceByYear(vm.year,vm.bancoSelected);
        }

        vm.nextYear = function () {
            return parseInt(vm.year) + 1;
        };
        vm.backYear = function () {
            return parseInt(vm.year) - 1;
        };

        vm.showNextYear = function () {
            vm.isReady = false;
            vm.year = parseInt(vm.year) + 1;
            loadBalanceByYear(vm.year,vm.bancoSelected);

        };
        vm.showBackYear = function () {
            vm.isReady = false;
            vm.year = parseInt(vm.year) - 1;
            loadBalanceByYear(vm.year,vm.bancoSelected);
        }

        vm.saveBalance = function (initialBalance) {
            Modal.confirmDialog("¿Está seguro que desea editar este saldo inicial?", "",function () {

                if (initialBalance.id !== null) {
                    BalanceByAccount.update(initialBalance, onSaveSuccess, onSaveError);
                } else {
                   BalanceByAccount.save(initialBalance, onSaveSuccess, onSaveError);
                }
            })

        }
        function onSaveSuccess (result) {
            Modal.toast("Guardado.");
            vm.isSaving = false;
        }


        function onSaveError () {
            Modal.toast("Un error inesperado ocurrió.")
        }

    }
})();
