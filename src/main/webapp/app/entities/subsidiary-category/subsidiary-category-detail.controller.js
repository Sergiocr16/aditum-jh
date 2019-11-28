(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryCategoryDetailController', SubsidiaryCategoryDetailController);

    SubsidiaryCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SubsidiaryCategory', 'Company'];

    function SubsidiaryCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, SubsidiaryCategory, Company) {
        var vm = this;

        vm.subsidiaryCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:subsidiaryCategoryUpdate', function(event, result) {
            vm.subsidiaryCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
