(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('houses-tabs', {
                parent: 'entity',
                url: '/subsidiaries',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house/houses-tabs.html',
                        // controller: 'HouseDetailController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('houseAdministration', {
                parent: 'entity',
                url: '/contabilidad-filiales',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                views: {
                    'content@': {
                        // templateUrl: 'app/entities/house/house-administration.html',
                        templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'HouseAdministrationController',
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('houseAdministration.chargePerHouse', {
                url: '/cuotas',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                templateUrl: 'app/entities/charge/charge-per-house.html',
                controller: 'ChargePerHouseController',
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('houseAdministration.accountStatus', {
                url: '/estadoDeCuenta',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                templateUrl: 'app/entities/account-status/accountStatus.html',
                controller: 'AccountStatusController',
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('houseAdministration.residentsByHouse', {
                url: '/residentesPorFilial',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                templateUrl: 'app/entities/resident/residents-house-administration.html',
                controller: 'ResidentsHouseAdministrationController',
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('houseAdministration.vehiculesByHouse', {
                url: '/vehiculosPorFilial',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                templateUrl: 'app/entities/vehicule/vehicules-house-administration.html',
                controller: 'VehiculesHouseAdministrationController',
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('houseAdministration.residentsByHouse.residentDetail', {
                parent: 'houseAdministration.residentsByHouse',
                url: '/detalleResidente?id2',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
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

            .state('houseAdministration.chargePerHouse.new', {
                parent: 'houseAdministration.chargePerHouse',
                url: '/crear',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_ADMIN']
                },
                resolve: {},
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/charge/charge-house.html',
                        controller: 'HouseChargeController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['$localStorage', 'House', 'CommonMethods', function ($localStorage, House, CommonMethods) {
                                return House.get({id: $localStorage.houseSelected.id}).$promise;
                            }],
                        }
                    }).result.then(function () {
                        $state.go('houseAdministration.chargePerHouse', null, {reload: true});
                    }, function () {
                        $state.go('houseAdministration.chargePerHouse');
                    });
                }]
            })

            .state('houseAdministration.paymentsPerHouse', {
                url: '/pagos?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                templateUrl: 'app/entities/payment/payments-per-house.html',
                controller: 'PaymentsPerHouseController',
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
                        $translatePartialLoader.addPart('payment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('houses-tabs.house', {
                url: '/?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO'],
                },
                templateUrl: 'app/entities/house/house-index.html',
                controller: 'HouseController',
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
                        $translatePartialLoader.addPart('house');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('houses-tabs.detail', {
                parent: 'houses-tabs',
                url: '/detail/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house/house-detail.html',
                        controller: 'HouseDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('house');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'House', 'CommonMethods', function ($stateParams, House, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return House.get({
                            id: id
                        }).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'house',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('house-detail.edit', {
                parent: 'house-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/house/house-dialog.html',
                        controller: 'HouseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['House', function (House) {
                                return House.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {
                            reload: false
                        });
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('house.reportAbsence', {
                parent: 'entity',
                url: '/report/absence',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house/house-absence.html',
                        controller: 'HouseAbsenceController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'house',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('houses-tabs.new', {
                parent: 'entity',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house/house-form.html',
                        controller: 'HouseDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                    entity: function () {
                        return {
                            housenumber: null,
                            extension: null,
                            isdesocupated: null,
                            desocupationinitialtime: null,
                            desocupationfinaltime: null,
                            securityKey: null,
                            emergencyKey: null,
                            id: null,
                            subsidiaries: []
                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'house',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })
            .state('houses-tabs.edit', {
                parent: 'entity',
                url: '/all/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house/house-form.html',
                        controller: 'HouseDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'House', 'CommonMethods', function ($stateParams, House, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return House.get({id: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'house',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('keysConfiguration', {
                parent: 'entity',
                url: '/keysConfiguration',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house/keyConfiguration.html',
                        controller: 'KeyConfigurationController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                    entity: function () {
                        return {
                            housenumber: null,
                            extension: null,
                            isdesocupated: null,
                            desocupationinitialtime: null,
                            desocupationfinaltime: null,
                            securityKey: null,
                            emergencyKey: null,
                            id: null
                        };
                    },
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || '',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            });
    }

})();
