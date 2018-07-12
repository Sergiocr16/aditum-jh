(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BudgetExecutionController', BudgetExecutionController);

    BudgetExecutionController.$inject = ['AlertService','$rootScope','Principal','MensualAndAnualReport','$state'];

    function BudgetExecutionController(AlertService,$rootScope,Principal,MensualAndAnualReport,$state) {
      $rootScope.active = "estadoResultados";
        var vm = this;

    }
})();
