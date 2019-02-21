(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('chargesReport', {
                parent: 'entity',
                url: '/reporte-cuotas-cobrar',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/charges-to-pay-report.html',
                        controller: 'ChargesToPayReportController',
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
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('chargesReport.graphic', {
                parent: 'chargesReport',
                url: '/grafica',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/charges-to-pay-grapich.html',
                        controller: 'ChargesToPayGraphicController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    }).result.then(function () {
                        $state.go('chargesReport', null, {reload: false});
                    }, function () {
                        $state.go('chargesReport');
                    });
                }]
            })
            .state('chargeMenu', {
                parent: 'entity',
                url: '/crear-cuotas',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/charge-menu.html',
                        controller: 'ChargeMenuController',
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
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mensualCharge', {
                parent: 'entity',
                url: '/crear/cuota/mantenimiento',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/mensual-charge.html',
                        controller: 'MensualChargeController',
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
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('individualCharge', {
                parent: 'entity',
                url: '/crear/cuota/individual',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/individual-charge.html',
                        controller: 'IndividualChargeController',
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
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('extraordinaryCharge', {
                parent: 'entity',
                url: '/crear/cuota/extraordinaria',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/extraordinary-charge.html',
                        controller: 'ExtraordinaryChargeController',
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
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('configureCharges', {
                parent: 'entity',
                url: '/configurar/cuotas',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/configure-charges.html',
                        controller: 'ConfigureChargesController',
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
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('configureCharges.global', {
                parent: 'configureCharges',
                url: '/global',
                data: {
                    authorities: ['ROLE_MANAGER']
                },

                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/configure-charge-global.html',
                        controller: 'ConfigureChargeGlobalController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                    }).result.then(function () {
                        $state.go('^', {}, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
