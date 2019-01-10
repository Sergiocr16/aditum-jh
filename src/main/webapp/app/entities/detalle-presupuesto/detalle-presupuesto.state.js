(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('detalle-presupuesto', {
            parent: 'entity',
            url: '/detalle-presupuesto',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.detallePresupuesto.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuestos.html',
                    controller: 'DetallePresupuestoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('detallePresupuesto');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('detalle-presupuesto-detail', {
            parent: 'detalle-presupuesto',
            url: '/detalle-presupuesto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.detallePresupuesto.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuesto-detail.html',
                    controller: 'DetallePresupuestoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('detallePresupuesto');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DetallePresupuesto', function($stateParams, DetallePresupuesto) {
                    return DetallePresupuesto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'detalle-presupuesto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('detalle-presupuesto-detail.edit', {
            parent: 'detalle-presupuesto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuesto-dialog.html',
                    controller: 'DetallePresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetallePresupuesto', function(DetallePresupuesto) {
                            return DetallePresupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detalle-presupuesto.new', {
            parent: 'detalle-presupuesto',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD']
            },
            views: {
                'content@': {
                           templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuesto-dialog.html',
                                     controller: 'DetallePresupuestoDialogController',
                                     controllerAs: 'vm',
                }
            },
            resolve: {

                entity: function() {
                    return {
                           category: null,
                           valuePerMonth: null,
                           type: null,
                           presupuestoId: null,
                           id: null
                    };
                },
                previousState: ["$state", function($state) {
                    var currentStateData = {
                        name: $state.current.name || 'detalle-presupuesto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }

        })

//
//
//        .state('detalle-presupuesto.new', {
//            parent: 'detalle-presupuesto',
//            url: '/new',
//            data: {
//                authorities: ['ROLE_USER']
//            },
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuesto-dialog.html',
//                    controller: 'DetallePresupuestoDialogController',
//                    controllerAs: 'vm',
//                    backdrop: 'static',
//                    size: 'lg',
//                    resolve: {
//                        entity: function () {
//                            return {
//                                category: null,
//                                valuePerMonth: null,
//                                type: null,
//                                presupuestoId: null,
//                                id: null
//                            };
//                        }
//                    }
//                }).result.then(function() {
//                    $state.go('detalle-presupuesto', null, { reload: 'detalle-presupuesto' });
//                }, function() {
//                    $state.go('detalle-presupuesto');
//                });
//            }]
//        })
        .state('detalle-presupuesto.edit', {
            parent: 'detalle-presupuesto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuesto-dialog.html',
                    controller: 'DetallePresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetallePresupuesto', function(DetallePresupuesto) {
                            return DetallePresupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('detalle-presupuesto', null, { reload: 'detalle-presupuesto' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detalle-presupuesto.delete', {
            parent: 'detalle-presupuesto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detalle-presupuesto/detalle-presupuesto-delete-dialog.html',
                    controller: 'DetallePresupuestoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DetallePresupuesto', function(DetallePresupuesto) {
                            return DetallePresupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('detalle-presupuesto', null, { reload: 'detalle-presupuesto' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
