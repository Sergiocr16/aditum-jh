(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivosFijosDetailController', ActivosFijosDetailController);

    ActivosFijosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ActivosFijos', 'Company'];

    function ActivosFijosDetailController($scope, $rootScope, $stateParams, previousState, entity, ActivosFijos, Company) {
        var vm = this;

        vm.activosFijos = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:activosFijosUpdate', function(event, result) {
            vm.activosFijos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
