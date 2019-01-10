(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('budgetExecution', {
            parent: 'entity',
            url: '/budgetExecution',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_JD'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mensualAndAnualReport/budgetExecution.html',
                    controller: 'BudgetExecutionController',
                    controllerAs: 'vm'
                }
            }
        })
      .state('budgetExecution.mensualReport', {
            url: '/mensualReport',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_JD'],
            },
            templateUrl: 'app/entities/mensualAndAnualReport/mensualReport.html',
            controller: 'MensualReportController',
            controllerAs: 'vm'

        })
       .state('budgetExecution.anualReport', {
                url: '/anualReport',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_JD'],
                },
                templateUrl: 'app/entities/mensualAndAnualReport/anualReport.html',
                controller: 'AnualReportController',
                controllerAs: 'vm'

            })



    }

})();
