(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDetailController', VisitantDetailController);

    VisitantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Visitant', 'House', 'Company','Principal'];

    function VisitantDetailController($scope, $rootScope, $stateParams, previousState, entity, Visitant, House, Company, Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.visitant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:visitantUpdate', function(event, result) {
            vm.visitant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
