(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SecurityCompanyDetailController', SecurityCompanyDetailController);

    SecurityCompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SecurityCompany'];

    function SecurityCompanyDetailController($scope, $rootScope, $stateParams, previousState, entity, SecurityCompany) {
        var vm = this;

        vm.securityCompany = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:securityCompanyUpdate', function(event, result) {
            vm.securityCompany = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
