(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('token-notifications', {
            parent: 'entity',
            url: '/token-notifications',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.tokenNotifications.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/token-notifications/token-notifications.html',
                    controller: 'TokenNotificationsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tokenNotifications');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('token-notifications-detail', {
            parent: 'token-notifications',
            url: '/token-notifications/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.tokenNotifications.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/token-notifications/token-notifications-detail.html',
                    controller: 'TokenNotificationsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tokenNotifications');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TokenNotifications', function($stateParams, TokenNotifications) {
                    return TokenNotifications.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'token-notifications',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('token-notifications-detail.edit', {
            parent: 'token-notifications-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/token-notifications/token-notifications-dialog.html',
                    controller: 'TokenNotificationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TokenNotifications', function(TokenNotifications) {
                            return TokenNotifications.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('token-notifications.new', {
            parent: 'token-notifications',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/token-notifications/token-notifications-dialog.html',
                    controller: 'TokenNotificationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                token: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('token-notifications', null, { reload: 'token-notifications' });
                }, function() {
                    $state.go('token-notifications');
                });
            }]
        })
        .state('token-notifications.edit', {
            parent: 'token-notifications',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/token-notifications/token-notifications-dialog.html',
                    controller: 'TokenNotificationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TokenNotifications', function(TokenNotifications) {
                            return TokenNotifications.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('token-notifications', null, { reload: 'token-notifications' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('token-notifications.delete', {
            parent: 'token-notifications',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/token-notifications/token-notifications-delete-dialog.html',
                    controller: 'TokenNotificationsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TokenNotifications', function(TokenNotifications) {
                            return TokenNotifications.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('token-notifications', null, { reload: 'token-notifications' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
