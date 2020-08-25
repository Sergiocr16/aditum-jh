(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MinutesDetailController', MinutesDetailController);

    MinutesDetailController.$inject = ['Modal', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CondominiumRecord', 'Company'];

    function MinutesDetailController(Modal, $scope, $rootScope, $stateParams, previousState, entity, CondominiumRecord, Company) {
        var vm = this;

        vm.condominiumRecord = entity;
        vm.previousState = previousState.name;
        $rootScope.active = "records";
        vm.isReady = true;

        vm.textDocumentName = $rootScope.adminCompany.id==1?"de la minuta":"del documento";
        vm.textDocumentName2 = $rootScope.adminCompany.id==1?"minuta":"documento";

        var unsubscribe = $rootScope.$on('aditumApp:condominiumRecordUpdate', function (event, result) {
            vm.condominiumRecord = result;
        });
        $rootScope.mainTitle = "Detalle de la minuta";
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
