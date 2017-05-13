(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('access-door', {
            parent: 'entity',
            url: '/access-door?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.accessDoor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/access-door/access-doors.html',
                    controller: 'AccessDoorController',
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
                    $translatePartialLoader.addPart('accessDoor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('access-door-detail', {
            parent: 'access-door',
            url: '/access-door/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.accessDoor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/access-door/access-door-detail.html',
                    controller: 'AccessDoorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accessDoor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AccessDoor', function($stateParams, AccessDoor) {
                    return AccessDoor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'access-door',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('access-door-detail.edit', {
            parent: 'access-door-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/access-door/access-door-dialog.html',
                    controller: 'AccessDoorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccessDoor', function(AccessDoor) {
                            return AccessDoor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('access-door.new', {
            parent: 'access-door',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/access-door/access-door-dialog.html',
                    controller: 'AccessDoorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('access-door', null, { reload: 'access-door' });
                }, function() {
                    $state.go('access-door');
                });
            }]
        })
                .state('main-access-door.newWatch', {
                    parent: 'main-access-door',
                    url: '/change-watch',
                    data: {
                        authorities: ['ROLE_OFFICER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'app/entities/access-door/change-watches.html',
                            controller: 'ChangeWatchesController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: function () {
                                    return {
                                        name: null,
                                        id: null
                                    };
                                }
                            }
                        }).result.then(function() {
                            $state.go('access-door', null, { reload: 'access-door' });
                        }, function() {
                            $state.go('access-door');
                        });
                    }]
                })
        .state('access-door.edit', {
            parent: 'access-door',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/access-door/access-door-dialog.html',
                    controller: 'AccessDoorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccessDoor', function(AccessDoor) {
                            return AccessDoor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('access-door', null, { reload: 'access-door' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('access-door.delete', {
            parent: 'access-door',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/access-door/access-door-delete-dialog.html',
                    controller: 'AccessDoorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccessDoor', function(AccessDoor) {
                            return AccessDoor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('access-door', null, { reload: 'access-door' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('main-access-door', {
               parent: 'entity',
               url: '/main-access-door',
               data: {
                   authorities: ['ROLE_OFFICER']
               },
               views: {
                   'access_door@': {
                       templateUrl: 'app/entities/access-door/main-access-door.html',
                       controller: 'AccessDoorController',
                       controllerAs: 'vm'
                   }
               },
           })
    }

})();
