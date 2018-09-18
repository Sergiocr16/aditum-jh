(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('administration-configuration', {
            parent: 'entity',
            url: '/administration-configuration?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.administrationConfiguration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/administration-configuration/administration-configurations.html',
                    controller: 'AdministrationConfigurationController',
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
                    $translatePartialLoader.addPart('administrationConfiguration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('administration-configuration-detail', {
            parent: 'entity',
            url: '/administration-config',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.administrationConfiguration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/administration-configuration/administration-configuration-detail.html',
                    controller: 'AdministrationConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('administrationConfiguration');
                    return $translate.refresh();
                }],
                entity: ['$rootScope', 'AdministrationConfiguration', function($rootScope, AdministrationConfiguration) {
                    return AdministrationConfiguration.get({companyId : $rootScope.companyId}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'administration-configuration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('administration-configuration-detail.edit', {
            parent: 'administration-configuration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administration-configuration/administration-configuration-dialog.html',
                    controller: 'AdministrationConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdministrationConfiguration', function(AdministrationConfiguration) {
                            return AdministrationConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('administration-configuration.new', {
            parent: 'administration-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administration-configuration/administration-configuration-dialog.html',
                    controller: 'AdministrationConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                squareMetersPrice: null,
                                folioSerie: null,
                                folioNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('administration-configuration', null, { reload: 'administration-configuration' });
                }, function() {
                    $state.go('administration-configuration');
                });
            }]
        })
        .state('administration-configuration.edit', {
            parent: 'administration-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administration-configuration/administration-configuration-dialog.html',
                    controller: 'AdministrationConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdministrationConfiguration', function(AdministrationConfiguration) {
                            return AdministrationConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('administration-configuration', null, { reload: 'administration-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('administration-configuration.delete', {
            parent: 'administration-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administration-configuration/administration-configuration-delete-dialog.html',
                    controller: 'AdministrationConfigurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AdministrationConfiguration', function(AdministrationConfiguration) {
                            return AdministrationConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('administration-configuration', null, { reload: 'administration-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
