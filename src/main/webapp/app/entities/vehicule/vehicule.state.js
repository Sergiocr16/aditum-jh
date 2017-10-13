(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vehicule', {
            parent: 'entity',
            url: '/vehicule?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vehicule/vehicules.html',
                    controller: 'VehiculeController',
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
                    $translatePartialLoader.addPart('vehicule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
                .state('vehiculeByHouse', {
                    parent: 'entity',
                    url: '/vehiculeByHouse?page&sort&search',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/vehicule/vehicule-by-house.html',
                            controller: 'VehiculeByHouseController',
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
                            $translatePartialLoader.addPart('vehicule');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    }
                })
        .state('vehicule-detail', {
            parent: 'vehicule',
            url: '/vehicule/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vehicule/vehicule-detail.html',
                    controller: 'VehiculeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vehicule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Vehicule', function($stateParams, Vehicule) {
                    return Vehicule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vehicule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vehicule-detail.edit', {
            parent: 'vehicule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicule/vehicule-dialog.html',
                    controller: 'VehiculeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vehicule', function(Vehicule) {
                            return Vehicule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
               .state('vehicule.new', {
                  parent: 'vehicule',
                            url: '/new',
                            data: {
                              authorities: ['ROLE_ADMIN','ROLE_MANAGER']
                            },
                            views: {
                                'content@': {
                       templateUrl: 'app/entities/vehicule/vehicule-dialog.html',
                                        controller: 'VehiculeDialogController',
                                    controllerAs: 'vm'
                                }
                            },
                            resolve: {

                                  entity: function () {
                                    return {
                                        licenseplate: null,
                                        brand: null,
                                        color: null,
                                        enabled: null,
                                        id: null

                                    };
                                },
                                companyUser: ['MultiCompany',function(MultiCompany){
                                    return MultiCompany.getCurrentUserCompany()
                                }],
                                previousState: ["$state", function ($state) {
                                    var currentStateData = {
                                        name: $state.current.name || 'vehicule',
                                        params: $state.params,
                                        url: $state.href($state.current.name, $state.params)
                                    };
                                    return currentStateData;
                                }]
                            }
                })
                .state('vehiculeByHouse.new', {
                  parent: 'vehiculeByHouse',
                            url: '/new',
                            data: {
                              authorities: ['ROLE_USER']
                            },
                            views: {
                                'content@': {
                       templateUrl: 'app/entities/vehicule/vehicule-dialog.html',
                                        controller: 'VehiculeByHouseDialogController',
                                    controllerAs: 'vm'
                                }
                            },
                            resolve: {

                                  entity: function () {
                                    return {
                                        licenseplate: null,
                                        brand: null,
                                        color: null,
                                        enabled: null,
                                        id: null

                                    };
                                },
                                companyUser: ['MultiCompany',function(MultiCompany){
                                    return MultiCompany.getCurrentUserCompany()
                                }],
                                previousState: ["$state", function ($state) {
                                    var currentStateData = {
                                        name: $state.current.name || 'vehiculeByHouse',
                                        params: $state.params,
                                        url: $state.href($state.current.name, $state.params)
                                    };
                                    return currentStateData;
                                }]
                            }
                })
         .state('vehicule.edit', {
                 parent: 'vehicule',
                    url: '/{id}/edit',
                    data: {
                     authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                    },
                    views: {
                        'content@': {
                          templateUrl: 'app/entities/vehicule/vehicule-dialog.html',
                            controller: 'VehiculeDialogController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                            entity: ['$stateParams', 'Vehicule','CommonMethods', function($stateParams, Vehicule, CommonMethods) {
                             var id = CommonMethods.decryptIdUrl($stateParams.id)
                                return Vehicule.get({id : id}).$promise;
                            }],
                        previousState: ["$state", function ($state) {
                            var currentStateData = {
                                name: $state.current.name || 'vehicule',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }

          })
       .state('vehiculeByHouse.edit', {
                 parent: 'vehiculeByHouse',
                    url: '/{id}/edit',
                    data: {
                     authorities: ['ROLE_USER']
                    },
                    views: {
                        'content@': {
                          templateUrl: 'app/entities/vehicule/vehicule-dialog.html',
                            controller: 'VehiculeByHouseDialogController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                            entity: ['$stateParams', 'Vehicule','CommonMethods', function($stateParams, Vehicule,CommonMethods) {
                                var id = CommonMethods.decryptIdUrl($stateParams.id)
                                return Vehicule.get({id : id}).$promise;
                            }],
                        previousState: ["$state", function ($state) {
                            var currentStateData = {
                                name: $state.current.name || 'vehiculeByHouse',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }

          })


        .state('vehicule.delete', {
            parent: 'vehicule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicule/vehicule-delete-dialog.html',
                    controller: 'VehiculeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vehicule', function(Vehicule) {
                            return Vehicule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vehicule', null, { reload: 'vehicule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
