(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reservation-house-restrictions', {
            parent: 'entity',
            url: '/reservation-house-restrictions',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.reservationHouseRestrictions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reservation-house-restrictions/reservation-house-restrictions.html',
                    controller: 'ReservationHouseRestrictionsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reservationHouseRestrictions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reservation-house-restrictions-detail', {
            parent: 'reservation-house-restrictions',
            url: '/reservation-house-restrictions/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.reservationHouseRestrictions.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reservation-house-restrictions/reservation-house-restrictions-detail.html',
                    controller: 'ReservationHouseRestrictionsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reservationHouseRestrictions');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReservationHouseRestrictions', function($stateParams, ReservationHouseRestrictions) {
                    return ReservationHouseRestrictions.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reservation-house-restrictions',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reservation-house-restrictions-detail.edit', {
            parent: 'reservation-house-restrictions-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation-house-restrictions/reservation-house-restrictions-dialog.html',
                    controller: 'ReservationHouseRestrictionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReservationHouseRestrictions', function(ReservationHouseRestrictions) {
                            return ReservationHouseRestrictions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reservation-house-restrictions.new', {
            parent: 'reservation-house-restrictions',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation-house-restrictions/reservation-house-restrictions-dialog.html',
                    controller: 'ReservationHouseRestrictionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reservationQuantity: null,
                                lastTimeReservation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reservation-house-restrictions', null, { reload: 'reservation-house-restrictions' });
                }, function() {
                    $state.go('reservation-house-restrictions');
                });
            }]
        })
        .state('reservation-house-restrictions.edit', {
            parent: 'reservation-house-restrictions',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation-house-restrictions/reservation-house-restrictions-dialog.html',
                    controller: 'ReservationHouseRestrictionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReservationHouseRestrictions', function(ReservationHouseRestrictions) {
                            return ReservationHouseRestrictions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reservation-house-restrictions', null, { reload: 'reservation-house-restrictions' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reservation-house-restrictions.delete', {
            parent: 'reservation-house-restrictions',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation-house-restrictions/reservation-house-restrictions-delete-dialog.html',
                    controller: 'ReservationHouseRestrictionsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReservationHouseRestrictions', function(ReservationHouseRestrictions) {
                            return ReservationHouseRestrictions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reservation-house-restrictions', null, { reload: 'reservation-house-restrictions' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
