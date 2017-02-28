(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoDetailController', AdminInfoDetailController);

    AdminInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'AdminInfo', 'User', 'Company'];

    function AdminInfoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, AdminInfo, User, Company) {
        var vm = this;

        vm.adminInfo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('aditumApp:adminInfoUpdate', function(event, result) {
            vm.adminInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
