(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressCategoryDetailController', EgressCategoryDetailController);

    EgressCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EgressCategory', 'Company'];

    function EgressCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, EgressCategory, Company) {
        var vm = this;

        vm.egressCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:egressCategoryUpdate', function(event, result) {
            vm.egressCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
