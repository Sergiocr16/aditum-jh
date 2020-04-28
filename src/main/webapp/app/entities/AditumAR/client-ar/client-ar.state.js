(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('client-ar', {
                parent: 'entity',
                url: '/client-ar?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/client-ar/client-ar-index.html',
                        controller: 'ClientARController',
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
                        $translatePartialLoader.addPart('resident');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('client-ar-detail', {
                parent: 'entity',
                url: '/client-ar/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER_MACRO', 'ROLE_JD'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/client-ar/client-ar-detail.html',
                        controller: 'ClientArDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('resident');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Resident', 'CommonMethods', function ($stateParams, Resident, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Resident.get({id: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'resident',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('client-ar.new', {
                parent: 'client-ar',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/client-ar/client-ar-dialog.html',
                        controller: 'ClientARDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            lastname: null,
                            secondlastname: null,
                            identificationnumber: null,
                            phonenumber: null,
                            image: null,
                            imageContentType: null,
                            email: null,
                            isOwner: 0,
                            enabled: 1,
                            id: null,
                            principalContact: "0"
                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'resident',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('client-ar.edit', {
                parent: 'client-ar',
                url: '/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/client-ar/client-ar-dialog.html',
                        controller: 'ClientARDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Resident', 'CommonMethods', function ($stateParams, Resident, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Resident.get({id: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'resident',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
    }

})();
