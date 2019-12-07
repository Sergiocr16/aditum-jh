(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activos-fijos', {
            parent: 'entity',
            url: '/activos-fijos?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.activosFijos.home.title'
            },
            views: {
                'content@': {
                    // templateUrl: 'app/entities/activos-fijos/activos-fijos.html',
                    templateUrl: 'app/entities/company/commingSoonFinanzes.html',
                    controller: 'ActivosFijosController',
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
                    $translatePartialLoader.addPart('activosFijos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('activos-fijos-detail', {
            parent: 'activos-fijos',
            url: '/activos-fijos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.activosFijos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activos-fijos/activos-fijos-detail.html',
                    controller: 'ActivosFijosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activosFijos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActivosFijos', function($stateParams, ActivosFijos) {
                    return ActivosFijos.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'activos-fijos',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('activos-fijos-detail.edit', {
            parent: 'activos-fijos-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activos-fijos/activos-fijos-dialog.html',
                    controller: 'ActivosFijosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivosFijos', function(ActivosFijos) {
                            return ActivosFijos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activos-fijos.new', {
            parent: 'activos-fijos',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activos-fijos/activos-fijos-dialog.html',
                    controller: 'ActivosFijosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                deleted: null,
                                nombre: null,
                                valor: null,
                                depreciacion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activos-fijos', null, { reload: 'activos-fijos' });
                }, function() {
                    $state.go('activos-fijos');
                });
            }]
        })
        .state('activos-fijos.edit', {
            parent: 'activos-fijos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activos-fijos/activos-fijos-dialog.html',
                    controller: 'ActivosFijosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivosFijos', function(ActivosFijos) {
                            return ActivosFijos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activos-fijos', null, { reload: 'activos-fijos' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activos-fijos.delete', {
            parent: 'activos-fijos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activos-fijos/activos-fijos-delete-dialog.html',
                    controller: 'ActivosFijosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActivosFijos', function(ActivosFijos) {
                            return ActivosFijos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activos-fijos', null, { reload: 'activos-fijos' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
