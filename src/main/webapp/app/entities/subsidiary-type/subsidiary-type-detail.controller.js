(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryTypeDetailController', SubsidiaryTypeDetailController);

    SubsidiaryTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SubsidiaryType', 'SubsidiaryCategory', 'Company'];

    function SubsidiaryTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, SubsidiaryType, SubsidiaryCategory, Company) {
        var vm = this;

        vm.subsidiaryType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:subsidiaryTypeUpdate', function(event, result) {
            vm.subsidiaryType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
