(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('revision', {
            parent: 'entity',
            url: '/revision',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.revision.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision/revisions.html',
                    controller: 'RevisionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revision');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('revision-detail', {
            parent: 'revision',
            url: '/revision/{id}',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.revision.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/revision/revision-detail.html',
                    controller: 'RevisionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('revision');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Revision','CommonMethods', function($stateParams, Revision, CommonMethods) {
                    var id = CommonMethods.decryptIdUrl($stateParams.id)
                    return Revision.get({id : id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'revision',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('revision-detail.edit', {
            parent: 'revision-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision/revision-dialog.html',
                    controller: 'RevisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Revision', function(Revision) {
                            return Revision.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision.new', {
            parent: 'revision',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision/revision-dialog.html',
                    controller: 'RevisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                executionDate: null,
                                observations: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('revision', null, { reload: 'revision' });
                }, function() {
                    $state.go('revision');
                });
            }]
        })
        .state('revision.edit', {
            parent: 'revision',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision/revision-dialog.html',
                    controller: 'RevisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Revision', function(Revision) {
                            return Revision.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision', null, { reload: 'revision' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('revision.delete', {
            parent: 'revision',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/revision/revision-delete-dialog.html',
                    controller: 'RevisionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Revision', function(Revision) {
                            return Revision.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('revision', null, { reload: 'revision' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
