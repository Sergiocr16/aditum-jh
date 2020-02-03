(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CondominiumRecordDetailController', CondominiumRecordDetailController);

    CondominiumRecordDetailController.$inject = ['Modal', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CondominiumRecord', 'Company'];

    function CondominiumRecordDetailController(Modal, $scope, $rootScope, $stateParams, previousState, entity, CondominiumRecord, Company) {
        var vm = this;

        vm.condominiumRecord = entity;
        vm.previousState = previousState.name;
        $rootScope.active = "records";
        vm.isReady = true;
        var unsubscribe = $rootScope.$on('aditumApp:condominiumRecordUpdate', function (event, result) {
            vm.condominiumRecord = result;
        });
        $rootScope.mainTitle = "Detalle del acta";
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
