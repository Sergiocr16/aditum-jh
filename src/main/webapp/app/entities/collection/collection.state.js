(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collection', {
            parent: 'entity',
            url: '/collection?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.collection.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection/collections.html',
                    controller: 'CollectionController',
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
                    $translatePartialLoader.addPart('collection');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('collection-table', {
            parent: 'entity',
            url: '/tabla-cobranza',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'Aditum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection/collection-table.html',
                    controller: 'CollectionTableController',
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
                    $translatePartialLoader.addPart('collection');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('collection-detail', {
            parent: 'collection',
            url: '/collection/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.collection.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection/collection-detail.html',
                    controller: 'CollectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('collection');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Collection', function($stateParams, Collection) {
                    return Collection.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'collection',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('collection-detail.edit', {
            parent: 'collection-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection/collection-dialog.html',
                    controller: 'CollectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Collection', function(Collection) {
                            return Collection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection.new', {
            parent: 'collection',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection/collection-dialog.html',
                    controller: 'CollectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                mensualBalance: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collection', null, { reload: 'collection' });
                }, function() {
                    $state.go('collection');
                });
            }]
        })
        .state('collection.edit', {
            parent: 'collection',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection/collection-dialog.html',
                    controller: 'CollectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Collection', function(Collection) {
                            return Collection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection', null, { reload: 'collection' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection.delete', {
            parent: 'collection',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection/collection-delete-dialog.html',
                    controller: 'CollectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Collection', function(Collection) {
                            return Collection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection', null, { reload: 'collection' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
