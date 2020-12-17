(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-sended', {
            parent: 'entity',
            url: '/notification-sended?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.notificationSended.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-sended/notification-sendeds.html',
                    controller: 'NotificationSendedController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notificationSended');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notification-sended-detail', {
            parent: 'notification-sended',
            url: '/notification-sended/{id}',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.notificationSended.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-sended/notification-sended-detail.html',
                    controller: 'NotificationSendedDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notificationSended');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NotificationSended', function($stateParams, NotificationSended) {
                    return NotificationSended.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification-sended',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notification-sended-detail.edit', {
            parent: 'notification-sended-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-sended/notification-sended-dialog.html',
                    controller: 'NotificationSendedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationSended', function(NotificationSended) {
                            return NotificationSended.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-sended.new', {
            parent: 'notification-sended',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-sended/notification-sended-dialog.html',
                    controller: 'NotificationSendedDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        title: null,
                        description: null,
                        sendedTo: null,
                        url: null,
                        state: null,
                        id: null
                    };
                }
            }
        })
        .state('notification-sended.edit', {
            parent: 'notification-sended',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-sended/notification-sended-dialog.html',
                    controller: 'NotificationSendedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationSended', function(NotificationSended) {
                            return NotificationSended.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-sended', null, { reload: 'notification-sended' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-sended.delete', {
            parent: 'notification-sended',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_JD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-sended/notification-sended-delete-dialog.html',
                    controller: 'NotificationSendedDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationSended', function(NotificationSended) {
                            return NotificationSended.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-sended', null, { reload: 'notification-sended' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
