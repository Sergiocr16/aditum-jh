(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDetailController', VisitantDetailController);

    VisitantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Visitant', 'House', 'Company'];

    function VisitantDetailController($scope, $rootScope, $stateParams, previousState, entity, Visitant, House, Company) {
        var vm = this;

        vm.visitant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:visitantUpdate', function(event, result) {
            vm.visitant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
