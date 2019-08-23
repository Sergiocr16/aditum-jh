(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDetailController', RegulationDetailController);

    RegulationDetailController.$inject = ['AlertService','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Regulation', 'Company'];

    function RegulationDetailController(AlertService,$scope, $rootScope, $stateParams, previousState, entity, Regulation, Company) {
        var vm = this;
        vm.isReady = false;
        vm.regulation = entity;
        vm.previousState = previousState.name;
        $rootScope.active = "regulation";
        var unsubscribe = $rootScope.$on('aditumApp:regulationUpdate', function(event, result) {
            vm.regulation = result;
        });
        if(vm.regulation.companyId!=null){
            Company.get({id: parseInt(vm.regulation.companyId)}, function (company) {
                vm.regulation.company = company;
                vm.isReady = true;
            });
        }else{
            vm.isReady = true;
        }



        $scope.$on('$destroy', unsubscribe);
    }
})();
