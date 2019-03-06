(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DocumentFileDetailController', DocumentFileDetailController);

    DocumentFileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DocumentFile', 'Company'];

    function DocumentFileDetailController($scope, $rootScope, $stateParams, previousState, entity, DocumentFile, Company) {
        var vm = this;

        vm.documentFile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:documentFileUpdate', function(event, result) {
            vm.documentFile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
