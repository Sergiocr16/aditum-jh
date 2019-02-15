(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BudgetExecutionAdministrationController', BudgetExecutionAdministrationController);

    BudgetExecutionAdministrationController.$inject = ['AlertService','$rootScope','Principal','MensualAndAnualReport','$state'];

    function BudgetExecutionAdministrationController(AlertService,$rootScope,Principal,MensualAndAnualReport,$state) {
      $rootScope.active = "budgetExecution";
        var vm = this;

    }
})();
