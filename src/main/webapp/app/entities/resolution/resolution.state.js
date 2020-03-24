(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resolution', {
            parent: 'entity',
            url: '/resolution?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER'],
                pageTitle: 'aditumApp.resolution.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resolution/resolutions.html',
                    controller: 'ResolutionController',
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
                    $translatePartialLoader.addPart('resolution');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resolution-detail', {
            parent: 'resolution',
            url: '/resolution/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER'],
                pageTitle: 'aditumApp.resolution.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resolution/resolution-detail.html',
                    controller: 'ResolutionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resolution');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Resolution', function($stateParams, Resolution) {
                    return Resolution.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resolution',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resolution-detail.edit', {
            parent: 'resolution-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution/resolution-dialog.html',
                    controller: 'ResolutionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resolution', function(Resolution) {
                            return Resolution.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resolution.new', {
            parent: 'resolution',
            url: '/new',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution/resolution-dialog.html',
                    controller: 'ResolutionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                problem: null,
                                solution: null,
                                stated: null,
                                deleted: null,
                                solvedTimes: null,
                                creationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resolution', null, { reload: 'resolution' });
                }, function() {
                    $state.go('resolution');
                });
            }]
        })
        .state('resolution.edit', {
            parent: 'resolution',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution/resolution-dialog.html',
                    controller: 'ResolutionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resolution', function(Resolution) {
                            return Resolution.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resolution', null, { reload: 'resolution' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resolution.delete', {
            parent: 'resolution',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resolution/resolution-delete-dialog.html',
                    controller: 'ResolutionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resolution', function(Resolution) {
                            return Resolution.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resolution', null, { reload: 'resolution' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
