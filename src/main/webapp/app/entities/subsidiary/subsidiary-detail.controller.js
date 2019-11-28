(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryDetailController', SubsidiaryDetailController);

    SubsidiaryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Subsidiary', 'SubsidiaryType', 'House'];

    function SubsidiaryDetailController($scope, $rootScope, $stateParams, previousState, entity, Subsidiary, SubsidiaryType, House) {
        var vm = this;

        vm.subsidiary = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:subsidiaryUpdate', function(event, result) {
            vm.subsidiary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
