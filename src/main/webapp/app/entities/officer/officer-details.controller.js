(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDetailsController', OfficerDetailsController);

    OfficerDetailsController.$inject = ['$rootScope','$state','Principal','$timeout', 'CommonMethods','$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company'];

    function OfficerDetailsController ($rootScope,$state, Principal, $timeout, CommonMethods, $scope, $stateParams, $q, DataUtils, entity, Officer, User, Company) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.officer = entity;
        $rootScope.active = "officers";
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        $("#officerInformation").fadeIn(300);
        vm.title = vm.officer.name + " " + vm.officer.lastname + " " + vm.officer.secondlastname;
        var unsubscribe = $rootScope.$on('aditumApp:officerUpdate', function(event, result) {
            vm.officer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
