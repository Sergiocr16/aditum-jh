(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resolution-comments', {
            parent: 'entity',
            url: '/resolution-comments?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.resolutionComments.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resolution-comments/resolution-comments.html',
                    controller: 'ResolutionCommentsController',
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
                    $translatePartialLoader.addPart('resolutionComments');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resolution-comments-detail', {
            parent: 'resolution-comments',
            url: '/resolution-comments/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.resolutionComments.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resolution-comments/resolution-comments-detail.html',
                    controller: 'ResolutionCommentsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resolutionComments');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ResolutionComments', function($stateParams, ResolutionComments) {
                    return ResolutionComments.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resolution-comments',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resolution-comments-detail.edit', {
            parent: 'resolution-comments-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution-comments/resolution-comments-dialog.html',
                    controller: 'ResolutionCommentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResolutionComments', function(ResolutionComments) {
                            return ResolutionComments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resolution-comments.new', {
            parent: 'resolution-comments',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution-comments/resolution-comments-dialog.html',
                    controller: 'ResolutionCommentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                creationDate: null,
                                editedDate: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resolution-comments', null, { reload: 'resolution-comments' });
                }, function() {
                    $state.go('resolution-comments');
                });
            }]
        })
        .state('resolution-comments.edit', {
            parent: 'resolution-comments',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution-comments/resolution-comments-dialog.html',
                    controller: 'ResolutionCommentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResolutionComments', function(ResolutionComments) {
                            return ResolutionComments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resolution-comments', null, { reload: 'resolution-comments' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resolution-comments.delete', {
            parent: 'resolution-comments',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution-comments/resolution-comments-delete-dialog.html',
                    controller: 'ResolutionCommentsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ResolutionComments', function(ResolutionComments) {
                            return ResolutionComments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resolution-comments', null, { reload: 'resolution-comments' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
