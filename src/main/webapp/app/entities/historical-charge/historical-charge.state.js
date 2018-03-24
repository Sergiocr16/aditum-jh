(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('historical-charge', {
            parent: 'entity',
            url: '/historical-charge?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalCharge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-charge/historical-charges.html',
                    controller: 'HistoricalChargeController',
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
                    $translatePartialLoader.addPart('historicalCharge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('historical-charge-detail', {
            parent: 'historical-charge',
            url: '/historical-charge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalCharge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-charge/historical-charge-detail.html',
                    controller: 'HistoricalChargeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalCharge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HistoricalCharge', function($stateParams, HistoricalCharge) {
                    return HistoricalCharge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'historical-charge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('historical-charge-detail.edit', {
            parent: 'historical-charge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-charge/historical-charge-dialog.html',
                    controller: 'HistoricalChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalCharge', function(HistoricalCharge) {
                            return HistoricalCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-charge.new', {
            parent: 'historical-charge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-charge/historical-charge-dialog.html',
                    controller: 'HistoricalChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                concept: null,
                                ammount: null,
                                abono: null,
                                type: null,
                                state: null,
                                deleted: null,
                                total: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('historical-charge', null, { reload: 'historical-charge' });
                }, function() {
                    $state.go('historical-charge');
                });
            }]
        })
        .state('historical-charge.edit', {
            parent: 'historical-charge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-charge/historical-charge-dialog.html',
                    controller: 'HistoricalChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalCharge', function(HistoricalCharge) {
                            return HistoricalCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-charge', null, { reload: 'historical-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-charge.delete', {
            parent: 'historical-charge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-charge/historical-charge-delete-dialog.html',
                    controller: 'HistoricalChargeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HistoricalCharge', function(HistoricalCharge) {
                            return HistoricalCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-charge', null, { reload: 'historical-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
