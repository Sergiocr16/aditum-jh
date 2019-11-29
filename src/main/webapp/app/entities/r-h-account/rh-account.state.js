(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rh-account', {
            parent: 'entity',
            url: '/rh-account?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/r-h-account/r-h-accounts.html',
                    controller: 'RHAccountController',
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
                    $translatePartialLoader.addPart('rHAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rh-account-detail', {
            parent: 'rh-account',
            url: '/rh-account/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/r-h-account/rh-account-detail.html',
                    controller: 'RHAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rHAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RHAccount', function($stateParams, RHAccount) {
                    return RHAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rh-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rh-account-detail.edit', {
            parent: 'rh-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/r-h-account/rh-account-dialog.html',
                    controller: 'RHAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RHAccount', function(RHAccount) {
                            return RHAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rh-account.new', {
            parent: 'rh-account',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/r-h-account/rh-account-dialog.html',
                    controller: 'RHAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lastname: null,
                                secondlastname: null,
                                enterprisename: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rh-account', null, { reload: 'rh-account' });
                }, function() {
                    $state.go('rh-account');
                });
            }]
        })
        .state('rh-account.edit', {
            parent: 'rh-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/r-h-account/rh-account-dialog.html',
                    controller: 'RHAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RHAccount', function(RHAccount) {
                            return RHAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rh-account', null, { reload: 'rh-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rh-account.delete', {
            parent: 'rh-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/r-h-account/rh-account-delete-dialog.html',
                    controller: 'RHAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RHAccount', function(RHAccount) {
                            return RHAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rh-account', null, { reload: 'rh-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
