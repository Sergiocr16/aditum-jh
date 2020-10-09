(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('block-reservation', {
            parent: 'entity',
            url: '/block-reservation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.blockReservation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/block-reservation/block-reservations.html',
                    controller: 'BlockReservationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blockReservation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('block-reservation-detail', {
            parent: 'block-reservation',
            url: '/block-reservation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.blockReservation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/block-reservation/block-reservation-detail.html',
                    controller: 'BlockReservationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blockReservation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BlockReservation', function($stateParams, BlockReservation) {
                    return BlockReservation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'block-reservation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('block-reservation-detail.edit', {
            parent: 'block-reservation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-reservation/block-reservation-dialog.html',
                    controller: 'BlockReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlockReservation', function(BlockReservation) {
                            return BlockReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('block-reservation.new', {
            parent: 'block-reservation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-reservation/block-reservation-dialog.html',
                    controller: 'BlockReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                blocked: null,
                                comments: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('block-reservation', null, { reload: 'block-reservation' });
                }, function() {
                    $state.go('block-reservation');
                });
            }]
        })
        .state('block-reservation.edit', {
            parent: 'block-reservation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-reservation/block-reservation-dialog.html',
                    controller: 'BlockReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlockReservation', function(BlockReservation) {
                            return BlockReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('block-reservation', null, { reload: 'block-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('block-reservation.delete', {
            parent: 'block-reservation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-reservation/block-reservation-delete-dialog.html',
                    controller: 'BlockReservationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BlockReservation', function(BlockReservation) {
                            return BlockReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('block-reservation', null, { reload: 'block-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
