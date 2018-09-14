(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('common-area-administration', {
                parent: 'entity',
                url: '/common-area-administration',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area/common-area-administration.html',
                        controller: 'CommonAreaAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('common-area-resident-account', {
                parent: 'entity',
                url: '/common-area-resident-account?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.commonArea.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area/common-area-resident-account.html',
                        controller: 'CommonAreaController',
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
                        $translatePartialLoader.addPart('commonArea');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('common-area', {
                url: '/common-area?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.commonArea.home.title'
                },
                templateUrl: 'app/entities/common-area/common-areas.html',
                controller: 'CommonAreaController',
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
                        $translatePartialLoader.addPart('commonArea');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('common-area-detail', {
                parent: 'common-area',
                url: '/common-area/{id}',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.commonArea.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area/common-area-detail.html',
                        controller: 'CommonAreaDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('commonArea');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CommonArea', function ($stateParams, CommonArea) {
                        return CommonArea.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area-detail.edit', {
                parent: 'common-area-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area/common-area-dialog.html',
                        controller: 'CommonAreaDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonArea', function (CommonArea) {
                                return CommonArea.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.reservation-calendar.reservationDetail', {
                parent: 'common-area-administration.reservation-calendar',
                url: '/{id}/reservation-detail?id2',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-detail.html',
                        controller: 'ReservationCalendarReservationDetailController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id2}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-administration.reservation-calendar.reservationDetail', null, { reload: 'common-area-administration.reservation-calendar.reservationDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.reservation-calendar.acceptedReservationsDetail', {
                parent: 'common-area-administration.reservation-calendar',
                url: '/{id}/accepted-reservation-detail?id2',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/reservation-calendar-accepted-reservations.html',
                        controller: 'ReservationsCalentarAcceptedReservations',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id2}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-administration.reservation-calendar.reservationDetail', null, { reload: 'common-area-administration.reservation-calendar.reservationDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.general-reservation-calendar.acceptedReservationsDetail', {
                parent: 'common-area-administration.general-reservation-calendar',
                url: '/{id}/accepted-reservation-detail',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/reservation-calendar-accepted-reservations.html',
                        controller: 'ReservationsCalentarAcceptedReservations',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-administration.reservation-calendar.reservationDetail', null, { reload: 'common-area-administration.reservation-calendar.reservationDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.general-reservation-calendar.reservationDetail', {
                parent: 'common-area-administration.general-reservation-calendar',
                url: '/{id}/reservation-detail',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-detail.html',
                        controller: 'ReservationCalendarReservationDetailController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-administration.reservation-calendar.reservationDetail', null, { reload: 'common-area-administration.reservation-calendar.reservationDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.new', {
                url: '/newcommonArea',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },

                templateUrl: 'app/entities/common-area/common-area-dialog.html',
                controller: 'CommonAreaDialogController',
                controllerAs: 'vm',

                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            description: null,
                            reservationCharge: null,
                            devolutionAmmount: null,
                            chargeRequired: null,
                            reservationWithDebt: null,
                            picture: null,
                            pictureContentType: null,
                            maximunHours: null,
                            id: null
                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area-administration.editCommonArea', {
                url: '/{id}/editCommonArea',
                data: {
                    authorities: ['ROLE_MANAGER']
                },

                templateUrl: 'app/entities/common-area/common-area-dialog.html',
                controller: 'CommonAreaDialogController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams,CommonArea) {
                        return CommonArea.get({id : $stateParams.id}).$promise;
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('common-area-administration.reservation-calendar', {
                url: '/{id}/reservation-calendar',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl:'app/entities/common-area/reservation-calendar.html',
                controller: 'ReservationCalendarController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams,CommonArea) {
                        return CommonArea.get({id : $stateParams.id}).$promise;
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area-administration.general-reservation-calendar', {
                url: '/general-reservation-calendar',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl:'app/entities/common-area/general-reservation-calendar.html',
                controller: 'GeneralReservationCalendarController',
                controllerAs: 'vm',
            })

            .state('common-area.delete', {
                parent: 'common-area',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area/common-area-delete-dialog.html',
                        controller: 'CommonAreaDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['CommonArea', function (CommonArea) {
                                return CommonArea.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('common-area', null, {reload: 'common-area'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
