(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('security-company', {
            parent: 'entity',
            url: '/security-company?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.securityCompany.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-company/security-companies.html',
                    controller: 'SecurityCompanyController',
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
                    $translatePartialLoader.addPart('securityCompany');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-company-detail', {
            parent: 'security-company',
            url: '/security-company/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.securityCompany.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-company/security-company-detail.html',
                    controller: 'SecurityCompanyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityCompany');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityCompany', function($stateParams, SecurityCompany) {
                    return SecurityCompany.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'security-company',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('security-company-detail.edit', {
            parent: 'security-company-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-company/security-company-dialog.html',
                    controller: 'SecurityCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SecurityCompany', function(SecurityCompany) {
                            return SecurityCompany.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('security-company.new', {
            parent: 'security-company',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-company/security-company-dialog.html',
                    controller: 'SecurityCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('security-company', null, { reload: 'security-company' });
                }, function() {
                    $state.go('security-company');
                });
            }]
        })
        .state('security-company.edit', {
            parent: 'security-company',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-company/security-company-dialog.html',
                    controller: 'SecurityCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SecurityCompany', function(SecurityCompany) {
                            return SecurityCompany.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-company', null, { reload: 'security-company' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('security-company.delete', {
            parent: 'security-company',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-company/security-company-delete-dialog.html',
                    controller: 'SecurityCompanyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityCompany', function(SecurityCompany) {
                            return SecurityCompany.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-company', null, { reload: 'security-company' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
