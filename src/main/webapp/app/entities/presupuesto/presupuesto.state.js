(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('presupuesto', {
            parent: 'entity',
            url: '/presupuesto',
            data: {
                  authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                pageTitle: 'aditumApp.presupuesto.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/presupuesto/presupuestos.html',
                    controller: 'PresupuestoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('presupuesto');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('presupuesto-detail', {
            parent: 'presupuesto',
            url: '/presupuesto/{id}',
            data: {
                     authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD'],
                pageTitle: 'aditumApp.presupuesto.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/presupuesto/presupuesto-detail.html',
                    controller: 'PresupuestoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('presupuesto');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Presupuesto', function($stateParams, Presupuesto) {
                    return Presupuesto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'presupuesto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('presupuesto-detail.edit', {
            parent: 'presupuesto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-dialog.html',
                    controller: 'PresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Presupuesto', function(Presupuesto) {
                            return Presupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('presupuesto.edit', {
            parent: 'presupuesto',
            url: '/new',
            data: {
                   authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-dialog.html',
                    controller: 'PresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                modificationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('presupuesto', null, { reload: 'presupuesto' });
                }, function() {
                    $state.go('presupuesto');
                });
            }]
        })
        .state('presupuesto.delete', {
            parent: 'presupuesto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-delete-dialog.html',
                    controller: 'PresupuestoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Presupuesto', function(Presupuesto) {
                            return Presupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('presupuesto', null, { reload: 'presupuesto' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
