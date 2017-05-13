(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoDetailController', AdminInfoDetailController);

    AdminInfoDetailController.$inject = ['Principal','$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'AdminInfo', 'User', 'Company'];

    function AdminInfoDetailController(Principal,$scope, $rootScope, $stateParams, previousState, DataUtils, entity, AdminInfo, User, Company) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.adminInfo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        $("#adminInformation").fadeIn(300);


        Company.get({id:vm.adminInfo.companyId},onSuccess)
        function onSuccess (data){
            vm.company = data;
        }

        vm.title = 'Administrador '+vm.adminInfo.name + " " + vm.adminInfo.lastname + " " + vm.adminInfo.secondlastname;

        var unsubscribe = $rootScope.$on('aditumApp:adminInfoUpdate', function(event, result) {
            vm.adminInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
