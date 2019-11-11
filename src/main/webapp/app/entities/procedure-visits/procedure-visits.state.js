(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('procedure-visits', {
            parent: 'entity',
            url: '/procedure-visits?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.procedureVisits.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-visits/procedure-visits.html',
                    controller: 'ProcedureVisitsController',
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
                    $translatePartialLoader.addPart('procedureVisits');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('procedure-visits-detail', {
            parent: 'procedure-visits',
            url: '/procedure-visits/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.procedureVisits.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-visits/procedure-visits-detail.html',
                    controller: 'ProcedureVisitsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('procedureVisits');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProcedureVisits', function($stateParams, ProcedureVisits) {
                    return ProcedureVisits.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'procedure-visits',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('procedure-visits-detail.edit', {
            parent: 'procedure-visits-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visits/procedure-visits-dialog.html',
                    controller: 'ProcedureVisitsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureVisits', function(ProcedureVisits) {
                            return ProcedureVisits.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-visits.new', {
            parent: 'procedure-visits',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visits/procedure-visits-dialog.html',
                    controller: 'ProcedureVisitsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                visitDate: null,
                                duration: null,
                                puntuation: null,
                                deleted: null,
                                isDone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('procedure-visits', null, { reload: 'procedure-visits' });
                }, function() {
                    $state.go('procedure-visits');
                });
            }]
        })
        .state('procedure-visits.edit', {
            parent: 'procedure-visits',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visits/procedure-visits-dialog.html',
                    controller: 'ProcedureVisitsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureVisits', function(ProcedureVisits) {
                            return ProcedureVisits.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-visits', null, { reload: 'procedure-visits' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-visits.delete', {
            parent: 'procedure-visits',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-visits/procedure-visits-delete-dialog.html',
                    controller: 'ProcedureVisitsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProcedureVisits', function(ProcedureVisits) {
                            return ProcedureVisits.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-visits', null, { reload: 'procedure-visits' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
