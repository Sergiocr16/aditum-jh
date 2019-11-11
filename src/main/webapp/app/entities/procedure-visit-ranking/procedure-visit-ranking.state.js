(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('procedure-visit-ranking', {
            parent: 'entity',
            url: '/procedure-visit-ranking?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.procedureVisitRanking.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-visit-ranking/procedure-visit-rankings.html',
                    controller: 'ProcedureVisitRankingController',
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
                    $translatePartialLoader.addPart('procedureVisitRanking');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('procedure-visit-ranking-detail', {
            parent: 'procedure-visit-ranking',
            url: '/procedure-visit-ranking/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.procedureVisitRanking.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-visit-ranking/procedure-visit-ranking-detail.html',
                    controller: 'ProcedureVisitRankingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('procedureVisitRanking');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProcedureVisitRanking', function($stateParams, ProcedureVisitRanking) {
                    return ProcedureVisitRanking.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'procedure-visit-ranking',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('procedure-visit-ranking-detail.edit', {
            parent: 'procedure-visit-ranking-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visit-ranking/procedure-visit-ranking-dialog.html',
                    controller: 'ProcedureVisitRankingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureVisitRanking', function(ProcedureVisitRanking) {
                            return ProcedureVisitRanking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-visit-ranking.new', {
            parent: 'procedure-visit-ranking',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visit-ranking/procedure-visit-ranking-dialog.html',
                    controller: 'ProcedureVisitRankingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                question: null,
                                puntuation: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('procedure-visit-ranking', null, { reload: 'procedure-visit-ranking' });
                }, function() {
                    $state.go('procedure-visit-ranking');
                });
            }]
        })
        .state('procedure-visit-ranking.edit', {
            parent: 'procedure-visit-ranking',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visit-ranking/procedure-visit-ranking-dialog.html',
                    controller: 'ProcedureVisitRankingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureVisitRanking', function(ProcedureVisitRanking) {
                            return ProcedureVisitRanking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-visit-ranking', null, { reload: 'procedure-visit-ranking' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-visit-ranking.delete', {
            parent: 'procedure-visit-ranking',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visit-ranking/procedure-visit-ranking-delete-dialog.html',
                    controller: 'ProcedureVisitRankingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProcedureVisitRanking', function(ProcedureVisitRanking) {
                            return ProcedureVisitRanking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-visit-ranking', null, { reload: 'procedure-visit-ranking' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
