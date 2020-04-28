(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminARDetailController', AdminARDetailController);

    AdminARDetailController.$inject = ['Principal','$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'AdminInfo', 'User', 'Company'];

    function AdminARDetailController(Principal,$scope, $rootScope, $stateParams, previousState, DataUtils, entity, AdminInfo, User, Company) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.adminInfo = entity;
        vm.isReady = false;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        $("#adminInformation").fadeIn(300);


        Company.get({id:parseInt(vm.adminInfo.companyId)},onSuccess)
        function onSuccess (data){
            vm.company = data;

        }
        vm.isReady = true;

        var unsubscribe = $rootScope.$on('aditumApp:adminInfoUpdate', function(event, result) {
            vm.adminInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
