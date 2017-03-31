(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('visitant', {
                parent: 'entity',
                url: '/visitant?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.visitant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant/visitants.html',
                        controller: 'VisitantController',
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
                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('visitant-admin', {
                parent: 'entity',
                url: '/visitant/manage/?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.visitant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant/visitants-admin.html',
                        controller: 'VisitantAdminController',
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
                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('visitant-invited-user', {
                parent: 'entity',
                url: '/visitant/invited/user/?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.visitant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant/visitants-user-invited.html',
                        controller: 'VisitantInvitedUserController',
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
                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('visitant-detail', {
                parent: 'visitant',
                url: '/visitant/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.visitant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant/visitant-detail.html',
                        controller: 'VisitantDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Visitant', function($stateParams, Visitant) {
                        return Visitant.get({
                            id: $stateParams.id
                        }).$promise;
                    }],
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'visitant',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('visitant-invited-user.edit', {
                parent: 'visitant-invited-user',
                url: 'renew/:id',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', 'CommonMethods', function($stateParams, $state, $uibModal, CommonMethods) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant/visitant-dialog-renew.html',
                        controller: 'VisitantDialogRenewController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Visitant', function(Visitant) {
                                var visitorId = CommonMethods.decryptIdUrl($stateParams.id)
                                return Visitant.get({
                                    id: visitorId
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('^', {}, {
                            reload: true
                        });
                    }, function() {
                        $state.go('^', {}, {
                            reload: true
                        });
                    });
                }]
            })
            .state('visitant-invited-user.new', {
                parent: 'visitant-invited-user',
                url: 'new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.visitant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant/visitant-dialog.html',
                        controller: 'VisitantDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        return $translate.refresh();
                    }],
                    //                 entity: ['$stateParams', 'Visitant', function($stateParams, Visitant) {
                    //                     return Visitant.get({id : $stateParams.id}).$promise;
                    //                 }],
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'visitant',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })
            .state('visitant.edit', {
                parent: 'visitant',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant/visitant-dialog.html',
                        controller: 'VisitantDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Visitant', function(Visitant) {
                                return Visitant.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('visitant', null, {
                            reload: 'visitant'
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('visitant.delete', {
                parent: 'visitant',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant/visitant-delete-dialog.html',
                        controller: 'VisitantDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Visitant', function(Visitant) {
                                return Visitant.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('visitant', null, {
                            reload: 'visitant'
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }
})();
