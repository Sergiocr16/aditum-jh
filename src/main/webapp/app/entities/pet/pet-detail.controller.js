(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PetDetailController', PetDetailController);

    PetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pet', 'House', 'Company', 'Modal'];

    function PetDetailController($scope, $rootScope, $stateParams, previousState, entity, Pet, House, Company, Modal) {
        var vm = this;

        vm.pet = entity;
        vm.previousState = previousState.name;
        $rootScope.mainTitle = "Detalle de mascota";
        var unsubscribe = $rootScope.$on('aditumApp:petUpdate', function (event, result) {
            vm.pet = result;
        });
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        vm.isReady = true;
        $scope.$on('$destroy', unsubscribe);
    }
})();
