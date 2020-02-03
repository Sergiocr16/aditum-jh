(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyDetailController', EmergencyDetailController);

    EmergencyDetailController.$inject = ['Modal', 'AlertService', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Regulation', 'Company'];

    function EmergencyDetailController(Modal, AlertService, $scope, $rootScope, $stateParams, previousState, entity, Regulation, Company) {
        var vm = this;
        vm.isReady = true;

        vm.emergency = entity;
        console.log(entity)

        vm.previousState = previousState.name;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $rootScope.active = "emergency";


    }
})();
