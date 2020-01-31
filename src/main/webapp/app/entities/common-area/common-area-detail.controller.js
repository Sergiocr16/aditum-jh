(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDetailController', CommonAreaDetailController);

    CommonAreaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'CommonArea'];

    function CommonAreaDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, CommonArea) {
        var vm = this;

        vm.commonArea = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('aditumApp:commonAreaUpdate', function(event, result) {
            vm.commonArea = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
