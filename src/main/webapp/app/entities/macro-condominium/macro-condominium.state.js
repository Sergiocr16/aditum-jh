(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro-condominium', {
            parent: 'entity',
            url: '/macro-condominium',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.macroCondominium.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-condominium/macro-condominiums.html',
                    controller: 'MacroCondominiumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroCondominium');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('macro-condominium-detail', {
            parent: 'macro-condominium',
            url: '/macro-condominium/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.macroCondominium.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-condominium/macro-condominium-detail.html',
                    controller: 'MacroCondominiumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroCondominium');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MacroCondominium', function($stateParams, MacroCondominium) {
                    return MacroCondominium.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'macro-condominium',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('macro-condominium-detail.edit', {
            parent: 'macro-condominium-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-condominium/macro-condominium-dialog.html',
                    controller: 'MacroCondominiumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroCondominium', function(MacroCondominium) {
                            return MacroCondominium.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-condominium.new', {
            parent: 'macro-condominium',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-condominium/macro-condominium-dialog.html',
                    controller: 'MacroCondominiumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                enabled: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro-condominium', null, { reload: 'macro-condominium' });
                }, function() {
                    $state.go('macro-condominium');
                });
            }]
        })
        .state('macro-condominium.edit', {
            parent: 'macro-condominium',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-condominium/macro-condominium-dialog.html',
                    controller: 'MacroCondominiumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroCondominium', function(MacroCondominium) {
                            return MacroCondominium.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-condominium', null, { reload: 'macro-condominium' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-condominium.delete', {
            parent: 'macro-condominium',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-condominium/macro-condominium-delete-dialog.html',
                    controller: 'MacroCondominiumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MacroCondominium', function(MacroCondominium) {
                            return MacroCondominium.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-condominium', null, { reload: 'macro-condominium' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
