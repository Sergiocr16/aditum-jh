(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('FinancesMobileMenuController', FinancesMobileMenuController);

        FinancesMobileMenuController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope', 'CommonMethods', 'globalCompany'];

        function FinancesMobileMenuController(timeout, $scope, $stateParams, $rootScope, CommonMethods, globalCompany) {
            var vm = this;
            $rootScope.active = "finances-mobile-menu";
            $rootScope.mainTitle = "Finanzas";
            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
            vm.showEstadoResultados = companyConfig.showEstadoResultados;
            vm.menu = [
                {
                    title: "Mi estado de cuenta",
                    icon: "account_balance_wallet",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "accountStatus-residentAccount",
                    show: true,
                },
                {
                    title: "Mis deudas",
                    icon: "request_quote",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "chargePerHouse-residentAccount",
                    show: true,
                },
                {
                    title: "Mis Pagos",
                    icon: "payments",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "paymentsPerHouse-residentAccount",
                    show: true,
                },
                {
                    title: "Comprobantes de pago",
                    icon: "credit_score",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "paymentProof.pending-user",
                    show: true,
                },
                {
                    title: "Estados financieros",
                    icon: "auto_stories",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "mensual-billing-file",
                    show: vm.showEstadoResultados,
                },
            ]
        }
    }

)();
