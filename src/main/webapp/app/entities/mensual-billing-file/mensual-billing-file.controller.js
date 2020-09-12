(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualBillingFileController', MensualBillingFileController);

    MensualBillingFileController.$inject = ['$timeout', 'ExcelExport', '$scope', '$state', 'Collection', 'ParseLinks', 'AlertService', '$rootScope', 'globalCompany', 'Modal', 'CommonMethods'];

    function MensualBillingFileController($timeout, ExcelExport, $scope, $state, Collection, ParseLinks, AlertService, $rootScope, globalCompany, Modal, CommonMethods) {
        var vm = this;
        vm.isReady = false;
        $rootScope.mainTitle = "Estados financieros";
        $rootScope.active = "collectionTable";
        vm.companyId = globalCompany.getId();
        vm.year = moment(new Date()).format("YYYY")
        vm.month = undefined;
        vm.selectMonth = function (month) {
            vm.month = month
            $rootScope.monthArchives = {month: month, year: vm.year}
            $state.go("mensual-billing-file-detail");
        }

        vm.resetMonth = function () {
            vm.month = undefined;
        }
        vm.months = [
            {name: "Enero", number: 1},
            {name: "Febrero", number: 2},
            {name: "Marzo", number: 3},
            {name: "Abril", number: 4},
            {name: "Mayo", number: 5},
            {name: "Junio", number: 6},
            {name: "Julio", number: 7},
            {name: "Agosto", number: 8},
            {name: "Septiembre", number: 9},
            {name: "Octubre", number: 10},
            {name: "Noviembre", number: 11},
            {name: "Diciembre", number: 12},
        ]

        vm.nextYear = function () {
            return parseInt(vm.year) + 1;
        }
        vm.backYear = function () {
            return parseInt(vm.year) - 1;
        }

        vm.showNextYear = function () {
            vm.isReady = false;
            vm.year = parseInt(vm.year) + 1;
        }
        vm.showBackYear = function () {
            vm.isReady = false;
            vm.year = parseInt(vm.year) - 1;
        }
    }
})();
