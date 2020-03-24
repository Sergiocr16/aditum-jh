(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('revision-task', {
            parent: 'entity',
            url: '/revision-task',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.revisionTask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision-task/revision-tasks.html',
                    controller: 'RevisionTaskController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revisionTask');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('revision-task-detail', {
            parent: 'revision-task',
            url: '/revision-task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.revisionTask.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision-task/revision-task-detail.html',
                    controller: 'RevisionTaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revisionTask');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RevisionTask', function($stateParams, RevisionTask) {
                    return RevisionTask.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'revision-task',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('revision-task-detail.edit', {
            parent: 'revision-task-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task/revision-task-dialog.html',
                    controller: 'RevisionTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevisionTask', function(RevisionTask) {
                            return RevisionTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision-task.new', {
            parent: 'revision-task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task/revision-task-dialog.html',
                    controller: 'RevisionTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                done: null,
                                observations: null,
                                observationFile: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('revision-task', null, { reload: 'revision-task' });
                }, function() {
                    $state.go('revision-task');
                });
            }]
        })
        .state('revision-task.edit', {
            parent: 'revision-task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task/revision-task-dialog.html',
                    controller: 'RevisionTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevisionTask', function(RevisionTask) {
                            return RevisionTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision-task', null, { reload: 'revision-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision-task.delete', {
            parent: 'revision-task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision-task/revision-task-delete-dialog.html',
                    controller: 'RevisionTaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RevisionTask', function(RevisionTask) {
                            return RevisionTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision-task', null, { reload: 'revision-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
