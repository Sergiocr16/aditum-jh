(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('officer', {
                parent: 'entity',
                url: '/officer?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_JD']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/officer/officers.html',
                        controller: 'OfficerController',
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
                        $translatePartialLoader.addPart('officer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
           .state('officer-rh', {
                parent: 'entity',
                url: '/officer-rh?page&sort&search',
                data: {
                    authorities: ['ROLE_RH']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/officer/officers-rh.html',
                        controller: 'OfficerRHController',
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
                        $translatePartialLoader.addPart('officer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('officer.details', {
                parent: 'officer',
                url: '/{id}/details',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_RH', 'ROLE_JD']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/officer/officer-details.html',
                        controller: 'OfficerDetailsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Officer','CommonMethods', function($stateParams, Officer, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Officer.get({
                            id: id
                        }).$promise;
                    }],
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'officer',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })


            .state('officer-detail.edit', {
                parent: 'officer-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_RH']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/officer/officer-dialog.html',
                        controller: 'OfficerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Officer', function(Officer) {
                                return Officer.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('^', {}, {
                            reload: false
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('officer.new', {
                parent: 'officer',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_RH']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/officer/officer-dialog.html',
                        controller: 'OfficerDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                    entity: function() {
                        return {
                            name: null,
                            lastname: null,
                            secondlastname: null,
                            image: null,
                            imageContentType: null,
                            email: null,
                            identificationnumber: null,
                            inservice: null,
                            id: null
                        };
                    },
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'officer',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]

                }
            })
            .state('officer.edit', {
                parent: 'officer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_RH']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/officer/officer-dialog.html',
                        controller: 'OfficerDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Officer','CommonMethods', function($stateParams, Officer, CommonMethods) {
                    var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Officer.get({
                            id: id
                        }).$promise;
                    }],
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'officer',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            });
    }

})();
