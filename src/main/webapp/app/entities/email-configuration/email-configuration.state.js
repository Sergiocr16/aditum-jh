(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('email-configuration', {
            parent: 'entity',
            url: '/email-configuration',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.emailConfiguration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email-configuration/email-configurations.html',
                    controller: 'EmailConfigurationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('emailConfiguration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('email-configuration-detail', {
            parent: 'email-configuration',
            url: '/email-configuration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.emailConfiguration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email-configuration/email-configuration-detail.html',
                    controller: 'EmailConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('emailConfiguration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmailConfiguration', function($stateParams, EmailConfiguration) {
                    return EmailConfiguration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'email-configuration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('email-configuration-detail.edit', {
            parent: 'email-configuration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-configuration/email-configuration-dialog.html',
                    controller: 'EmailConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmailConfiguration', function(EmailConfiguration) {
                            return EmailConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email-configuration.new', {
            parent: 'email-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-configuration/email-configuration-dialog.html',
                    controller: 'EmailConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                password: null,
                                customEmail: null,
                                emailCompany: null,
                                adminCompanyName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('email-configuration', null, { reload: 'email-configuration' });
                }, function() {
                    $state.go('email-configuration');
                });
            }]
        })
        .state('email-configuration.edit', {
            parent: 'email-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-configuration/email-configuration-dialog.html',
                    controller: 'EmailConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmailConfiguration', function(EmailConfiguration) {
                            return EmailConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email-configuration', null, { reload: 'email-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email-configuration.delete', {
            parent: 'email-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-configuration/email-configuration-delete-dialog.html',
                    controller: 'EmailConfigurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmailConfiguration', function(EmailConfiguration) {
                            return EmailConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email-configuration', null, { reload: 'email-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
