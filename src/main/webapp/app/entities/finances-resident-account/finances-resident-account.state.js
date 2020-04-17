(function () {
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
                    authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
                },
                views: {
                    'content@': {
                       templateUrl: 'app/entities/finances-resident-account/finances-resident-account.html',
                        //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'FinancesResidentController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('finanzasPorCasa.mensualReport', {
                url: '/mensual-report',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
                },
                templateUrl: 'app/entities/mensualAndAnualReport/resultStatesMensualReport.html',
                //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                controller: 'MensualReportController',
                controllerAs: 'vm'

            })
            .state('finanzasPorCasa.anualReport', {
                url: '/anual-report',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
                },
               templateUrl: 'app/entities/mensualAndAnualReport/resultStatesAnualReport.html',
                //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                controller: 'AnualReportController',
                controllerAs: 'vm'

            })
            .state('accountStatus-residentAccount', {
                parent: 'entity',
                url: '/account-status-subsidiary',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
                },
                views: {
                    'content@': {
                       templateUrl: 'app/entities/finances-resident-account/accountStatus-resident-account.html',
                        //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'AccountStatusResidentAccountController',
                        controllerAs: 'vm'
                    }
                },
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('waterChargePerHouse-residentAccount', {
                parent: 'entity',
                url: '/subsidiary-water-debts',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/finances-resident-account/water-charge-resident-account.html',
                        //    templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'WaterChargePerHouseController',
                        controllerAs: 'vm'
                    }
                },
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('paymentsPerHouse-residentAccount', {
                parent: 'entity',
                url: '/payments?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
                },
                views: {
                    'content@': {
                       templateUrl: 'app/entities/finances-resident-account/payments-resident-account.html',
                        //    templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'PaymentsPerHouseController',
                        controllerAs: 'vm'
                    }
                },
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
            .state('waterPaymentsPerHouse-residentAccount', {
            parent: 'entity',
            url: '/water-payments?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_USER','ROLE_OWNER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/finances-resident-account/water-payments-resident-account.html',
                    //    templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'WaterPaymentsPerHouseController',
                    controllerAs: 'vm'
                }
            },
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
