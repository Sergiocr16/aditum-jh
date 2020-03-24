(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('revision-config-task', {
            parent: 'entity',
            url: '/revision-config-task',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.revisionConfigTask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision-config-task/revision-config-tasks.html',
                    controller: 'RevisionConfigTaskController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revisionConfigTask');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('revision-config-task-detail', {
            parent: 'revision-config-task',
            url: '/revision-config-task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.revisionConfigTask.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision-config-task/revision-config-task-detail.html',
                    controller: 'RevisionConfigTaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revisionConfigTask');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RevisionConfigTask', function($stateParams, RevisionConfigTask) {
                    return RevisionConfigTask.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'revision-config-task',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('revision-config-task-detail.edit', {
            parent: 'revision-config-task-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-config-task/revision-config-task-dialog.html',
                    controller: 'RevisionConfigTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevisionConfigTask', function(RevisionConfigTask) {
                            return RevisionConfigTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision-config-task.new', {
            parent: 'revision-config-task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-config-task/revision-config-task-dialog.html',
                    controller: 'RevisionConfigTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                observations: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('revision-config-task', null, { reload: 'revision-config-task' });
                }, function() {
                    $state.go('revision-config-task');
                });
            }]
        })
        .state('revision-config-task.edit', {
            parent: 'revision-config-task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-config-task/revision-config-task-dialog.html',
                    controller: 'RevisionConfigTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevisionConfigTask', function(RevisionConfigTask) {
                            return RevisionConfigTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision-config-task', null, { reload: 'revision-config-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision-config-task.delete', {
            parent: 'revision-config-task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-config-task/revision-config-task-delete-dialog.html',
                    controller: 'RevisionConfigTaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RevisionConfigTask', function(RevisionConfigTask) {
                            return RevisionConfigTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision-config-task', null, { reload: 'revision-config-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
