(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteDetailController', SoporteDetailController);

    SoporteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Soporte', 'House', 'Company'];

    function SoporteDetailController($scope, $rootScope, $stateParams, previousState, entity, Soporte, House, Company) {
        var vm = this;
        vm.isReady = true;
        vm.soporte = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:soporteUpdate', function(event, result) {
            vm.soporte = result;
        });
        $scope.$on('$destroy', unsubscribe);

        Company.get({id:parseInt(vm.soporte.companyId)},onSuccess);
        function onSuccess (data){
            vm.company = data;
        if(vm.soporte.houseId!=null){
            House.get({id:parseInt(vm.soporte.houseId)},function (data) {
                vm.house = data;
            });
        }

        }
    }
})();
