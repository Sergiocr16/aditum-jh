(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('admin-ar', {
                parent: 'entity',
                url: '/admin-ar-by-company?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER_MACRO','ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/admin-ar/admin-ar.html',
                        controller: 'AdminsByCompanyARController',
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
                    }]
                }
            })
            .state('admin-ar-detail', {
                parent: 'admin-ar',
                url: '/admin-ar/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_RH','ROLE_MANAGER_MACRO'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/admin-ar/admin-ar-detail.html',
                        controller: 'AdminARDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adminInfo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AdminInfo', 'CommonMethods', function ($stateParams, AdminInfo, CommonMethods) {
                        var adminInfoId = CommonMethods.decryptIdUrl($stateParams.id);
                        return AdminInfo.get({id: adminInfoId}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'admin-info',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('admin-ar.new', {
                parent: 'admin-ar',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_MANAGER_MACRO']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/admin-ar/admin-ar-dialog.html',
                        controller: 'AdminARDialogController',
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
                            email: null,
                            image: null,
                            imageContentType: null,
                            enabled: null,
                            id: null
                        };
                    },
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
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
            .state('admin-ar.edit', {
                parent: 'admin-ar',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER_MACRO'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/admin-ar/admin-ar-dialog.html',
                        controller: 'AdminARDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AdminInfo', 'CommonMethods', function ($stateParams, AdminInfo, CommonMethods) {
                        return AdminInfo.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'admin-info',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })

    }

})();
