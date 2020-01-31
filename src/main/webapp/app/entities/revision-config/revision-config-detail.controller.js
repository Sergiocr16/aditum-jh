(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigDetailController', RevisionConfigDetailController);

    RevisionConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RevisionConfig', 'Company'];

    function RevisionConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, RevisionConfig, Company) {
        var vm = this;

        vm.revisionConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:revisionConfigUpdate', function(event, result) {
            vm.revisionConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
