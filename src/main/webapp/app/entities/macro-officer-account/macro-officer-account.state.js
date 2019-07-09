(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro-condominium-officer-account', {
            parent: 'entity',
            url: '/macro-condominium-officer-account/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.macroOfficerAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-officer-account/macro-officer-accounts.html',
                    controller: 'MacroOfficerAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroOfficerAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('macro-officer-account-detail', {
            parent: 'macro-condominium-officer-account',
            url: '/macro-officer-account/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.macroOfficerAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-officer-account/macro-officer-account-detail.html',
                    controller: 'MacroOfficerAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroOfficerAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MacroOfficerAccount', function($stateParams, MacroOfficerAccount) {
                    return MacroOfficerAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'macro-officer-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('macro-officer-account-detail.edit', {
            parent: 'macro-officer-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-officer-account/macro-officer-account-dialog.html',
                    controller: 'MacroOfficerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroOfficerAccount', function(MacroOfficerAccount) {
                            return MacroOfficerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-condominium-officer-account.new', {
            parent: 'macro-condominium-officer-account',
            url: '/new/',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-officer-account/macro-officer-account-dialog.html',
                    controller: 'MacroOfficerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                enabled: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro-condominium-officer-account', null, { reload: 'macro-condominium-officer-account' });
                }, function() {
                    $state.go('macro-condominium-officer-account', null, { reload: 'macro-condominium-officer-account' });
                });
            }]
        })
        .state('macro-condominium-officer-account.edit', {
            parent: 'macro-condominium-officer-account',
            url: '/edit/{accountId}',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-officer-account/macro-officer-account-dialog.html',
                    controller: 'MacroOfficerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroOfficerAccount', function(MacroOfficerAccount) {
                            return MacroOfficerAccount.get({id : $stateParams.accountId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-condominium-officer-account', null, { reload: 'macro-condominium-officer-account' });
                }, function() {
                    $state.go('macro-condominium-officer-account', null, { reload: 'macro-condominium-officer-account' });
                });
            }]
        })
        .state('macro-condominium-officer-account.delete', {
            parent: 'macro-condominium-officer-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-officer-account/macro-officer-account-delete-dialog.html',
                    controller: 'MacroOfficerAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MacroOfficerAccount', function(MacroOfficerAccount) {
                            return MacroOfficerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-officer-account', null, { reload: 'macro-officer-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
