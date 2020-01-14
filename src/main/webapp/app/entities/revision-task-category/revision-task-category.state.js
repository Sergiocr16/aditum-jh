(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('revision-task-category', {
            parent: 'entity',
            url: '/revision-task-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.revisionTaskCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision-task-category/revision-task-categories.html',
                    controller: 'RevisionTaskCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revisionTaskCategory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('revision-task-category-detail', {
            parent: 'revision-task-category',
            url: '/revision-task-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.revisionTaskCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision-task-category/revision-task-category-detail.html',
                    controller: 'RevisionTaskCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revisionTaskCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RevisionTaskCategory', function($stateParams, RevisionTaskCategory) {
                    return RevisionTaskCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'revision-task-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('revision-task-category-detail.edit', {
            parent: 'revision-task-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task-category/revision-task-category-dialog.html',
                    controller: 'RevisionTaskCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevisionTaskCategory', function(RevisionTaskCategory) {
                            return RevisionTaskCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision-task-category.new', {
            parent: 'revision-task-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task-category/revision-task-category-dialog.html',
                    controller: 'RevisionTaskCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                deleted: null,
                                order: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('revision-task-category', null, { reload: 'revision-task-category' });
                }, function() {
                    $state.go('revision-task-category');
                });
            }]
        })
        .state('revision-task-category.edit', {
            parent: 'revision-task-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task-category/revision-task-category-dialog.html',
                    controller: 'RevisionTaskCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevisionTaskCategory', function(RevisionTaskCategory) {
                            return RevisionTaskCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision-task-category', null, { reload: 'revision-task-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision-task-category.delete', {
            parent: 'revision-task-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task-category/revision-task-category-delete-dialog.html',
                    controller: 'RevisionTaskCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RevisionTaskCategory', function(RevisionTaskCategory) {
                            return RevisionTaskCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision-task-category', null, { reload: 'revision-task-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
