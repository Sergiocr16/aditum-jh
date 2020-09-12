(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualBillingFileDetailController', MensualBillingFileDetailController);

    MensualBillingFileDetailController.$inject = ['globalCompany','$scope', '$rootScope', '$stateParams', 'previousState', 'MensualBillingFile', 'Company', '$state', 'Modal'];

    function MensualBillingFileDetailController(globalCompany,$scope, $rootScope, $stateParams, previousState, MensualBillingFile, Company, $state, Modal) {
        var vm = this;
        vm.previousState = previousState.name;
        var vm = this;
        vm.isReady = false;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $rootScope.active = "collectionTable";
        if ($rootScope.monthArchives == undefined) {
            $state.go("mensual-billing-file")
        }
        vm.month = $rootScope.monthArchives.month;
        vm.year = $rootScope.monthArchives.year;
        vm.files = [];
        $rootScope.mainTitle = vm.month.name + " " + vm.year;
        MensualBillingFile.findAll({
            companyId: globalCompany.getId(),
            month: vm.month.number,
            year: vm.year
        }, function (data) {
            vm.files = data;
            console.log(vm.files);
        })
        var unsubscribe = $rootScope.$on('aditumApp:mensualBillingFileUpdate', function (event, result) {
            vm.mensualBillingFile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
