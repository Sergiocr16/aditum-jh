(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('admin-info', {
                parent: 'entity',
                url: '/admin-infos?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/admin-info/admin-infos.html',
                        controller: 'AdminInfoController',
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
            .state('admin-info-by-company', {
                parent: 'entity',
                url: '/admin-infos-by-company?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER_MACRO','ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/admin-info/admins-by-company.html',
                        controller: 'AdminsByCompanyController',
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

            .state('admin-info-edit', {
                parent: 'entity',
                url: '/admin-info',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/admin-info/admin-info-edit.html',
                        controller: 'AdminInfoEditDialogController',
                        controllerAs: 'vm'
                    }
                }, resolve: {
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                }
            })

            .state('admin-info-detail', {
                parent: 'admin-info',
                url: '/admin-info/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_RH','ROLE_MANAGER_MACRO'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/admin-info/admin-info-detail.html',
                        controller: 'AdminInfoDetailController',
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
            .state('admin-info-detail.edit', {
                parent: 'admin-info-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER_MACRO']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/admin-info/admin-info-dialog.html',
                        controller: 'AdminInfoDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['AdminInfo', function (AdminInfo) {
                                return AdminInfo.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('admin-info.new', {
                parent: 'admin-info',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_MANAGER_MACRO']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/admin-info/admin-info-dialog.html',
                        controller: 'AdminInfoDialogController',
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

            .state('admin-info.edit', {

                parent: 'admin-info',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER_MACRO'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/admin-info/admin-info-dialog.html',
                        controller: 'AdminInfoDialogController',
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
