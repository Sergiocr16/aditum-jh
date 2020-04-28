(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('houses-ar', {
                parent: 'entity',
                url: '/dwellings',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_MANAGER_MACRO'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/house-security-direction/houses-ar.html',
                        controller: 'HouseARController',
                        controllerAs: 'vm',
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
            .state('house-security-direction', {
                parent: 'entity',
                url: '/house-security-direction',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.houseSecurityDirection.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house-security-direction/house-security-directions.html',
                        controller: 'HouseSecurityDirectionController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('houseSecurityDirection');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('house-security-direction-detail', {
                parent: 'house-security-direction',
                url: '/house-security-direction/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.houseSecurityDirection.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/house-security-direction/house-security-direction-detail.html',
                        controller: 'HouseSecurityDirectionDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('houseSecurityDirection');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'HouseSecurityDirection', function ($stateParams, HouseSecurityDirection) {
                        return HouseSecurityDirection.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'house-security-direction',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('house-security-direction-detail.edit', {
                parent: 'house-security-direction-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/house-security-direction/house-ar-dialog.html',
                        controller: 'HouseSecurityDirectionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['HouseSecurityDirection', function (HouseSecurityDirection) {
                                return HouseSecurityDirection.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('houses-ar.new', {
                parent: 'houses-ar',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/house-security-direction/house-ar-dialog.html',
                        controller: 'HouseARDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            housenumber: null,
                            extension: null,
                            isdesocupated: null,
                            hasOwner: null,
                            securityKey: null,
                            emergencyKey: null,
                            companyId: null,
                            id: null,
                            ubication: {
                                id: null,
                                directionDescription: null,
                                latitude: null,
                                longitude: null,
                                houseDescription: null,
                                aditionalNotes: null,
                                smallDirection: null,
                                housePictureUrl: null,
                                houseId: null,
                                companyId: null
                            }
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
            .state('houses-ar.edit', {
                parent: 'houses-ar',
                url: '{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/house-security-direction/house-ar-dialog.html',
                        controller: 'HouseARDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['HouseSecurityDirection', 'CommonMethods', '$stateParams', function (HouseSecurityDirection, CommonMethods, $stateParams) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id);
                        return HouseSecurityDirection.get({id: id}).$promise;
                    }]
                }
            })
            .state('house-security-direction.edit', {
                parent: 'house-security-direction',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/house-security-direction/house-ar-dialog.html',
                        controller: 'HouseSecurityDirectionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['HouseSecurityDirection', function (HouseSecurityDirection) {
                                return HouseSecurityDirection.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('house-security-direction', null, {reload: 'house-security-direction'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('house-security-direction.delete', {
                parent: 'house-security-direction',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/house-security-direction/house-security-direction-delete-dialog.html',
                        controller: 'HouseSecurityDirectionDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['HouseSecurityDirection', function (HouseSecurityDirection) {
                                return HouseSecurityDirection.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('house-security-direction', null, {reload: 'house-security-direction'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
