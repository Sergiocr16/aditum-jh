(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro-condominium-admin-account', {
            parent: 'entity',
            url: '/macro-condominium-admin-account/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.macroAdminAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-admin-account/macro-admin-accounts.html',
                    controller: 'MacroAdminAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroAdminAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('macro-admin-account-detail', {
            parent: 'macro-admin-account',
            url: '/macro-admin-account/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.macroAdminAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-admin-account/macro-admin-account-detail.html',
                    controller: 'MacroAdminAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroAdminAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MacroAdminAccount', function($stateParams, MacroAdminAccount) {
                    return MacroAdminAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'macro-admin-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('macro-admin-account-detail.edit', {
            parent: 'macro-admin-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-admin-account/macro-admin-account-dialog.html',
                    controller: 'MacroAdminAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroAdminAccount', function(MacroAdminAccount) {
                            return MacroAdminAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-condominium-admin-account.new', {
            parent: 'macro-condominium-admin-account',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-admin-account/macro-admin-account-dialog.html',
                    controller: 'MacroAdminAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lastname: null,
                                secondlastname: null,
                                identificationnumber: null,
                                email: null,
                                enabled: false,
                                imageUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro-condominium-admin-account', null, { reload: 'macro-condominium-admin-account' });
                }, function() {
                    $state.go('macro-condominium-admin-account');
                });
            }]
        })
        .state('macro-condominium-admin-account.edit', {
            parent: 'macro-condominium-admin-account',
            url: '/{accountId}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-admin-account/macro-admin-account-dialog.html',
                    controller: 'MacroAdminAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroAdminAccount', function(MacroAdminAccount) {
                            return MacroAdminAccount.get({id : $stateParams.accountId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-condominium-admin-account', null, { reload: 'macro-condominium-admin-account' });
                }, function() {
                    $state.go('macro-condominium-admin-account');
                });
            }]
        })
        .state('macro-admin-account.delete', {
            parent: 'macro-admin-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-admin-account/macro-admin-account-delete-dialog.html',
                    controller: 'MacroAdminAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MacroAdminAccount', function(MacroAdminAccount) {
                            return MacroAdminAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-admin-account', null, { reload: 'macro-admin-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
