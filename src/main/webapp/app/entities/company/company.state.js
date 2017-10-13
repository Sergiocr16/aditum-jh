(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company', {
            parent: 'entity',
            url: '/company?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],

            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/companies.html',
                    controller: 'CompanyController',
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
                    $translatePartialLoader.addPart('company');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-rh', {
            parent: 'entity',
            url: '/company-rh?page&sort&search',
            data: {
                authorities: ['ROLE_RH'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/companies-rh.html',
                    controller: 'CompanyRHController',
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
                    $translatePartialLoader.addPart('company');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-detail', {
            parent: 'company',
            url: '/company/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/company-detail.html',
                    controller: 'CompanyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('company');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Company', function($stateParams, Company) {
                    return Company.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-detail.edit', {
            parent: 'company-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company.new', {
            parent: 'company',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-dialog.html',
                    controller: 'CompanyDialogController',
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
                    $state.go('company', null, { reload: 'company' });
                }, function() {
                    $state.go('company');
                });
            }]
        })
        .state('company.edit', {
            parent: 'company',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: 'company' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('company-rh.edit', {
                parent: 'company-rh',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_RH']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/company/company-rh-dialog.html',
                        controller: 'CompanyRhDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Company','CommonMethods', function(Company,CommonMethods) {
                              var id = CommonMethods.decryptIdUrl($stateParams.id)
                                return Company.get({id : id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('company-rh', null, { reload: 'company-rh' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('companyConfiguration', {
                parent: 'company',
                url: '/configuration/{id}',
                data: {
                    authorities: ['ROLE_ADMIN']
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
                        $state.go('company', null, { reload: 'company' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })

              .state('company.editOfficerAccount', {
                         parent: 'company',
                         url: '/officer-account/{id}/edit',
                         data: {
                                   authorities: ['ROLE_ADMIN'],
                         },
                         onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                             $uibModal.open({
                                 templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                                 controller: 'OfficerAccountDialogController',
                                 controllerAs: 'vm',
                                 backdrop: 'static',
                                 size: 'lg',
                                 resolve: {
                                     entity: ['OfficerAccount', function(OfficerAccount) {
                                         return OfficerAccount.get({id : $stateParams.id}).$promise;
                                     }]
                                 }
                             }).result.then(function() {
                                 $state.go('company', null, { reload: 'company' });
                             }, function() {
                                 $state.go('^');
                             });
                         }]
                     })
            .state('admins', {
                parent: 'company',
                url: '/admins/:companyId',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_RH']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/admin-info/admins-by-company.html',
                        controller: 'AdminsByCompanyController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',

                    }).result.then(function() {

                    }, function() {

                    });
                }]
            })
           .state('admins-rh', {
                parent: 'company-rh',
                url: '/admins-rh/:companyId',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_RH']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/admin-info/admins-by-company.html',
                        controller: 'AdminsByCompanyController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                    }).result.then(function() {

                    }, function() {

                    });
                }]
            })

        .state('company.delete', {
            parent: 'company',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-delete-dialog.html',
                    controller: 'CompanyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: 'company' });
                }, function() {
                    $state.go('^');
                });
            }]
        });

    }

})();
