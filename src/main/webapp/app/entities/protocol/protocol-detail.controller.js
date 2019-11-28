(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProtocolDetailController', ProtocolDetailController);

    ProtocolDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Protocol', 'Company'];

    function ProtocolDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Protocol, Company) {
        var vm = this;

        vm.protocol = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('aditumApp:protocolUpdate', function(event, result) {
            vm.protocol = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
