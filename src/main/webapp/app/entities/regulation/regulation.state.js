(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('regulation', {
                parent: 'entity',
                url: '/regulation?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_OWNER', 'ROLE_JD'],
                    pageTitle: 'aditumApp.regulation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/regulation/regulations.html',
                        controller: 'RegulationController',
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
                        $translatePartialLoader.addPart('regulation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('regulation-detail', {
                parent: 'regulation',
                url: '/regulation/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD', 'ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.regulation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/regulation/regulation-detail.html',
                        controller: 'RegulationDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('regulation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Regulation', function ($stateParams, Regulation) {
                        return Regulation.completeRegulationInfo({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'regulation',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('regulation-detail.edit', {
                parent: 'regulation-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/regulation/regulation-dialog.html',
                        controller: 'RegulationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Regulation', function (Regulation) {
                                return Regulation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('regulation.new', {
                parent: 'regulation',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/regulation/regulation-dialog.html',
                        controller: 'RegulationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    deleted: null,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('regulation', null, {reload: 'regulation'});
                    }, function () {
                        $state.go('regulation');
                    });
                }]
            })
            .state('regulation-admin-new', {
                parent: 'regulation',
                url: '/newadmin',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/regulation/regulation-dialog-admin.html',
                        controller: 'RegulationDialogAdminController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    deleted: null,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('regulation', null, {reload: 'regulation'});
                    }, function () {
                        $state.go('regulation');
                    });
                }]
            })
            .state('regulation.edit', {
                parent: 'regulation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/regulation/regulation-dialog.html',
                        controller: 'RegulationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Regulation', function (Regulation) {
                                return Regulation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('regulation', null, {reload: 'regulation'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            }).state('regulation-admin-edit', {
            parent: 'regulation',
            url: '/{id}/editadmin',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/regulation/regulation-dialog-admin.html',
                    controller: 'RegulationDialogAdminController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Regulation', function (Regulation) {
                            return Regulation.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('regulation', null, {reload: 'regulation'});
                }, function () {
                    $state.go('^');
                });
            }]
        })
            .state('regulation-search-tabs', {
                parent: 'entity',
                url: '/regulation',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD', 'ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/regulation/regulation-search-tabs.html',
                        controller: 'RegulationSearchTabsController',
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
                        $translatePartialLoader.addPart('regulation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('regulation-search-tabs.searchChapters', {
                url: '/regulation-search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD', 'ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.regulation.home.title'
                },
                templateUrl: 'app/entities/regulation/regulation-search.html',
                controller: 'RegulationSearchController',
                controllerAs: 'vm',
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
                        $translatePartialLoader.addPart('regulation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('regulation-search-tabs.byCategories', {
                url: '/regulation-search-by-categories',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD', 'ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.regulation.home.title'
                },
                templateUrl: 'app/entities/regulation/regulation-search-by-categories.html',
                controller: 'RegulationSearchByCategoriesController',
                controllerAs: 'vm',
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
                        $translatePartialLoader.addPart('regulation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

    }

})();
