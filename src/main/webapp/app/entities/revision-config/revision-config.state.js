(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('revision-config', {
                parent: 'entity',
                url: '/revision-config',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.revisionConfig.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/revision-config/revision-configs.html',
                        controller: 'RevisionConfigController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('revisionConfig');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('revision-config-detail', {
                parent: 'revision-config',
                url: '/revision-config/{id}',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.revisionConfig.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/revision-config/revision-config-detail.html',
                        controller: 'RevisionConfigDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('revisionConfig');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RevisionConfig', 'CommonMethods', function ($stateParams, RevisionConfig, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return RevisionConfig.get({id: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'revision-config',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('revision-config-detail.edit', {
                parent: 'revision-config-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/revision-config/revision-config-dialog.html',
                        controller: 'RevisionConfigDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['RevisionConfig', 'CommonMethods', function (RevisionConfig, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return RevisionConfig.get({id: id}).$promise;
                    }]
                }
            })
            .state('revision-config.new', {
                parent: 'revision-config',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/revision-config/revision-config-dialog.html',
                        controller: 'RevisionConfigDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            description: null,
                            configTasks: []
                        };
                    },
                }
            })
            .state('revision-config.edit', {
                parent: 'revision-config',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/revision-config/revision-config-dialog.html',
                        controller: 'RevisionConfigDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['RevisionConfig', 'CommonMethods','$stateParams', function (RevisionConfig, CommonMethods,$stateParams) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return RevisionConfig.get({id: id}).$promise;
                    }]
                }
            })
            .state('revision-config.delete', {
                parent: 'revision-config',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/revision-config/revision-config-delete-dialog.html',
                        controller: 'RevisionConfigDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['RevisionConfig', function (RevisionConfig) {
                                return RevisionConfig.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('revision-config', null, {reload: 'revision-config'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
