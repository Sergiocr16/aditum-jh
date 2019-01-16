(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
           .state('finanzasPorCasa', {
                    parent: 'entity',
                    url: '/finanzasPorCasa',
                    data: {
                        authorities: ['ROLE_ADMIN','ROLE_USER'],
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/finances-resident-account/finances-resident-account.html',
                            controller: 'FinancesResidentController',
                            controllerAs: 'vm'
                        }
                    }
                })
               .state('finanzasPorCasa.mensualReport', {
                        url: '/mensualReport',
                        data: {
                            authorities: ['ROLE_ADMIN','ROLE_USER'],
                        },
                        templateUrl: 'app/entities/mensualAndAnualReport/mensualReport.html',
                        controller: 'MensualReportController',
                        controllerAs: 'vm'

                    })
              .state('finanzasPorCasa.anualReport', {
                        url: '/anualReport',
                        data: {
                            authorities: ['ROLE_ADMIN','ROLE_USER'],
                        },
                        templateUrl: 'app/entities/mensualAndAnualReport/anualReport.html',
                        controller: 'AnualReportController',
                        controllerAs: 'vm'

                    })
                     .state('finanzasPorCasa.accountStatus', {
                                url: '/estadoDeCuenta',
                                data: {
                                    authorities: ['ROLE_ADMIN', 'ROLE_USER'],
                                },
                                templateUrl:'app/entities/finances-resident-account/accountStatus-resident-account.html',
                                 controller: 'AccountStatusResidentAccountController',
                                 controllerAs: 'vm',
                                params: {
                                    page: {
                                        value: '1',
                                        squash: true
                                    },
                                    sort: {
                                        value: 'id,asc',
                                        squash: true
                                    },
                                    search: null
                                },
                                resolve: {
                                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                                        return {
                                            page: PaginationUtil.parsePage($stateParams.page),
                                            sort: $stateParams.sort,
                                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                                            search: $stateParams.search
                                        };
                                    }],
                                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                                        $translatePartialLoader.addPart('house');
                                        $translatePartialLoader.addPart('global');
                                        return $translate.refresh();
                                    }]
                                }
                            })

      .state('finanzasPorCasa.chargePerHouse', {
            url: '/cuotas',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            },
                        templateUrl: 'app/entities/finances-resident-account/charges-resident-account.html',
             controller: 'ChargePerHouseController',
             controllerAs: 'vm',
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('house');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
              .state('finanzasPorCasa.paymentsPerHouse', {
                              url: '/pagos?page&sort&search',
                              data: {
                                  authorities: ['ROLE_ADMIN', 'ROLE_USER'],
                              },
                              templateUrl:'app/entities/finances-resident-account/payments-resident-account.html',
                               controller: 'PaymentsPerHouseController',
                               controllerAs: 'vm',
                     params: {
                         page: {
                             value: '1',
                             squash: true
                         },
                         sort: {
                             value: 'id,asc',
                             squash: true
                         },
                         search: null
                     },
                     resolve: {
                         pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                             return {
                                 page: PaginationUtil.parsePage($stateParams.page),
                                 sort: $stateParams.sort,
                                 predicate: PaginationUtil.parsePredicate($stateParams.sort),
                                 ascending: PaginationUtil.parseAscending($stateParams.sort),
                                 search: $stateParams.search
                             };
                         }],
                         translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                             $translatePartialLoader.addPart('payment');
                             $translatePartialLoader.addPart('global');
                             return $translate.refresh();
                         }]
                     }
                          })






    }

})();
