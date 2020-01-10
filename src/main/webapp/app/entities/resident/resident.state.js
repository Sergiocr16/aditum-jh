(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('resident', {
                parent: 'entity',
                url: '/resident?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-index.html',
                        controller: 'ResidentController',
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
            .state('owner', {
                parent: 'entity',
                url: '/owner?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO','ROLE_JD'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/owner/owner-index.html',
                        controller: 'OwnerController',
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
            .state('tenant', {
                parent: 'entity',
                url: '/tenant?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO','ROLE_JD','ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/tenant/tenant-index.html',
                        controller: 'TenantController',
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
            .state('residentByHouse.residentDetail', {
                parent: 'residentByHouse',
                url: '/detalleResidenteVista?id2',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/resident/resident-detail-house-administration.html',
                        controller: 'ResidentDetailHouseAdministrationController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Resident', 'CommonMethods', function (Resident) {
                                return Resident.get({id: $stateParams.id2}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('resident-detail', {
                parent: 'resident',
                url: '/resident/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_JD'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-detail.html',
                        controller: 'ResidentDetailController',
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
            .state('owner-detail', {
                parent: 'owner',
                url: '/owner/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_MANAGER_MACRO','ROLE_JD' ],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/owner/owner-detail.html',
                        controller: 'OwnerDetailController',
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
            .state('resident-detail.edit', {
                parent: 'resident-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/resident/resident-form.html',
                        controller: 'ResidentDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Resident', 'CommonMethods', function (Resident, CommonMethods) {
                                var id = CommonMethods.decryptIdUrl($stateParams.id)
                                return Resident.get({id: id}).$promise;
                            }],
                            companyUser: ['MultiCompany', function (MultiCompany) {
                                return MultiCompany.getCurrentUserCompany()
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('tenant.new', {
                parent: 'tenant',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER','ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/tenant/tenant-form.html',
                        controller: 'TenantDialogController',
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
                            isOwner: null,
                            enabled: 1,
                            id: null,
                            principalContact: "0"
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
            .state('resident.new', {
                parent: 'resident',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-form.html',
                        controller: 'ResidentDialogController',
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
                            isOwner: null,
                            enabled: 1,
                            id: null,
                            principalContact: "0"
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
            .state('owner.new', {
                parent: 'owner',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/owner/owner-form.html',
                        controller: 'OwnerDialogController',
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
            .state('residentByHouse.new', {
                parent: 'residentByHouse',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-form-resident-view.html',
                        controller: 'ResidentByHouseDialogController',
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
                            isOwner: null,
                            enabled: null,
                            id: null,
                            principalContact: "0"

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
            .state('resident.edit', {
                parent: 'resident',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-form.html',
                        controller: 'ResidentDialogController',
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
            .state('owner.edit', {

                parent: 'owner',
                url: '/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                views: {
                    'content@': {

                        templateUrl: 'app/entities/owner/owner-form.html',
                        controller: 'OwnerDialogController',
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
            .state('tenant.edit', {

                parent: 'tenant',
                url: '/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'],
                },
                views: {
                    'content@': {

                        templateUrl: 'app/entities/tenant/tenant-form.html',
                        controller: 'TenantDialogController',
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
            .state('residentByHouse.edit', {
                parent: 'residentByHouse',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-form-resident-view.html',
                        controller: 'ResidentByHouseDialogController',
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
                            name: $state.current.name || 'residentByHouse',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })
            .state('residentByHouse', {
                parent: 'entity',
                url: '/residents-by-subsidiary?page&sort&search',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/resident/resident-by-house.html',
                        controller: 'ResidentByHouseController',
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
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }]
                }
            });
    }

})();
