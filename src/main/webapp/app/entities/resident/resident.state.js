(function() {
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
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.resident.home.title'
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
        .state('resident-detail', {
            parent: 'resident',
            url: '/resident/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.resident.detail.title'
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
                entity: ['$stateParams', 'Resident', function($stateParams, Resident) {
                    return Resident.get({id : $stateParams.id}).$promise;
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
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resident/resident-dialog.html',
                    controller: 'ResidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resident', function(Resident) {
                            return Resident.get({id : $stateParams.id}).$promise;
                        }],
                        companyUser: ['MultiCompany',function(MultiCompany){
                                         return MultiCompany.getCurrentUserCompany()
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resident.new', {
          parent: 'resident',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_MANAGER'],
                        pageTitle: 'aditumApp.resident.detail.title'
                    },
                    views: {
                        'content@': {
               templateUrl: 'app/entities/resident/resident-form.html',
                                controller: 'ResidentDialogController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('resident');
                            return $translate.refresh();
                        }],
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
                                id: null

                            };
                        },
                        companyUser: ['MultiCompany',function(MultiCompany){
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
//            parent: 'resident',
//            url: '/new',
//            data: {
//                authorities: ['ROLE_MANAGER']
//            } ,
//                   onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//            views: {
//                             'content@': {
//                                 templateUrl: 'app/entities/resident/resident-form.html',
//                                 controller: 'ResidentDialogController',
//                                 controllerAs: 'vm'
//                             }
//                         }
//                            }]
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/entities/resident/resident-form.html',
//                    controller: 'ResidentDialogController',
//                    controllerAs: 'vm',
//                    backdrop: 'static',
//                    size: 'lg',
//                    resolve: {
//                        entity: function () {
//                            return {
//                                name: null,
//                                lastname: null,
//                                secondlastname: null,
//                                identificationnumber: null,
//                                phonenumber: null,
//                                image: null,
//                                imageContentType: null,
//                                email: null,
//                                isOwner: null,
//                                enabled: null,
//                                id: null
//                            };
//                        }
//                    }
//                }).result.then(function() {
//                    $state.go('resident', null, { reload: 'resident' });
//                }, function() {
//                    $state.go('resident');
//                });
//            }]
        })
        .state('resident.edit', {
            parent: 'resident',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resident/resident-dialog.html',
                    controller: 'ResidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resident', function(Resident) {
                            return Resident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resident', null, { reload: 'resident' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resident.delete', {
            parent: 'resident',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resident/resident-delete-dialog.html',
                    controller: 'ResidentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resident', function(Resident) {
                            return Resident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resident', null, { reload: 'resident' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
