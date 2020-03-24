(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('balance-by-account', {
            parent: 'entity',
            url: '/balance-by-account',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.balanceByAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/balance-by-account/balance-by-accounts.html',
                    controller: 'BalanceByAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('balanceByAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('balance-by-account-detail', {
            parent: 'balance-by-account',
            url: '/balance-by-account/{id}',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.balanceByAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/balance-by-account/balance-by-account-detail.html',
                    controller: 'BalanceByAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('balanceByAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BalanceByAccount', function($stateParams, BalanceByAccount) {
                    return BalanceByAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'balance-by-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('balance-by-account-detail.edit', {
            parent: 'balance-by-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/balance-by-account/balance-by-account-dialog.html',
                    controller: 'BalanceByAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BalanceByAccount', function(BalanceByAccount) {
                            return BalanceByAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('balance-by-account.new', {
            parent: 'balance-by-account',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/balance-by-account/balance-by-account-dialog.html',
                    controller: 'BalanceByAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                accountId: null,
                                balance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('balance-by-account', null, { reload: 'balance-by-account' });
                }, function() {
                    $state.go('balance-by-account');
                });
            }]
        })
        .state('balance-by-account.edit', {
            parent: 'balance-by-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/balance-by-account/balance-by-account-dialog.html',
                    controller: 'BalanceByAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BalanceByAccount', function(BalanceByAccount) {
                            return BalanceByAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('balance-by-account', null, { reload: 'balance-by-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('balance-by-account.delete', {
            parent: 'balance-by-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/balance-by-account/balance-by-account-delete-dialog.html',
                    controller: 'BalanceByAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BalanceByAccount', function(BalanceByAccount) {
                            return BalanceByAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('balance-by-account', null, { reload: 'balance-by-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
