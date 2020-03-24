(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subsidiary', {
            parent: 'entity',
            url: '/subsidiary',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER'],
                pageTitle: 'aditumApp.subsidiary.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsidiary/subsidiaries.html',
                    controller: 'SubsidiaryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsidiary');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('subsidiary-detail', {
            parent: 'subsidiary',
            url: '/subsidiary/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER'],
                pageTitle: 'aditumApp.subsidiary.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsidiary/subsidiary-detail.html',
                    controller: 'SubsidiaryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsidiary');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Subsidiary', function($stateParams, Subsidiary) {
                    return Subsidiary.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'subsidiary',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('subsidiary-detail.edit', {
            parent: 'subsidiary-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary/subsidiary-dialog.html',
                    controller: 'SubsidiaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subsidiary', function(Subsidiary) {
                            return Subsidiary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsidiary.new', {
            parent: 'subsidiary',
            url: '/new',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary/subsidiary-dialog.html',
                    controller: 'SubsidiaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                deleted: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subsidiary', null, { reload: 'subsidiary' });
                }, function() {
                    $state.go('subsidiary');
                });
            }]
        })
        .state('subsidiary.edit', {
            parent: 'subsidiary',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary/subsidiary-dialog.html',
                    controller: 'SubsidiaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subsidiary', function(Subsidiary) {
                            return Subsidiary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsidiary', null, { reload: 'subsidiary' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsidiary.delete', {
            parent: 'subsidiary',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary/subsidiary-delete-dialog.html',
                    controller: 'SubsidiaryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Subsidiary', function(Subsidiary) {
                            return Subsidiary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsidiary', null, { reload: 'subsidiary' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
