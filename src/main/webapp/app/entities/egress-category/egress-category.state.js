(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('egress-category', {
            parent: 'entity',
            url: '/egress-category',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                pageTitle: 'aditumApp.egressCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/egress-category/egress-categories.html',
                    // templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'EgressCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('egressCategory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('egress-category-detail', {
            parent: 'egress-category',
            url: '/egress-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.egressCategory.detail.title'
            },
            views: {
                'content@': {
                   templateUrl: 'app/entities/egress-category/egress-category-detail.html',
                    // templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'EgressCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('egressCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EgressCategory', function($stateParams, EgressCategory) {
                    return EgressCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'egress-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('egress-category-detail.edit', {
            parent: 'egress-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                   templateUrl: 'app/entities/egress-category/egress-category-dialog.html',
                    //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'EgressCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EgressCategory', function(EgressCategory) {
                            return EgressCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('egress-category.new', {
            parent: 'egress-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                     templateUrl: 'app/entities/egress-category/egress-category-dialog.html',
                    //    templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'EgressCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                group: null,
                                category: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('egress-category', null, { reload: 'egress-category' });
                }, function() {
                    $state.go('egress-category');
                });
            }]
        })
        .state('egress-category.edit', {
            parent: 'egress-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                  templateUrl: 'app/entities/egress-category/egress-category-dialog.html',
                    //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'EgressCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EgressCategory', function(EgressCategory) {
                            return EgressCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('egress-category', null, { reload: 'egress-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('egress-category.delete', {
            parent: 'egress-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/egress-category/egress-category-delete-dialog.html',
                    //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'EgressCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EgressCategory', function(EgressCategory) {
                            return EgressCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('egress-category', null, { reload: 'egress-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
