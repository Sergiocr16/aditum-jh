(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('procedure-comments', {
            parent: 'entity',
            url: '/procedure-comments?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.procedureComments.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-comments/procedure-comments.html',
                    controller: 'ProcedureCommentsController',
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
                    $translatePartialLoader.addPart('procedureComments');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('procedure-comments-detail', {
            parent: 'procedure-comments',
            url: '/procedure-comments/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.procedureComments.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedure-comments/procedure-comments-detail.html',
                    controller: 'ProcedureCommentsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('procedureComments');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProcedureComments', function($stateParams, ProcedureComments) {
                    return ProcedureComments.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'procedure-comments',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('procedure-comments-detail.edit', {
            parent: 'procedure-comments-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-comments/procedure-comments-dialog.html',
                    controller: 'ProcedureCommentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureComments', function(ProcedureComments) {
                            return ProcedureComments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-comments.new', {
            parent: 'procedure-comments',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-comments/procedure-comments-dialog.html',
                    controller: 'ProcedureCommentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                comment: null,
                                deleted: null,
                                creationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('procedure-comments', null, { reload: 'procedure-comments' });
                }, function() {
                    $state.go('procedure-comments');
                });
            }]
        })
        .state('procedure-comments.edit', {
            parent: 'procedure-comments',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-comments/procedure-comments-dialog.html',
                    controller: 'ProcedureCommentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProcedureComments', function(ProcedureComments) {
                            return ProcedureComments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-comments', null, { reload: 'procedure-comments' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedure-comments.delete', {
            parent: 'procedure-comments',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedure-comments/procedure-comments-delete-dialog.html',
                    controller: 'ProcedureCommentsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProcedureComments', function(ProcedureComments) {
                            return ProcedureComments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedure-comments', null, { reload: 'procedure-comments' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
