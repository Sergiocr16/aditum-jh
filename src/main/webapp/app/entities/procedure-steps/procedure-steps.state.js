(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('procedure-steps', {
            parent: 'entity',
            url: '/procedure-steps?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.procedureSteps.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-steps/procedure-steps.html',
                    controller: 'ProcedureStepsController',
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
                    $translatePartialLoader.addPart('procedureSteps');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('procedure-steps-detail', {
            parent: 'procedure-steps',
            url: '/procedure-steps/{id}',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.procedureSteps.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-steps/procedure-steps-detail.html',
                    controller: 'ProcedureStepsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('procedureSteps');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProcedureSteps', function($stateParams, ProcedureSteps) {
                    return ProcedureSteps.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'procedure-steps',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('procedure-steps-detail.edit', {
            parent: 'procedure-steps-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-steps/procedure-steps-dialog.html',
                    controller: 'ProcedureStepsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureSteps', function(ProcedureSteps) {
                            return ProcedureSteps.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-steps.new', {
            parent: 'procedure-steps',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-steps/procedure-steps-dialog.html',
                    controller: 'ProcedureStepsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                number: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('procedure-steps', null, { reload: 'procedure-steps' });
                }, function() {
                    $state.go('procedure-steps');
                });
            }]
        })
        .state('procedure-steps.edit', {
            parent: 'procedure-steps',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-steps/procedure-steps-dialog.html',
                    controller: 'ProcedureStepsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureSteps', function(ProcedureSteps) {
                            return ProcedureSteps.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-steps', null, { reload: 'procedure-steps' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-steps.delete', {
            parent: 'procedure-steps',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-steps/procedure-steps-delete-dialog.html',
                    controller: 'ProcedureStepsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProcedureSteps', function(ProcedureSteps) {
                            return ProcedureSteps.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-steps', null, { reload: 'procedure-steps' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
