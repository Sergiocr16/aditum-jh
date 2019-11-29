(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bitacora-acciones', {
            parent: 'entity',
            url: '/bitacora-acciones?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'aditumApp.bitacoraAcciones.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bitacora-acciones/bitacora-acciones.html',
                    controller: 'BitacoraAccionesController',
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
                    $translatePartialLoader.addPart('bitacoraAcciones');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bitacora-acciones-detail', {
            parent: 'bitacora-acciones',
            url: '/bitacora-acciones/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.bitacoraAcciones.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bitacora-acciones/bitacora-acciones-detail.html',
                    controller: 'BitacoraAccionesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bitacoraAcciones');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BitacoraAcciones', function($stateParams, BitacoraAcciones) {
                    return BitacoraAcciones.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bitacora-acciones',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bitacora-acciones-detail.edit', {
            parent: 'bitacora-acciones-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bitacora-acciones/bitacora-acciones-dialog.html',
                    controller: 'BitacoraAccionesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BitacoraAcciones', function(BitacoraAcciones) {
                            return BitacoraAcciones.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bitacora-acciones.new', {
            parent: 'bitacora-acciones',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bitacora-acciones/bitacora-acciones-dialog.html',
                    controller: 'BitacoraAccionesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                concept: null,
                                type: null,
                                idReference: null,
                                idResponsable: null,
                                ejecutionDate: null,
                                category: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bitacora-acciones', null, { reload: 'bitacora-acciones' });
                }, function() {
                    $state.go('bitacora-acciones');
                });
            }]
        })
        .state('bitacora-acciones.edit', {
            parent: 'bitacora-acciones',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bitacora-acciones/bitacora-acciones-dialog.html',
                    controller: 'BitacoraAccionesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BitacoraAcciones', function(BitacoraAcciones) {
                            return BitacoraAcciones.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bitacora-acciones', null, { reload: 'bitacora-acciones' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bitacora-acciones.delete', {
            parent: 'bitacora-acciones',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bitacora-acciones/bitacora-acciones-delete-dialog.html',
                    controller: 'BitacoraAccionesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BitacoraAcciones', function(BitacoraAcciones) {
                            return BitacoraAcciones.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bitacora-acciones', null, { reload: 'bitacora-acciones' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
