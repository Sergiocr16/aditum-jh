(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('charge', {
                parent: 'entity',
                url: '/charge?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.charge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/charges.html',
                        controller: 'ChargeController',
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
            })
            .state('charge-detail', {
                parent: 'charge',
                url: '/charge/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.charge.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/charge/charge-detail.html',
                        controller: 'ChargeDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('charge');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Charge', function ($stateParams, Charge) {
                        return Charge.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'charge',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('charge-detail.edit', {
                parent: 'charge-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/charge-dialog.html',
                        controller: 'ChargeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Charge', function (Charge) {
                                return Charge.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('charge.new', {
                parent: 'charge',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/charge-dialog.html',
                        controller: 'ChargeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    date: null,
                                    concept: null,
                                    ammount: null,
                                    state: null,
                                    deleted: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('charge', null, {reload: 'charge'});
                    }, function () {
                        $state.go('charge');
                    });
                }]
            })
            .state('charge.edit', {
                parent: 'charge',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/charge-dialog.html',
                        controller: 'ChargeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Charge', function (Charge) {
                                return Charge.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('charge', null, {reload: 'charge'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('charge.delete', {
                parent: 'charge',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/charge-delete-dialog.html',
                        controller: 'ChargeDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Charge', function (Charge) {
                                return Charge.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('charge', null, {reload: 'charge'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
