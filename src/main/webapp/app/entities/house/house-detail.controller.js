(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDetailController', HouseDetailController);

    HouseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CommonMethods', '$state', 'Modal'];

    function HouseDetailController($scope, $rootScope, $stateParams, entity, CommonMethods, $state, Modal) {
        var vm = this;

        vm.house = entity;
        $rootScope.mainTitle = 'Filial ' + vm.house.housenumber;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        vm.editHouse = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('houses-tabs.edit', {
                id: encryptedId
            })
        }
    }
})();
