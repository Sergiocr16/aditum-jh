(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subsidiary-category', {
            parent: 'entity',
            url: '/subsidiary-category',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.subsidiaryCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsidiary-category/subsidiary-categories.html',
                    controller: 'SubsidiaryCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsidiaryCategory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('subsidiary-category-detail', {
            parent: 'subsidiary-category',
            url: '/subsidiary-category/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.subsidiaryCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsidiary-category/subsidiary-category-detail.html',
                    controller: 'SubsidiaryCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsidiaryCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SubsidiaryCategory', function($stateParams, SubsidiaryCategory) {
                    return SubsidiaryCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'subsidiary-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('subsidiary-category-detail.edit', {
            parent: 'subsidiary-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-category/subsidiary-category-dialog.html',
                    controller: 'SubsidiaryCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubsidiaryCategory', function(SubsidiaryCategory) {
                            return SubsidiaryCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsidiary-category.new', {
            parent: 'subsidiary-category',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-category/subsidiary-category-dialog.html',
                    controller: 'SubsidiaryCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                categoryType: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subsidiary-category', null, { reload: 'subsidiary-category' });
                }, function() {
                    $state.go('subsidiary-category');
                });
            }]
        })
        .state('subsidiary-category.edit', {
            parent: 'subsidiary-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-category/subsidiary-category-dialog.html',
                    controller: 'SubsidiaryCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubsidiaryCategory', function(SubsidiaryCategory) {
                            return SubsidiaryCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsidiary-category', null, { reload: 'subsidiary-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsidiary-category.delete', {
            parent: 'subsidiary-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-category/subsidiary-category-delete-dialog.html',
                    controller: 'SubsidiaryCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SubsidiaryCategory', function(SubsidiaryCategory) {
                            return SubsidiaryCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsidiary-category', null, { reload: 'subsidiary-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
