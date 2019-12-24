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
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD','ROLE_USER'],
                },
                views: {
                    'content@': {
                         templateUrl: 'app/entities/mensual-anual-report/budgetExecutionAdministration.html',
                        //  templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'BudgetExecutionAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('resultStates', {
                parent: 'entity',
                url: '/resultStates',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD','ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensual-anual-report/resultStatesAdministration.html',
                        //  templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'ResultStatesAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('budgetExecution.mensualReport', {
                url: '/mensualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD','ROLE_USER'],
                },
                templateUrl: 'app/entities/mensual-anual-report/budgetExecutionMensualReport.html',
                //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                controller: 'BudgetExecutionMensualReportController',
                controllerAs: 'vm'

            })
            .state('budgetExecution.anualReport', {
                url: '/anualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD','ROLE_USER'],
                },
                templateUrl: 'app/entities/mensual-anual-report/budgetExecutionAnualReport.html',
                // templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                controller: 'BudgetExecutionAnualReportController',
                controllerAs: 'vm'

            })
            .state('budgetExecution.anualReport.graphic', {
                parent: 'budgetExecution.anualReport',
                url: '/grafica?year',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_USER', 'ROLE_JD'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                      templateUrl: 'app/entities/mensual-anual-report/anual-report-graphic.html',
                        //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'AnualReportGraphicController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    }).result.then(function () {
                        $state.go('budgetExecution.anualReport', null, {reload: false});
                    }, function () {
                        $state.go('budgetExecution.anualReport');
                    });
                }]
            })
            .state('resultStates.mensualReport', {
                url: '/mensualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD','ROLE_USER'],
                },
              templateUrl: 'app/entities/mensual-anual-report/resultStatesMensualReport.html',
                // templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                controller: 'ResultStatesMensualReportController',
                controllerAs: 'vm'

            })
            .state('resultStates.anualReport', {
                url: '/anualReport',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD','ROLE_USER'],
                },
                templateUrl: 'app/entities/mensual-anual-report/resultStatesAnualReport.html',
                //  templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                controller: 'ResultStatesAnualReportController',
                controllerAs: 'vm'

            })
            .state('resultStates.anualReport.graphic', {
                parent: 'resultStates.anualReport',
                url: '/grafica?year',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_JD','ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                       templateUrl: 'app/entities/mensual-anual-report/anual-report-graphic.html',
                        //     templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'AnualReportGraphicController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    }).result.then(function () {
                        $state.go('resultStates.anualReport', null, {reload: false});
                    }, function () {
                        $state.go('resultStates.anualReport');
                    });
                }]
            })


    }

})();
