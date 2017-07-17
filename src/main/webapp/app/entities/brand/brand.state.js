(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('brand', {
            parent: 'entity',
            url: '/brand',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.brand.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/brand/brands.html',
                    controller: 'BrandController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('brand');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('brand-detail', {
            parent: 'brand',
            url: '/brand/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.brand.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/brand/brand-detail.html',
                    controller: 'BrandDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('brand');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Brand', function($stateParams, Brand) {
                    return Brand.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'brand',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('brand-detail.edit', {
            parent: 'brand-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/brand/brand-dialog.html',
                    controller: 'BrandDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Brand', function(Brand) {
                            return Brand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('brand.new', {
            parent: 'brand',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/brand/brand-dialog.html',
                    controller: 'BrandDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                brand: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('brand', null, { reload: 'brand' });
                }, function() {
                    $state.go('brand');
                });
            }]
        })
        .state('brand.edit', {
            parent: 'brand',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/brand/brand-dialog.html',
                    controller: 'BrandDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Brand', function(Brand) {
                            return Brand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('brand', null, { reload: 'brand' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('brand.delete', {
            parent: 'brand',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/brand/brand-delete-dialog.html',
                    controller: 'BrandDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Brand', function(Brand) {
                            return Brand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('brand', null, { reload: 'brand' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
