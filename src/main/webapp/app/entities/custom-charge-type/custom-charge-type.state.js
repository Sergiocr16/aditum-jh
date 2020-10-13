(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('custom-charge-type', {
            parent: 'entity',
            url: '/custom-charge-type',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.customChargeType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/custom-charge-type/custom-charge-types.html',
                    controller: 'CustomChargeTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customChargeType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('custom-charge-type-detail', {
            parent: 'custom-charge-type',
            url: '/custom-charge-type/{id}',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.customChargeType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/custom-charge-type/custom-charge-type-detail.html',
                    controller: 'CustomChargeTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customChargeType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomChargeType', function($stateParams, CustomChargeType) {
                    return CustomChargeType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'custom-charge-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('custom-charge-type-detail.edit', {
            parent: 'custom-charge-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-charge-type/custom-charge-type-dialog.html',
                    controller: 'CustomChargeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomChargeType', function(CustomChargeType) {
                            return CustomChargeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('custom-charge-type.new', {
            parent: 'custom-charge-type',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-charge-type/custom-charge-type-dialog.html',
                    controller: 'CustomChargeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                type: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('custom-charge-type', null, { reload: 'custom-charge-type' });
                }, function() {
                    $state.go('custom-charge-type');
                });
            }]
        })
        .state('custom-charge-type.edit', {
            parent: 'custom-charge-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-charge-type/custom-charge-type-dialog.html',
                    controller: 'CustomChargeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomChargeType', function(CustomChargeType) {
                            return CustomChargeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('custom-charge-type', null, { reload: 'custom-charge-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('custom-charge-type.delete', {
            parent: 'custom-charge-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-charge-type/custom-charge-type-delete-dialog.html',
                    controller: 'CustomChargeTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomChargeType', function(CustomChargeType) {
                            return CustomChargeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('custom-charge-type', null, { reload: 'custom-charge-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
