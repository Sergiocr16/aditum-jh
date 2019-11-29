(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('junta-directiva-account', {
            parent: 'entity',
            url: '/junta-directiva-account?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.juntaDirectivaAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/junta-directiva-account/junta-directiva-accounts.html',
                    controller: 'JuntaDirectivaAccountController',
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
                    $translatePartialLoader.addPart('juntaDirectivaAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('junta-directiva-account-detail', {
            parent: 'junta-directiva-account',
            url: '/junta-directiva-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.juntaDirectivaAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/junta-directiva-account/junta-directiva-account-detail.html',
                    controller: 'JuntaDirectivaAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('juntaDirectivaAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'JuntaDirectivaAccount', function($stateParams, JuntaDirectivaAccount) {
                    return JuntaDirectivaAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'junta-directiva-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('junta-directiva-account-detail.edit', {
            parent: 'junta-directiva-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/junta-directiva-account/junta-directiva-account-dialog.html',
                    controller: 'JuntaDirectivaAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JuntaDirectivaAccount', function(JuntaDirectivaAccount) {
                            return JuntaDirectivaAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('junta-directiva-account.new', {
                parent: 'junta-directiva-account',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/junta-directiva-account/junta-directiva-account-dialog.html',
                        controller: 'JuntaDirectivaAccountDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                    entity: function () {
                        return {
                            email: null,
                            password: null,
                            id: null
                        };
                    },
                    companyUser: ['MultiCompany',function(MultiCompany){
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'junta-directiva-account',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
        //
        // .state('junta-directiva-account.new', {
        //     parent: 'junta-directiva-account',
        //     url: '/new',
        //     data: {
        //         authorities: ['ROLE_MANAGER']
        //     },
        //
        //
        //     onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'app/entities/junta-directiva-account/junta-directiva-account-dialog.html',
        //             controller: 'JuntaDirectivaAccountDialogController',
        //             controllerAs: 'vm',
        //             backdrop: 'static',
        //             size: 'lg',
        //             resolve: {
        //                 entity: function () {
        //                     return {
        //                         email: null,
        //                         id: null
        //                     };
        //                 }
        //             }
        //         }).result.then(function() {
        //             $state.go('junta-directiva-account', null, { reload: 'junta-directiva-account' });
        //         }, function() {
        //             $state.go('junta-directiva-account');
        //         });
        //     }]
        // })
        .state('junta-directiva-account.edit', {
            parent: 'junta-directiva-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/junta-directiva-account/junta-directiva-account-dialog.html',
                    controller: 'JuntaDirectivaAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JuntaDirectivaAccount', function(JuntaDirectivaAccount) {
                            return JuntaDirectivaAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('junta-directiva-account', null, { reload: 'junta-directiva-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('junta-directiva-account.delete', {
            parent: 'junta-directiva-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/junta-directiva-account/junta-directiva-account-delete-dialog.html',
                    controller: 'JuntaDirectivaAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JuntaDirectivaAccount', function(JuntaDirectivaAccount) {
                            return JuntaDirectivaAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('junta-directiva-account', null, { reload: 'junta-directiva-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
