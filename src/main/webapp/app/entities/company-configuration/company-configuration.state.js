(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-configuration', {
            parent: 'entity',
            url: '/company-configuration?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.companyConfiguration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-configuration/company-configurations.html',
                    controller: 'CompanyConfigurationController',
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
                    $translatePartialLoader.addPart('companyConfiguration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-configuration-detail', {
            parent: 'company-configuration',
            url: '/company-configuration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.companyConfiguration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-configuration/company-configuration-detail.html',
                    controller: 'CompanyConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyConfiguration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanyConfiguration', function($stateParams, CompanyConfiguration) {
                    return CompanyConfiguration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-configuration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-configuration-detail.edit', {
            parent: 'company-configuration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-configuration/company-configuration-dialog.html',
                    controller: 'CompanyConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyConfiguration', function(CompanyConfiguration) {
                            return CompanyConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-configuration.new', {
            parent: 'company-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-configuration/company-configuration-dialog.html',
                    controller: 'CompanyConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantityhouses: null,
                                quantityadmins: null,
                                quantityaccessdoor: null,
                                hasContability: null,
                                minDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-configuration', null, { reload: 'company-configuration' });
                }, function() {
                    $state.go('company-configuration');
                });
            }]
        })
        .state('company-configuration.edit', {
            parent: 'company-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-configuration/company-configuration-dialog.html',
                    controller: 'CompanyConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyConfiguration', function(CompanyConfiguration) {
                            return CompanyConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-configuration', null, { reload: 'company-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-configuration.delete', {
            parent: 'company-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-configuration/company-configuration-delete-dialog.html',
                    controller: 'CompanyConfigurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyConfiguration', function(CompanyConfiguration) {
                            return CompanyConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-configuration', null, { reload: 'company-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
