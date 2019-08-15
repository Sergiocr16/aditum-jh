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
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO', 'ROLE_USER'],
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
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
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
                    authorities: ['ROLE_MANAGER','ROLE_JD', 'ROLE_MANAGER_MACRO'],

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

            .state('visitant-detail', {
                parent: 'visitant',
                url: '/visitant/{id}',
                data: {
                    authorities: ['ROLE_USER'],
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


            .state('visitant-invited-user.new-list', {
                parent: 'visitant-invited-user',
                url: 'new-party',
                data: {
                    authorities: ['ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant/visitant-invite-list-dialog.html',
                        controller: 'VisitantInviteListDialogController',
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
            });
    }
})();
