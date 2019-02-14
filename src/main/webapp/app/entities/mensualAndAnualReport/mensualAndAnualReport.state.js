(function () {
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
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensualAndAnualReport/budgetExecutionAdministration.html',
                        controller: 'BudgetExecutionAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('resultStates', {
                parent: 'entity',
                url: '/resultStates',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensualAndAnualReport/resultStatesAdministration.html',
                        controller: 'ResultStatesAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('budgetExecution.mensualReport', {
                url: '/mensualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                },
                templateUrl: 'app/entities/mensualAndAnualReport/budgetExecutionMensualReport.html',
                controller: 'BudgetExecutionMensualReportController',
                controllerAs: 'vm'

            })
            .state('budgetExecution.anualReport', {
                url: '/anualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                },
                templateUrl: 'app/entities/mensualAndAnualReport/budgetExecutionAnualReport.html',
                controller: 'BudgetExecutionAnualReportController',
                controllerAs: 'vm'

            })
            .state('resultStates.mensualReport', {
                url: '/mensualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                },
                templateUrl: 'app/entities/mensualAndAnualReport/resultStatesMensualReport.html',
                controller: 'ResultStatesMensualReportController',
                controllerAs: 'vm'

            })
            .state('resultStates.anualReport', {
                url: '/anualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                },
                templateUrl: 'app/entities/mensualAndAnualReport/resultStatesAnualReport.html',
                controller: 'ResultStatesAnualReportController',
                controllerAs: 'vm'

            })


    }

})();
