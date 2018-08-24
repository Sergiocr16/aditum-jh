(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('common-area-reservations', {
            parent: 'entity',
            url: '/common-area-reservations?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.commonAreaReservations.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations.html',
                    controller: 'CommonAreaReservationsController',
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
                    $translatePartialLoader.addPart('commonAreaReservations');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('common-area-reservations-detail', {
            parent: 'common-area-reservations',
            url: '/common-area-reservations/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.commonAreaReservations.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-detail.html',
                    controller: 'CommonAreaReservationsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commonAreaReservations');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CommonAreaReservations', function($stateParams, CommonAreaReservations) {
                    return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'common-area-reservations',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('common-area-reservations-detail.edit', {
            parent: 'common-area-reservations-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                    controller: 'CommonAreaReservationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                            return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area-reservations.new', {
            parent: 'common-area-reservations',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                    controller: 'CommonAreaReservationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                houseId: null,
                                residentId: null,
                                date: null,
                                initialTime: null,
                                finalTime: null,
                                comments: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('common-area-reservations', null, { reload: 'common-area-reservations' });
                }, function() {
                    $state.go('common-area-reservations');
                });
            }]
        })
        .state('common-area-reservations.edit', {
            parent: 'common-area-reservations',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                    controller: 'CommonAreaReservationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                            return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area-reservations', null, { reload: 'common-area-reservations' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area-reservations.delete', {
            parent: 'common-area-reservations',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-delete-dialog.html',
                    controller: 'CommonAreaReservationsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                            return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area-reservations', null, { reload: 'common-area-reservations' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
