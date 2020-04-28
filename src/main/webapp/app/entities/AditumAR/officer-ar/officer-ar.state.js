(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('officer-ar', {
                parent: 'entity',
                url: '/officer-ar?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar.html',
                        controller: 'OfficerARController',
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
            .state('officer-ar.details', {
                parent: 'officer-ar',
                url: '/{id}/details',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-details.html',
                        controller: 'OfficerARDetailsController',
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
            .state('officer-ar.new', {
                parent: 'officer-ar',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-dialog.html',
                        controller: 'OfficerARDialogController',
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
            .state('officer-ar.edit', {
                parent: 'officer-ar',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
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
